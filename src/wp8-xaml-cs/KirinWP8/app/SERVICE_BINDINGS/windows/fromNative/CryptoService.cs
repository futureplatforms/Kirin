/* Generated from CryptoService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class CryptoService {
private KirinAssistant Assistant;
public CryptoService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void result(string cbId, string res){
  Assistant.jsMethod("result", new object[] {cbId, res});
}

}}