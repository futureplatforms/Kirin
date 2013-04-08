defineServiceModule("Sharing", function (require, exports) {
	
	var api = require("api-utils"),
		kirin = require("kirin"),
		backend;
	
	exports.onLoad = function (proxy) {
		backend = proxy;
	};
	
   	exports.onUnload = function () {
   		backend = null;
   	};
	
	exports.share = function (serviceNameHint, text, optionalUrl) {
		if(optionalUrl) {
			backend.share_withThisLink_usingService_(text, optionalUrl, serviceNameHint);
		} else {
			backend.share_usingService_(text, serviceNameHint);						
		}
	};
});