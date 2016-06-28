/* Generated from GwtLocationService
 * Do not edit, as this WILL be overwritten
 */
defineModule('GwtLocationServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.updatingLocationCallback = function() {    kirinHelper.invokeMethod('updatingLocationCallback', arguments);  };
  exports.hasPermissionCallback = function() {    kirinHelper.invokeMethod('hasPermissionCallback', arguments);  };
  exports.locationError = function() {    kirinHelper.invokeMethod('locationError', arguments);  };
});