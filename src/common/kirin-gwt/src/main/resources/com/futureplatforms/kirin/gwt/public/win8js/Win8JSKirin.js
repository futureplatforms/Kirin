defineModule("Win8JSKirin", function (require, exports) {
	var objects = [];
	
	exports.bindModule = function bindModule(obj, moduleName, isGwt) {
		return (function() {
			objects[moduleName] = {
				obj: obj,
				isGwt: isGwt
			};
			return {
				onLoad: function onLoad() {
					var methods = [];
					for (var prop in obj) {
						if (obj.hasOwnProperty(prop)) {
							if (typeof obj[prop] === 'function') {
								// For GWT modules we want to stick an underscore per parameter on to the method name
								if (isGwt) { 
									var numParams = obj[prop].prototype.constructor.length;
									for (var i=0; i<numParams; i++) {
										// HACKERY AHOY
										// Some Kirin modules have methods with the same name but
										// different number of parameters.  How can Javascript tell the
										// difference?  Underscores for parameters.  But we don't know which 
										// methods are duplicated in this way.  SO.  If a method has parameters,
										// create versions for all numbers of parameters up to the maximum.
										var propWithUnderscores = prop
										
										for (var j=0; j<(i + 1); j++) {
											propWithUnderscores += "_";
										}
										
										methods.push(propWithUnderscores);
									}
								} 
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