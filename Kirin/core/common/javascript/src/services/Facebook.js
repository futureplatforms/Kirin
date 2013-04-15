defineServiceModule("Facebook", function (require, exports) {
	
	var api = require("api-utils"),
		kirin = require("kirin"),
		backend;
	
	exports.onLoad = function (proxy) {
		backend = proxy;
	};
	
   	exports.onUnload = function () {
   		backend = null;
   	};
	
	exports.openSessionWithReadPermissions = function (readPermissions, successCB, errorCB, cancelCB) {
		var idSuccessCB = kirin.wrapCallback(successCB);
		var idErrorCB = kirin.wrapCallback(errorCB);
		var idCancelCB = kirin.wrapCallback(cancelCB);
		
		backend.openSessionWithReadPermissions_andSuccessCB_andErrorCB_andCancelCB_(readPermissions, successCB, errorCB, idCancelCB);
	};
	
	exports.queryWithFQL = function (fql, successCB, errorCB) {
		var idSuccessCB = kirin.wrapCallback(successCB);
		var idErrorCB = kirin.wrapCallback(errorCB);
		
        backend.queryWithFQL_andSuccessCB_andErrorCB_(fql, idSuccessCB, idErrorCB);
    };
});