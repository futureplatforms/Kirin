/* Generated from TransactionServiceNative
 * Do not edit, as this WILL be overwritten
 */
namespace Generated {
public interface TransactionServiceNative {
void appendStatementForRows(string filename, int txId, int statementId, string statement, string[]  txParams);
void appendStatementForToken(string filename, int txId, int statementId, string statement, string[]  txParams);
void appendStatementForJSON(string filename, int txId, int statementId, string statement, string[]  txParams);
void appendStatements(string filename, int txId, int[]  returnTypes, int[]  statementIds, string[]  statements, string[]  txParams);
void appendBatch(string filename, int txId, string[]  batch);
void end(string filename, int txId);
}}