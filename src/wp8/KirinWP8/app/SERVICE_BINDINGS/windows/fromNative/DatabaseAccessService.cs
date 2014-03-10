/* Generated from DatabaseAccessService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class DatabaseAccessService {
private KirinAssistant Assistant;
public DatabaseAccessService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void databaseOpenedSuccess(int dbId){
  Assistant.jsMethod("databaseOpenedSuccess", new object[] {dbId});
}

public void databaseOpenedFailure(int dbId){
  Assistant.jsMethod("databaseOpenedFailure", new object[] {dbId});
}

}}