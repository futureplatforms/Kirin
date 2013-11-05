defineModule("Win8JSKirin", function (require, exports) {
	var objects = [];
	
	exports.bindModule = function bindModule(obj, moduleName) {
		return (function() {
			objects[moduleName] = obj;
			return {
				onLoad: function onLoad() {
					var methods = [];
					for (var prop in obj) {
						if (obj.hasOwnProperty(prop)) {
							if (typeof obj[prop] === 'function') {
								methods.push(prop);
							}
						}
					}
					EXPOSED_TO_NATIVE.native2js.loadProxyForModule(moduleName, methods);
				},
				
				invokeMethod: function(methodName, params) {
					if (!params || params.length === 0) {
						EXPOSED_TO_NATIVE.native2js.execMethod(moduleName, methodName);
					} else {
						EXPOSED_TO_NATIVE.native2js.execMethod(moduleName, methodName, params);
					}
				}, 
				
				executeCallback: function(callback, params) {
					if (!params || params.length === 0) {
						EXPOSED_TO_NATIVE.native2js.execCallback(callback);
					} else {
						EXPOSED_TO_NATIVE.native2js.execCallback(callback, params);
					}
				}
			};
		})();
	};
	
	exports.getModule = function(name) {
		return objects[name];
	};
});