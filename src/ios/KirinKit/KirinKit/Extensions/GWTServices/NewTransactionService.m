//
//  NewTransactionService.m
//  KirinKit
//
//  Created by Douglas Hoskins on 27/11/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewTransactionService.h"
#import "NewTransactionStatement.h"
#import "NewDatabaseAccessService.h"
#import "fromNative/TransactionService.h"
#import "FMDatabaseQueue.h"
#import "FMDatabase.h"
#import "KirinDropbox.h"

@interface NewTransactionService() {
    NewDatabaseAccessService * _DatabaseAccessService;
    NSMutableDictionary * _DbToTx;
}

@property(strong) id<TransactionService> kirinModule;

@end

@implementation NewTransactionService

- (id) initWithDatabaseAccessService: (NewDatabaseAccessService *) databaseAccessService {
    _DatabaseAccessService = databaseAccessService;
    _DbToTx = [[NSMutableDictionary alloc] init];
    NSLog(@"initialising transaction service!");
    self.serviceName = @"TransactionService";
    self.kirinModuleProtocol = @protocol(TransactionService);
    return [super initWithServiceName: self.serviceName];
}

- (NSMutableArray*) getStatements: (int) dbId : (int) txId {
    NSMutableDictionary * txToStatements;
    if ((txToStatements = [_DbToTx objectForKey:@(dbId)]) == nil) {
        txToStatements = [[NSMutableDictionary alloc] init];
        [_DbToTx setObject:txToStatements forKey:@(dbId)];
    }
    
    NSMutableArray *statements;
    if ((statements = [txToStatements objectForKey:@(txId)]) == nil) {
        statements = [[NSMutableArray alloc] init];
        [txToStatements setObject:statements forKey:@(txId)];
    }
    return statements;
}

- (void) begin: (int) dbId : (int) txId {
    [self.kirinModule transactionBeginOnSuccess:dbId :txId];
}

- (void) appendStatementForRows: (int) dbId : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) params {
    NSMutableArray *statements = [self getStatements:dbId :txId];
    [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_rowset andId:statementId andStatement:statement andParameters:params]];
}

- (void) appendStatementForToken: (int) dbId : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) params {
    NSMutableArray *statements = [self getStatements:dbId :txId];
    [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_token andId:statementId andStatement:statement andParameters:params]];
}

- (void) appendBatch: (int) dbId : (int) txId : (NSArray*) batch {
    NSMutableArray *statements = [self getStatements:dbId :txId];
    for (NSString *b in batch) {
        [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_rowset andStatement:b andParameters:nil]];
    }
}

- (void) end: (int) dbId : (int) txId {
    FMDatabaseQueue * dbQueue = [_DatabaseAccessService.DbForId objectForKey:@(dbId)];
    [dbQueue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        NSMutableArray * statements = [self getStatements:dbId :txId];
        for (NewTransactionStatement * st in statements) {
            FMResultSet * s = [db executeQuery:st.statement withArgumentsInArray:st.parameters];
            if ([db hadError]) {
                NSLog(@"DB Error :: %@", [db lastErrorMessage]);
                rollback = YES;
                [self.kirinModule endFailure:dbId :txId];
                return;
            }
            
            if (st.hasId) {
                if (st.type == SQL_rowset) {
                    // ROWSET query
                    // Iterate through column names and send them to kirin
                    NSMutableArray * columnNames = [[NSMutableArray alloc] init];
                    int colCount = [s columnCount];
                    for (int i=0; i<colCount; i++) {
                        [columnNames addObject:[s columnNameForIndex:i]];
                    }
                    [self.kirinModule statementRowSuccessColumnNames:dbId :txId :st.statementId :columnNames];
                
                    // Now iterate through all rows
                    while ([s next]) {
                        // create an array of all this row's values and send them to kirin
                        NSMutableArray * row = [[NSMutableArray alloc] init];
                        for (int i=0; i<colCount; i++) {
                            if (![s columnIndexIsNull:i]) {
                                [row addObject:[s stringForColumnIndex:i]];
                            }
                        }
                        [self.kirinModule statementRowSuccess:dbId :txId :st.statementId :row];
                    }
                
                    // Finally tell Kirin we've finished!
                    [self.kirinModule statementRowSuccessEnd:dbId :txId :st.statementId];
                } else if (st.type == SQL_token) {
                    // TOKEN query
                    NSMutableArray * arr = [[NSMutableArray alloc] init];
                    while ([s next]) {
                        NSDictionary * dict = [s resultDictionary];
                        [arr addObject:dict];
                    }
                
                    NSString *token = [[KIRIN dropbox] putObject:arr];
                    [self.kirinModule statementTokenSuccess:dbId :txId :st.statementId :token];
                }
            } else {
                // FMDB plays silly beggars if you don't iterate through the result set... weird
                while ([s next]);
            }
        }
        [self.kirinModule endSuccess:dbId :txId];
    }];
}

@end
