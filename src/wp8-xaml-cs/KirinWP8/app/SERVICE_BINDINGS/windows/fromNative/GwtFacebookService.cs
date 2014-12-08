/* Generated from GwtFacebookService
 * Do not edit, as this WILL be overwritten
 */
using KirinWindows.Core;
namespace Generated {
public class GwtFacebookService {
private KirinAssistant Assistant;
public GwtFacebookService(KirinAssistant ka) {
  this.Assistant = ka;
}
public void openSessionSuccess(int fbId){
  Assistant.jsMethod("openSessionSuccess", new object[] {fbId});
}

public void openSessionCancel(int fbId){
  Assistant.jsMethod("openSessionCancel", new object[] {fbId});
}

public void openSessionError(int fbId){
  Assistant.jsMethod("openSessionError", new object[] {fbId});
}

public void openSessionAuthenticationFailed(int fbId){
  Assistant.jsMethod("openSessionAuthenticationFailed", new object[] {fbId});
}

public void openSessionErrorWithUserMessage(int fbId, string msg){
  Assistant.jsMethod("openSessionErrorWithUserMessage", new object[] {fbId, msg});
}

public void requestPublishSuccess(int fbId){
  Assistant.jsMethod("requestPublishSuccess", new object[] {fbId});
}

public void requestPublishCancel(int fbId){
  Assistant.jsMethod("requestPublishCancel", new object[] {fbId});
}

public void requestPublishError(int fbId){
  Assistant.jsMethod("requestPublishError", new object[] {fbId});
}

public void requestPublishAuthenticationFailed(int fbId){
  Assistant.jsMethod("requestPublishAuthenticationFailed", new object[] {fbId});
}

public void shareSuccess(int cbId, string res){
  Assistant.jsMethod("shareSuccess", new object[] {cbId, res});
}

public void shareCancel(int cbId){
  Assistant.jsMethod("shareCancel", new object[] {cbId});
}

public void shareErr(int cbId){
  Assistant.jsMethod("shareErr", new object[] {cbId});
}

public void setAccessToken(int cbId, string accessToken, string expirationDate){
  Assistant.jsMethod("setAccessToken", new object[] {cbId, accessToken, expirationDate});
}

public void setIsLoggedIn(int cbId, bool isLoggedIn){
  Assistant.jsMethod("setIsLoggedIn", new object[] {cbId, isLoggedIn});
}

public void setAppId(int cbId, string appId){
  Assistant.jsMethod("setAppId", new object[] {cbId, appId});
}

public void setCurrentPermissions(int cbId, string[]  currentPermissions){
  Assistant.jsMethod("setCurrentPermissions", new object[] {cbId, currentPermissions});
}

public void requestsDialogSuccess(int cbId, string respQuery){
  Assistant.jsMethod("requestsDialogSuccess", new object[] {cbId, respQuery});
}

public void requestsDialogCancel(int cbId){
  Assistant.jsMethod("requestsDialogCancel", new object[] {cbId});
}

public void requestsDialogFail(int cbId){
  Assistant.jsMethod("requestsDialogFail", new object[] {cbId});
}

}}