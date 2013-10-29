defineModule("Win8JSKirin", function (require, exports) {
	var objects = [];
	
	exports.bindModule = function bindModule(obj, moduleName, isGwt) {
		return (function() {
			objects[moduleName] = {
				obj: obj,
				isGwt: isGwt
			};
			var invokeMethod = function invokeMethod(methodName, params) {
				if (!params || params.length === 0) {
					EXPOSED_TO_NATIVE.native2js.execMethod(moduleName, methodName);
				} else {
					EXPOSED_TO_NATIVE.native2js.execMethod(moduleName, methodName, params);
				}
			};
			return {
				onLoad: function onLoad() {
					var methods = [];
					for (var prop in obj) {
						if (obj.hasOwnProperty(prop)) {
							if (typeof obj[prop] === 'function') {
								var numParams = obj[prop].prototype.constructor.length;
								for (var j=0; j<numParams; j++) {
									prop += "_";
								}
								methods.push(prop);
							}
						}
					}
					EXPOSED_TO_NATIVE.native2js.loadProxyForModule(moduleName, methods);
				},
				
				invokeMethod: function(methodName, params) {
					invokeMethod.apply(invokeMethod, arguments);
				}
			};
		})();
	};
	
	exports.getModule = function(name) {
		return objects[name];
	};
});