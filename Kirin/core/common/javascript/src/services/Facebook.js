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
		
		backend.openSessionWithReadPermissions_andSuccessCB_andErrorCB_andCancelCB_(readPermissions, idSuccessCB, idErrorCB, idCancelCB);
	};
	
	exports.queryWithFQL = function (fql, successCB, errorCB) {
		var idSuccessCB = kirin.wrapCallback(successCB);
		var idErrorCB = kirin.wrapCallback(errorCB);
		
        backend.queryWithFQL_andSuccessCB_andErrorCB_(fql, idSuccessCB, idErrorCB);
    };
    
    exports.openFeedDialog = function(toFriendUIDMayBeNull, linkUrl, linkName, linkCaption, linkDescription, successCB, errorCB, cancelCB) {
		var idSuccessCB = kirin.wrapCallback(successCB);
		var idErrorCB = kirin.wrapCallback(errorCB);
		var idCancelCB = kirin.wrapCallback(cancelCB);
		
		backend.openFeedDialog_withLinkUrl_withLinkName_withLinkCaption_withLinkDescription_andSuccessCB_andErrorCB_andCancelCB_(toFriendUIDMayBeNull, linkUrl, linkName, linkCaption, linkDescription, idSuccessCB, idErrorCB, idCancelCB);
    };
    
    exports.openRequestDialog = function(toFriendsUIDsMayBeNull, messageMayBeNull, successCB, errorCB, cancelCB) {
		var idSuccessCB = kirin.wrapCallback(successCB);
		var idErrorCB = kirin.wrapCallback(errorCB);
		var idCancelCB = kirin.wrapCallback(cancelCB);
		
		backend.openRequestDialog_withMessage_andSuccessCB_andErrorCB_andCancelCB_(toFriendsUIDsMayBeNull, messageMayBeNull, idSuccessCB, idErrorCB, idCancelCB);
    };
});