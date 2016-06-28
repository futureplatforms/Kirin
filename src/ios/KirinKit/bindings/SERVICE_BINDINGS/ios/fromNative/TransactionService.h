/* Generated from TransactionService
 * Do not edit, as this WILL be overwritten
 */
@protocol TransactionService <NSObject>

- (void) statementFailure: (NSString*) filename : (int) txId : (int) statementId;

- (void) statementTokenSuccess: (NSString*) filename : (int) txId : (int) statementId : (NSString*) token;

- (void) statementRowSuccessColumnNames: (NSString*) filename : (int) txId : (int) statementId : (NSArray*) columnNames;

- (void) statementRowSuccess: (NSString*) filename : (int) txId : (int) statementId : (NSArray*) row;

- (void) statementRowSuccessEnd: (NSString*) filename : (int) txId : (int) statementId;

- (void) statementJSONSuccess: (NSString*) filename : (int) txId : (int) statementId : (NSString*) json;

- (void) endSuccess: (NSString*) filename : (int) txId;

- (void) endFailure: (NSString*) filename : (int) txId;

@end