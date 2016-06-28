/* Generated from NetworkingService
 * Do not edit, as this WILL be overwritten
 */
defineModule('NetworkingServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.payload = function() {    kirinHelper.invokeMethod('payload', arguments);  };
  exports.onError = function() {    kirinHelper.invokeMethod('onError', arguments);  };
});