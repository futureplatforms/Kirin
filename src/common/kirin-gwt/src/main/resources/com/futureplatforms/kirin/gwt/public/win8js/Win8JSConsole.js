defineModule("Win8JSConsole", function (require, exports) {
	exports.kickOff = function() {
		var kirinHelper;
		var native = {
			log_atLevel_: function log_atLevel_(params) {
				consoleLog(params);
			}
		};

		var kirin = require('Win8JSKirin');
		kirinHelper = kirin.bindModule(native, "DebugConsole", false);
		kirinHelper.onLoad();
	};
});