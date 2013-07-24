/* Generated from TestModule
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class TestModule {
private KirinAssistant Assistant;
public TestModule(KirinAssistant ka) {
  this.Assistant = ka;
}
public void testyMethod(string str, int i){
  Assistant.jsMethod("testyMethod", new object[] {str, i});
}

}}