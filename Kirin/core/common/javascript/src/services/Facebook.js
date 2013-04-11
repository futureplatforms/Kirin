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
	
	exports.openSessionWithReadPermissions = function (readPermissions) {
		backend.openSessionWithReadPermissions_(readPermissions);
	};
});