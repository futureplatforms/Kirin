defineModule("Zomg", function (require, exports) {
	exports.onLoad = function(obj) {
		console.log("onLoad done!", "INFO");
		obj.Arrrgh();
		var config = {};
		config.method = "GET";
		config.url = "http://www.google.co.uk/";
		config.postData = null;
		config.headers = "";
		config.payload = function(payload) {
			console.log("THE PAYLOAD WAS " + payload);
		};
		config.onError = function(err) {
			console.log("THE ERROR WAS " + err);
		};
		require("Networking").downloadString(config);
		
		var settings = require("Settings");
		//settings.put("oh", "yes");
		//settings.commit();
		console.log("oh: " + settings.get("oh"));
	};
	
	exports.Whateva = function(num) {
		console.log("Whateva " + num);
	};
});