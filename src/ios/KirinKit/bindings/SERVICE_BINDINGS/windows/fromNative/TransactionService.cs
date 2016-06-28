/* Generated from TransactionService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
using System.Collections.Generic;
namespace Generated {
public class TransactionService {
private KirinAssistant Assistant;
public TransactionService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void statementFailure(string filename, int txId, int statementId){
  Assistant.jsMethod("statementFailure", new object[] {filename, txId, statementId});
}

public void statementTokenSuccess(string filename, int txId, int statementId, string token){
  Assistant.jsMethod("statementTokenSuccess", new object[] {filename, txId, statementId, token});
}

public void statementRowSuccessColumnNames(string filename, int txId, int statementId, string[]  columnNames){
  Assistant.jsMethod("statementRowSuccessColumnNames", new object[] {filename, txId, statementId, columnNames});
}

public void statementRowSuccess(string filename, int txId, int statementId, string[]  row){
  Assistant.jsMethod("statementRowSuccess", new object[] {filename, txId, statementId, row});
}

public void statementRowSuccessEnd(string filename, int txId, int statementId){
  Assistant.jsMethod("statementRowSuccessEnd", new object[] {filename, txId, statementId});
}

public void statementJSONSuccess(string filename, int txId, int statementId, string json){
  Assistant.jsMethod("statementJSONSuccess", new object[] {filename, txId, statementId, json});
}

public void endSuccess(string filename, int txId){
  Assistant.jsMethod("endSuccess", new object[] {filename, txId});
}

public void endFailure(string filename, int txId){
  Assistant.jsMethod("endFailure", new object[] {filename, txId});
}

}}