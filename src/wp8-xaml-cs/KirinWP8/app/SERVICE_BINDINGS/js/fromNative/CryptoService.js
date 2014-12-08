/* Generated from CryptoService
 * Do not edit, as this WILL be overwritten
 */
defineModule('CryptoServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.result = function() {    kirinHelper.invokeMethod('result', arguments);  };
});