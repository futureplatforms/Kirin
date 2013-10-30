defineModule("Win8JSNetworking", function (require, exports) {
	exports.kickOff = function() {
		var kirinHelper;
		var native = {
			downloadString_: function downloadString_(params) {
				var method = params.method;
				var url = params.url;
				var headers = params.headers;
				var payload = params.payload;
				var onError = params.onError;
				
				var xhr = new XMLHttpRequest();
				
				for (var header in headers) {
					if (headers.hasOwnProperty(header)) {
						xhr.setRequestHeader(header, headers[header]);
					}
				}
				
				xhr.open(method, url, true);
				xhr.onreadystatechange = function() {
					console.log("onreadystatechange: " + xhr.readyState + ", " + xhr.status);
					if (xhr.readyState === 4) {
						if (xhr.status === 200) {
							kirinHelper.executeCallback(payload, xhr.responseText);
						}
					}
				};
				xhr.send();
			}
		};

		var kirin = require('Win8JSKirin');
		kirinHelper = kirin.bindModule(native, "Networking", false);
		kirinHelper.onLoad();
	};
});