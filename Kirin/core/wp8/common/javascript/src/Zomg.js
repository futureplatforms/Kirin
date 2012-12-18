defineModule("Zomg", function (require, exports) {
	var module;
	exports.onLoad = function(obj) {
		console.log("onLoad done!", "INFO");
		module = obj;
		obj.Arrrgh();
		obj.HeresAStringArray_(["string a", "string b", "string c", "string d"]);
		obj.HeresAnIntArray_([17, 123, 345, 823, 11, 99]);
		obj.HeresABoolArray_([true, false, false, false, true]);
		obj.HeresSomeArrays___(["arr1", "arr2", "arr3"], [1, 2, 3], [false, true, false]);
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