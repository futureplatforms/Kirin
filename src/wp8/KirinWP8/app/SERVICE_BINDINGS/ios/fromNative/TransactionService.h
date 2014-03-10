/* Generated from TransactionService
 * Do not edit, as this WILL be overwritten
 */
@protocol TransactionService <NSObject>

- (void) transactionBeginOnSuccess: (int) dbId : (int) txId;

- (void) statementFailure: (int) dbId : (int) txId : (int) statementId;

- (void) statementTokenSuccess: (int) dbId : (int) txId : (int) statementId : (NSString*) token;

- (void) statementRowSuccessColumnNames: (int) dbId : (int) txId : (int) statementId : (NSArray*) columnNames;

- (void) statementRowSuccess: (int) dbId : (int) txId : (int) statementId : (NSArray*) row;

- (void) statementRowSuccessEnd: (int) dbId : (int) txId : (int) statementId;

- (void) statementJSONSuccess: (int) dbId : (int) txId : (int) statementId : (NSString*) json;

- (void) endSuccess: (int) dbId : (int) txId;

- (void) endFailure: (int) dbId : (int) txId;

@end