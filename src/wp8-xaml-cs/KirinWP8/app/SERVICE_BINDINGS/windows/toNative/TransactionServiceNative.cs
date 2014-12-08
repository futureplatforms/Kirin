/* Generated from TransactionServiceNative
 * Do not edit, as this WILL be overwritten
 */
namespace Generated {
public interface TransactionServiceNative {
void begin(int dbId, int txId);
void appendStatementForRows(int dbId, int txId, int statementId, string statement, string[]  txParams);
void appendStatementForToken(int dbId, int txId, int statementId, string statement, string[]  txParams);
void appendStatementForJSON(int dbId, int txId, int statementId, string statement, string[]  txParams);
void appendStatements(int dbId, int txId, int[]  returnTypes, int[]  statementIds, string[]  statements, string[]  txParams);
void appendBatch(int dbId, int txId, string[]  batch);
void end(int dbId, int txId);
}}