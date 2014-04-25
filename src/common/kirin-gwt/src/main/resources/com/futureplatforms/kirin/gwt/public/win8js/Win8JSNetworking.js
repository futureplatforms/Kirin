defineModule("Win8JSNetworking", function (require, exports) {
	exports.kickOff = function() {
		var native = {
			retrieve______: function(connId, method, url, postData, headerKeys, headerVals) {
				var xhr = new XMLHttpRequest();
				xhr.open(method, url, true);
				for (var i=0; i<headerKeys.length; i++) {
					xhr.setRequestHeader(headerKeys[i], headerVals[i]);
				}
				
				xhr.onreadystatechange = function() {
					console.log("onreadystatechange: " + xhr.readyState + ", " + xhr.status + ", " + xhr.getAllResponseHeaders());
					if (xhr.readyState === 4) {
						if (xhr.status === 0) {
							module.onError(connId);
						} else {
							module.payload(connId, xhr.status, xhr.responseText, [], []);
						}
					}
				};
				xhr.send(postData);
			}
		};
		var module = require('NetworkingServiceBinding');
        module.onLoad('NetworkingService', native)
	}
});