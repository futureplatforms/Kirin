
/* Generated from SymbolMapServiceNative
 * Do not edit, as this WILL be overwritten
 */
defineModule("Win8JSSymbolMaps", function (require, exports) {
	exports.kickOff = function() {
		var native = {
			setSymbolMapDetails__: function setSymbolMapDetails__(moduleName, strongName) {
				console.log('setSymbolMapDetails__(' + moduleName + ', ' + strongName + ')')
				var uri = new Windows.Foundation.Uri('ms-appx:///app/WEB-INF/' + moduleName + '/symbolMaps/' + strongName + '.symbolMap')
				if(!Windows.Xbox)
					Windows.Storage.StorageFile.getFileFromApplicationUriAsync(uri).done(function(file) {
						Windows.Storage.FileIO.readTextAsync(file).done(function(fileContent) {
							module.setSymbolMap(fileContent);
						})
					})
			}
		};
		var module = require('SymbolMapServiceBinding');
        module.onLoad('SymbolMapService', native)
	};
});