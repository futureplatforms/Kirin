/* Generated from TransactionServiceNative
 * Do not edit, as this WILL be overwritten
 */
@protocol TransactionServiceNative <NSObject>

- (void) begin: (int) dbId : (int) txId;

- (void) appendStatementForRows: (int) dbId : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) txParams;

- (void) appendStatementForToken: (int) dbId : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) txParams;

- (void) appendStatementForJSON: (int) dbId : (int) txId : (int) statementId : (NSString*) statement : (NSArray*) txParams;

- (void) appendStatements: (int) dbId : (int) txId : (NSArray*) returnTypes : (NSArray*) statementIds : (NSArray*) statements : (NSArray*) txParams;

- (void) appendBatch: (int) dbId : (int) txId : (NSArray*) batch;

- (void) end: (int) dbId : (int) txId;

@end