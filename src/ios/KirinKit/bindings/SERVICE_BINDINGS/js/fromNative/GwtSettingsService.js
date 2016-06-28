/* Generated from GwtSettingsService
 * Do not edit, as this WILL be overwritten
 */
defineModule('GwtSettingsServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.mergeOrOverwrite = function() {    kirinHelper.invokeMethod('mergeOrOverwrite', arguments);  };
});