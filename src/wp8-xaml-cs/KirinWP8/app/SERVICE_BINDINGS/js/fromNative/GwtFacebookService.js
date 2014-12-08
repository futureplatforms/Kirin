/* Generated from GwtFacebookService
 * Do not edit, as this WILL be overwritten
 */
defineModule('GwtFacebookServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.openSessionSuccess = function() {    kirinHelper.invokeMethod('openSessionSuccess', arguments);  };
  exports.openSessionCancel = function() {    kirinHelper.invokeMethod('openSessionCancel', arguments);  };
  exports.openSessionError = function() {    kirinHelper.invokeMethod('openSessionError', arguments);  };
  exports.openSessionAuthenticationFailed = function() {    kirinHelper.invokeMethod('openSessionAuthenticationFailed', arguments);  };
  exports.openSessionErrorWithUserMessage = function() {    kirinHelper.invokeMethod('openSessionErrorWithUserMessage', arguments);  };
  exports.requestPublishSuccess = function() {    kirinHelper.invokeMethod('requestPublishSuccess', arguments);  };
  exports.requestPublishCancel = function() {    kirinHelper.invokeMethod('requestPublishCancel', arguments);  };
  exports.requestPublishError = function() {    kirinHelper.invokeMethod('requestPublishError', arguments);  };
  exports.requestPublishAuthenticationFailed = function() {    kirinHelper.invokeMethod('requestPublishAuthenticationFailed', arguments);  };
  exports.shareSuccess = function() {    kirinHelper.invokeMethod('shareSuccess', arguments);  };
  exports.shareCancel = function() {    kirinHelper.invokeMethod('shareCancel', arguments);  };
  exports.shareErr = function() {    kirinHelper.invokeMethod('shareErr', arguments);  };
  exports.setAccessToken = function() {    kirinHelper.invokeMethod('setAccessToken', arguments);  };
  exports.setIsLoggedIn = function() {    kirinHelper.invokeMethod('setIsLoggedIn', arguments);  };
  exports.setAppId = function() {    kirinHelper.invokeMethod('setAppId', arguments);  };
  exports.setCurrentPermissions = function() {    kirinHelper.invokeMethod('setCurrentPermissions', arguments);  };
  exports.requestsDialogSuccess = function() {    kirinHelper.invokeMethod('requestsDialogSuccess', arguments);  };
  exports.requestsDialogCancel = function() {    kirinHelper.invokeMethod('requestsDialogCancel', arguments);  };
  exports.requestsDialogFail = function() {    kirinHelper.invokeMethod('requestsDialogFail', arguments);  };
});