defineModule("Zomg", function (require, exports) {
	exports.onLoad = function(obj) {
		console.log("onLoad done!", "INFO");
		obj.Arrrgh_();
	};
});