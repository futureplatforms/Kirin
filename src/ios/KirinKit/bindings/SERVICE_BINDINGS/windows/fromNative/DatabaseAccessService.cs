/* Generated from DatabaseAccessService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
using System.Collections.Generic;
namespace Generated {
public class DatabaseAccessService {
private KirinAssistant Assistant;
public DatabaseAccessService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void databaseOpenedSuccess(string filename){
  Assistant.jsMethod("databaseOpenedSuccess", new object[] {filename});
}

public void databaseOpenedFailure(string filename){
  Assistant.jsMethod("databaseOpenedFailure", new object[] {filename});
}

}}