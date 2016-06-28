/* Generated from GwtSettingsService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
using System.Collections.Generic;
namespace Generated {
public class GwtSettingsService {
private KirinAssistant Assistant;
public GwtSettingsService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void mergeOrOverwrite(string settingsJson){
  Assistant.jsMethod("mergeOrOverwrite", new object[] {settingsJson});
}

}}