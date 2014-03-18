
/* Generated from SymbolMapServiceNative
 * Do not edit, as this WILL be overwritten
 */
defineModule("Win8JSSymbolMaps", function (require, exports) {
	exports.kickOff = function() {
		var kirinHelper;
		var native = {
			setSymbolMapDetails__: function setSymbolMapDetails__(moduleName, strongName) {
				console.log('setSymbolMapDetails__(' + moduleName + ', ' + strongName + ')')
				var uri = new Windows.Foundation.Uri('ms-appx:///app/WEB-INF/' + moduleName + '/symbolMaps/' + strongName + '.symbolMap')
				Windows.Storage.StorageFile.getFileFromApplicationUriAsync(uri).done(function(file) {
					var text = Windows.Storage.FileIO.readTextAsync(file)
					console.log('heres the text: ' + text)
				})
				module.setSymbolMap
			}
		};
		var module = require('SymbolMapServiceBinding');
        module.onLoad('SymbolMapService', native)
	};
});