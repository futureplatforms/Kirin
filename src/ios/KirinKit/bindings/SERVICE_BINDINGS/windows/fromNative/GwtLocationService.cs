/* Generated from GwtLocationService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
using System.Collections.Generic;
namespace Generated {
public class GwtLocationService {
private KirinAssistant Assistant;
public GwtLocationService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void updatingLocationCallback(string lat, string lng, string acc, string timestamp){
  Assistant.jsMethod("updatingLocationCallback", new object[] {lat, lng, acc, timestamp});
}

public void hasPermissionCallback(int cbId, bool hasPermission){
  Assistant.jsMethod("hasPermissionCallback", new object[] {cbId, hasPermission});
}

public void locationError(string err){
  Assistant.jsMethod("locationError", new object[] {err});
}

}}