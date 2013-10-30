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
				var postData = params.postData;
				
				var xhr = new XMLHttpRequest();
				xhr.open(method, url, true);
				for (var header in headers) {
					if (headers.hasOwnProperty(header)) {
						xhr.setRequestHeader(header, headers[header]);
					}
				}

				xhr.onreadystatechange = function() {
					console.log("onreadystatechange: " + xhr.readyState + ", " + xhr.status);
					if (xhr.readyState === 4) {
						if (xhr.status === 200) {
							kirinHelper.executeCallback(payload, [encodeURIComponent(xhr.responseText)]);
						} else {
						    kirinHelper.executeCallback(onError);
						}
					}
				};
				xhr.send(postData);
			}
		};

		var kirin = require('Win8JSKirin');
		kirinHelper = kirin.bindModule(native, "Networking", false);
		kirinHelper.onLoad();
	};
});