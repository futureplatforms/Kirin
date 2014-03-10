/* Generated from SymbolMapService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class SymbolMapService {
private KirinAssistant Assistant;
public SymbolMapService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void setSymbolMap(string symbolMap){
  Assistant.jsMethod("setSymbolMap", new object[] {symbolMap});
}

}}