defineModule("Zomg", function (require, exports) {
	var module;
	exports.onLoad = function(obj) {
		console.log("onLoad done!", "INFO");
		module = obj;
		obj.Arrrgh();
		obj.HeresAString_("a string");
		obj.HeresAnInt_(1337);
		obj.HeresABool_(true);
		obj.HeresAStringArray_(["string a", "string b", "string c", "string d"]);
		obj.HeresAnIntArray_([17, 123, 345, 823, 11, 99]);
		obj.HeresABoolArray_([true, false, false, false, true]);
		obj.HeresSomeArrays___(["arr1", "arr2", "arr3"], [1, 2, 3], [false, true, false]);
		//obj.HeresSomeArraysOfArrays__([["string 00", "string 01"], ["string 10", "string 11", "string 12"]], [[[0, 1, 2], [2, 3 4]], [[1, 2, 3], [4, 5 6]]]);
		
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
		settings.put("when i see you celtic", "i go out of my head");
		settings.put("i just can't get enough", "i just can't get enough");
		settings.put("all the things you do to me", "and everything you say");
		settings.put("i slip and slide and i fall in love", "and i just can't seem to get enough of");
		settings.commit();
		//settings.put("oh", "yes");
		//settings.commit();
		console.log("oh: " + settings.get("oh"));
	};
	
	exports.Whateva = function(num) {
		console.log("Whateva " + num);
	};
});