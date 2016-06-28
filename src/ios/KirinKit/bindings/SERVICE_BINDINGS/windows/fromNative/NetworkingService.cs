/* Generated from NetworkingService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
using System.Collections.Generic;
namespace Generated {
public class NetworkingService {
private KirinAssistant Assistant;
public NetworkingService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void payload(int connId, int respCode, string str, string[]  headerKeys, string[]  headerVals){
  Assistant.jsMethod("payload", new object[] {connId, respCode, str, headerKeys, headerVals});
}

public void onError(int connId){
  Assistant.jsMethod("onError", new object[] {connId});
}

}}