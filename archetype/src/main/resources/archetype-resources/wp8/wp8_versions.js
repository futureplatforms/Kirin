// DH 2014-03-02
// This is a javascript program to plonk a version of your choosing into a WMAppManifest.xml

// Run from the command line like so:
// cscript versions.js "PATH\To\WMAppManifest.xml" "version.number"

// Give us console.log please
var console = {
	log: function(blah) {
		WScript.Echo(blah)
	}
};

var main = function() {
	var prefix='foo'
	var ns=''
		
	// Get the arguments, for some inexplicable reason this is NOT an array, you access them as WScript.Arguments(n), despite there being a property WScript.Arguments.length
	var args = WScript.Arguments
	for (var i=0,len=args.length; i<len; i++) {
		console.log('args ' + i + ': ' + args(i))
	}

	if (args.length !== 2) {
		console.log('USAGE: versions.js \'PATH\To\WMAppManifest.xml\' \'version.number\'')
		return
	}

	var pathToManifest = args(0)
	var version = args(1)

	var xmlDoc = new ActiveXObject('Msxml2.DOMDocument.6.0')
	xmlDoc.setProperty('SelectionLanguage', 'XPath')
	// You need to set the namespace and give it a prefix if you want to query it using XPath.
	// Doesn't matter what the prefix is, using 'foo' here.
	// http://stackoverflow.com/questions/16490839/how-to-query-default-namespace-with-msxml
	//xmlDoc.setProperty('SelectionNamespaces', 'xmlns:' + prefix + '=\'' + ns + '\'')
	xmlDoc.load(pathToManifest)
	
	var app = xmlDoc.selectNodes('//App')[0]
	app.setAttribute('Version', version)
	xmlDoc.save(pathToManifest)
}

main()