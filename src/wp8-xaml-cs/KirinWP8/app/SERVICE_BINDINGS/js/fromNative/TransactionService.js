/* Generated from TransactionService
 * Do not edit, as this WILL be overwritten
 */
defineModule('TransactionServiceBinding', function(require, exports) {	 var kirinHelper;	 exports.onLoad = function(moduleName, native) {    var kirin = require('Win8JSKirin');    kirinHelper = kirin.bindModule(native, moduleName, true);    kirinHelper.onLoad();  };  exports.onResume = function() {    kirinHelper.invokeMethod('onResume');  };  exports.transactionBeginOnSuccess = function() {    kirinHelper.invokeMethod('transactionBeginOnSuccess', arguments);  };
  exports.statementFailure = function() {    kirinHelper.invokeMethod('statementFailure', arguments);  };
  exports.statementTokenSuccess = function() {    kirinHelper.invokeMethod('statementTokenSuccess', arguments);  };
  exports.statementRowSuccessColumnNames = function() {    kirinHelper.invokeMethod('statementRowSuccessColumnNames', arguments);  };
  exports.statementRowSuccess = function() {    kirinHelper.invokeMethod('statementRowSuccess', arguments);  };
  exports.statementRowSuccessEnd = function() {    kirinHelper.invokeMethod('statementRowSuccessEnd', arguments);  };
  exports.statementJSONSuccess = function() {    kirinHelper.invokeMethod('statementJSONSuccess', arguments);  };
  exports.endSuccess = function() {    kirinHelper.invokeMethod('endSuccess', arguments);  };
  exports.endFailure = function() {    kirinHelper.invokeMethod('endFailure', arguments);  };
});