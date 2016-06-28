/* Generated from TransactionServiceNative
 * Do not edit, as this WILL be overwritten
 */
@protocol TransactionServiceNative <NSObject>

- (void) appendStatementForRows: (NSString*) filename : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) txParams;

- (void) appendStatementForToken: (NSString*) filename : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) txParams;

- (void) appendStatementForJSON: (NSString*) filename : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) txParams;

- (void) appendStatements: (NSString*) filename : (int) txId : (NSArray*) returnTypes : (NSArray*) statementIds : (NSArray*) statements : (NSArray*) txParams;

- (void) appendBatch: (NSString*) filename : (int) txId : (NSArray*) batch;

- (void) end: (NSString*) filename : (int) txId;

@end