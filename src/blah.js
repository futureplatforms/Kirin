// Give us console.log please
var console = {
	log: function(blah) {
		WScript.Echo(blah)
	}
}

var fso = new ActiveXObject("Scripting.FileSystemObject")
try {
	var folder = fso.GetFolder('c:\\blah')
	console.log('worked')
} catch(e) {
	console.log('caught')
}