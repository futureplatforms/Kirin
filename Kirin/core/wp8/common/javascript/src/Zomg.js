defineModule("Zomg", function (require, exports) {
	exports.onLoad = function(obj) {
		console.log("onLoad done!", "INFO");
		obj.Arrrgh_();
		var config = {};
		config.method = "GET";
		config.url = "http://www.google.co.uk/";
		config.postData = "";
		config.headers = "";
		config.payload = function(payload) {
			console.log("THE PAYLOAD WAS " + payload);
		};
		config.onError = function(err) {
			console.log("THE ERROR WAS " + err);
		};
		require("Networking").downloadString(config);
	};
});