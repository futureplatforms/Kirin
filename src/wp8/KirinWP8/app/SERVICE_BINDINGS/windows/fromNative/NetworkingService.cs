/* Generated from NetworkingService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class NetworkingService {
private KirinAssistant Assistant;
public NetworkingService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void payload(int connId, string str){
  Assistant.jsMethod("payload", new object[] {connId, str});
}

public void onError(int connId){
  Assistant.jsMethod("onError", new object[] {connId});
}

}}