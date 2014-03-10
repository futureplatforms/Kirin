/* Generated from SymbolMapService
 * Do not edit, as this WILL be overwritten
 */
defineModule('SymbolMapServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.setSymbolMap = function() {    kirinHelper.invokeMethod('setSymbolMap', arguments);  };
});