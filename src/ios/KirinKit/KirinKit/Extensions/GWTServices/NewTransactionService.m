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
#import <fromNative/TransactionService.h>
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
    DLog(@"initialising transaction service!");
    self.serviceName = @"TransactionService";
    self.kirinModuleProtocol = @protocol(TransactionService);
    return [super initWithServiceName: self.serviceName];
}

- (NSMutableArray*) getStatements: (NSString *) filename : (int) txId {
    NSMutableDictionary * txToStatements;
    if ((txToStatements = [_DbToTx objectForKey:filename]) == nil) {
        txToStatements = [[NSMutableDictionary alloc] init];
        [_DbToTx setObject:txToStatements forKey:filename];
    }
    
    NSMutableArray *statements;
    if ((statements = [txToStatements objectForKey:@(txId)]) == nil) {
        statements = [[NSMutableArray alloc] init];
        [txToStatements setObject:statements forKey:@(txId)];
    }
    return statements;
}

- (void) appendStatementForRows: (NSString *) filename : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) params {
    NSMutableArray *statements = [self getStatements:filename :txId];
    [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_rowset andId:statementId andStatement:statement andParameters:params]];
}

- (void) appendStatementForToken: (NSString *) filename : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) params {
    NSMutableArray *statements = [self getStatements:filename :txId];
    [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_token andId:statementId andStatement:statement andParameters:params]];
}

- (void) appendStatementForJSON: (NSString *) filename : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) params {
    NSMutableArray *statements = [self getStatements:filename :txId];
    [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_json andId:statementId andStatement:statement andParameters:params]];
}


- (void) appendBatch: (NSString *) filename : (int) txId : (NSArray*) batch {
    NSMutableArray *statements = [self getStatements:filename :txId];
    for (NSString *b in batch) {
        [statements addObject:[[NewTransactionStatement alloc] initWithType:SQL_rowset andStatement:b andParameters:nil]];
    }
}

// Appending a bunch of statements.
// enum StatementReturnType { Rows, Token, JSON, Batch };
- (void) appendStatements: (NSString *) filename : (int) txId : (NSArray*) returnTypes : (NSArray*) statementIds : (NSArray*) statements : (NSArray*) txParams {
    NSMutableArray *nativeStatements = [self getStatements:filename :txId];
    NSUInteger numStatements = [returnTypes count];
    for (int i=0; i<numStatements; i++) {
        int returnType = [((NSNumber*)returnTypes[i]) intValue];
        SQLOperationType opType;
        if (returnType == 0) {
            opType = SQL_rowset;
        } else if (returnType == 1) {
            opType = SQL_token;
        } else if (returnType == 2) {
            opType = SQL_json;
        } else if (returnType == 3) {
            opType = SQL_batch;
        } else {
            NSLog(@"ERROR :: newTransactionService unknown returnType %d", returnType);
            return;
        }
        NSString * statement = statements[i];
        
        if (opType == SQL_batch) {
            [nativeStatements addObject:[[NewTransactionStatement alloc] initWithType:SQL_rowset andStatement:statement andParameters:nil]];
        } else {
            int statementId = [((NSNumber*)statementIds[i]) intValue];
            
            NSString * json = txParams[i];
            
            NSArray * params = [NSJSONSerialization JSONObjectWithData:[json dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil];
            [nativeStatements addObject:[[NewTransactionStatement alloc] initWithType:opType andId:statementId andStatement:statement andParameters:params]];
        }
    }
}

- (void) end: (NSString *) filename : (int) txId {
    NSLog(@"End transaction on database %@", filename);
    FMDatabaseQueue * dbQueue = [_DatabaseAccessService.DbForFilename objectForKey:filename];
    [dbQueue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        NSMutableArray * statements = [self getStatements:filename :txId];
        for (NewTransactionStatement * st in statements) {
            FMResultSet * s = [db executeQuery:st.statement withArgumentsInArray:st.parameters];
            if ([db hadError]) {
                DLog(@"DB Error :: %@", [db lastErrorMessage]);
                BOOL rb = YES;
                rollback = &rb;
                [self.kirinModule endFailure:filename :txId];
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
                    [self.kirinModule statementRowSuccessColumnNames:filename :txId :st.statementId :columnNames];
                
                    // Now iterate through all rows
                    while ([s next]) {
                        // create an array of all this row's values and send them to kirin
                        NSMutableArray * row = [[NSMutableArray alloc] init];
                        for (int i=0; i<colCount; i++) {
                            if ([s columnIndexIsNull:i]) {
                                [row addObject:[NSNull null]];
                            } else {
                                [row addObject:[s stringForColumnIndex:i]];
                            }
                        }
                        [self.kirinModule statementRowSuccess:filename :txId :st.statementId :row];
                    }
                
                    // Finally tell Kirin we've finished!
                    [self.kirinModule statementRowSuccessEnd:filename :txId :st.statementId];
                } else if (st.type == SQL_token || st.type == SQL_json) {
                    // TOKEN query
                    NSMutableArray * arr = [[NSMutableArray alloc] init];
                    while ([s next]) {
                        //NSDictionary * dict = [NSDictionary dictionaryWithDictionary:[s resultDictionary]];
                        NSDictionary * dict = [[s resultDictionary] copy];
                        [arr addObject:dict];
                    }
                
                    if (st.type == SQL_token) {
                        NSString *token = [[KIRIN dropbox] putObject:arr];
                        [self.kirinModule statementTokenSuccess:filename :txId :st.statementId :token];
                    } else {
                        NSString *json = [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:arr options:0 error:nil] encoding:NSUTF8StringEncoding];
                        [self.kirinModule statementJSONSuccess: filename :txId :st.statementId :json];
                    }
                } else {
                    NSLog(@"DB ERROR -- UNEXPECTED TYPE");
                }
            } else {
                // FMDB plays silly beggars if you don't iterate through the result set... weird
                while ([s next]);
            }
        }
        [self.kirinModule endSuccess:filename :txId];
    }];
}

- (dispatch_queue_t) dispatchQueue {
    return [NewDatabaseAccessService getDatabaseDispatchQueue];
}


@end
