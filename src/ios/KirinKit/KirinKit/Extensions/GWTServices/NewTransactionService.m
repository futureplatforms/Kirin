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

- (void) appendFile: (int) dbId : (int) txId : (NSString*) filename {
    //NSMutableArray *statements = [self getStatements:dbId :txId];
   // [statements addObject:[[NewTransactionStatement alloc] initWithType:token andId:statementId andStatement:statement andParameters:params]];
}

- (void) end: (int) dbId : (int) txId {
    FMDatabaseQueue * dbQueue = [_DatabaseAccessService.DbForId objectForKey:@(dbId)];
    [dbQueue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        NSMutableArray * statements = [self getStatements:dbId :txId];
        for (NewTransactionStatement * st in statements) {
            FMResultSet * s = [db executeQuery:st.statement withArgumentsInArray:st.parameters];
            NSLog(@"executed it");
            if (st.type == SQL_rowset) {
                NSLog(@"it's a rowset");
                NSMutableArray * columnNames = [[NSMutableArray alloc] init];
                int colCount = [s columnCount];
                for (int i=0; i<colCount; i++) {
                    [columnNames addObject:[s columnNameForIndex:i]];
                }
                NSLog(@"got column names");
                [self.kirinModule statementRowSuccessColumnNames:dbId :txId :st.statementId :columnNames];
                
                while ([s next]) {
                    NSLog(@"s next");
                    NSMutableArray * row = [[NSMutableArray alloc] init];
                    for (int i=0; i<colCount; i++) {
                        [row addObject:[s stringForColumnIndex:i]];
                    }
                    NSLog(@"got row details");
                    [self.kirinModule statementRowSuccess:dbId :txId :st.statementId :row];
                }
                [self.kirinModule statementRowSuccessEnd:dbId :txId :st.statementId];
            }
            NSLog(@"end statement");
        }
        NSLog(@"end in transaction");
    }];
}

@end
