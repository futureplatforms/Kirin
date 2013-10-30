defineModule("Win8JSSettings", function (require, exports) {
	var existingKirinSettings = function existingKirinSettings() {
		var settings = {};
		for (var i = 0; i < localStorage.length; i++){
			var key = localStorage.key(i);
			if (key.indexOf('kirin-') === 0) {
				var value = localStorage.getItem(key);
				settings[key] = value;
			}
		}
		return settings;
	};

	
	exports.kickOff = function() {
		var kirinHelper;
		var native = {
				updateContents_withDeletes_: function updateContents_withDeletes_(adds, deletes) {
					// add all the adds
					for (var key in adds) {
						if (adds.hasOwnProperty(key)) {
							var value = adds[key];
							localStorage.setItem('kirin-' + key, value);
						}
					}
					
					// delete all the deletes
					for (var key in deletes) {
						if (deletes.hasOwnProperty(key)) {
							localStorage.removeItem(key);
						}
					}
				},
				
				requestPopulateJSWithCallback_: function requestPopulateJSWithCallback_(callback) {
					kirinHelper.executeCallback(callback, existingKirinSettings());
				}
		};

		var kirin = require('Win8JSKirin');
		kirinHelper = kirin.bindModule(native, "Settings", false);
		kirinHelper.onLoad();
		
		kirinHelper.invokeMethod('mergeOrOverwrite', existingKirinSettings());
		kirinHelper.invokeMethod('resetEnvironment');
	};
});