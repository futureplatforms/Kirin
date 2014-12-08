/* Generated from TransactionService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class TransactionService {
private KirinAssistant Assistant;
public TransactionService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void transactionBeginOnSuccess(int dbId, int txId){
  Assistant.jsMethod("transactionBeginOnSuccess", new object[] {dbId, txId});
}

public void statementFailure(int dbId, int txId, int statementId){
  Assistant.jsMethod("statementFailure", new object[] {dbId, txId, statementId});
}

public void statementTokenSuccess(int dbId, int txId, int statementId, string token){
  Assistant.jsMethod("statementTokenSuccess", new object[] {dbId, txId, statementId, token});
}

public void statementRowSuccessColumnNames(int dbId, int txId, int statementId, string[]  columnNames){
  Assistant.jsMethod("statementRowSuccessColumnNames", new object[] {dbId, txId, statementId, columnNames});
}

public void statementRowSuccess(int dbId, int txId, int statementId, string[]  row){
  Assistant.jsMethod("statementRowSuccess", new object[] {dbId, txId, statementId, row});
}

public void statementRowSuccessEnd(int dbId, int txId, int statementId){
  Assistant.jsMethod("statementRowSuccessEnd", new object[] {dbId, txId, statementId});
}

public void statementJSONSuccess(int dbId, int txId, int statementId, string json){
  Assistant.jsMethod("statementJSONSuccess", new object[] {dbId, txId, statementId, json});
}

public void endSuccess(int dbId, int txId){
  Assistant.jsMethod("endSuccess", new object[] {dbId, txId});
}

public void endFailure(int dbId, int txId){
  Assistant.jsMethod("endFailure", new object[] {dbId, txId});
}

}}