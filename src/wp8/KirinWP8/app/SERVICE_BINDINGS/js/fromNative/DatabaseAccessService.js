/* Generated from DatabaseAccessService
 * Do not edit, as this WILL be overwritten
 */
defineModule('DatabaseAccessServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.databaseOpenedSuccess = function() {    kirinHelper.invokeMethod('databaseOpenedSuccess', arguments);  };
  exports.databaseOpenedFailure = function() {    kirinHelper.invokeMethod('databaseOpenedFailure', arguments);  };
});