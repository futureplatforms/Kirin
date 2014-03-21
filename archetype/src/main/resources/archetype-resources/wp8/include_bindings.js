// DH 2014-03-02
// This is a javascript program to get all the old generated bindings out of our .jsproj and get the new ones in

// Please invoke me from within the local folder!

// Give us console.log please
var console = {
	log: function(blah) {
		WScript.Echo(blah)
	}
}

var main = function() {
	var prefix='foo'
	var ns='http://schemas.microsoft.com/developer/msbuild/2003'
		
	var platformDetails = function(platform) {
		return {
			extension: (platform === 'wp8') ? '.csproj' : '.jsproj',
			includeNode: (platform === 'wp8') ? 'Compile' : 'Content'
		}
	}
	
	/**
	 * returns the project suffix for the current platform
	 */
	var projectEnding = function(platform) {
		return (platform === 'wp8') ? '.csproj' : '.jsproj'
	}
	
	/**
	 * returns the name of the source file include XML node eg <Compile Include="file.cs"/>
	 */
	var includeNode = function(platform) {
		return (platform === 'wp8') ? 'Compile' : 'Content'
	}
	
	/**
	 *  Removes any compile includes for Kirin bindings.  We are looking for entries like:
	 *      <Compile Include="app\BINDINGS\windows\toNative\KickOffScreen.cs" />
	 */
	var removeBindingsFromGroup = function(itemGroup, path, desiredBindings) {
		var indexOf = function(list, str) {
			for (var i=0,len=list.length; i<len; i++) {
				if (list[i] === str) { return i }
			}
			return -1
		}
		var deleted = 0
		var compiles = itemGroup.selectNodes('.//' + prefix + ':' + includeNode(platform))
		for (var i=0,len=compiles.length; i<len; i++) {
			var compile = compiles[i]
			var include = compile.getAttribute('Include')
			if (include.indexOf(path) === 0) {
				
				
				// OK this is a potential.
				// Is it in our desired bindings?
				var index = indexOf(desiredBindings, include)
				if (index === -1) {
					// NO this is not a desired binding!  Remove it!
					compile.parentNode.removeChild(compile)
					if (debug) {
						console.log('removed ' + include)
					}
					deleted++
				} else {
					// YES this is a desired binding!  Keep it and remove the index
					desiredBindings.splice(index, 1)
				}
			}
		}
		
		if (itemGroup.childNodes.length === 0) {
			itemGroup.parentNode.removeChild(itemGroup)
		}
		return deleted
	}
	
	var loadDoc = function() {
		var xmlDoc = new ActiveXObject('Msxml2.DOMDocument.6.0')
		xmlDoc.setProperty('SelectionLanguage', 'XPath')
		// You need to set the namespace and give it a prefix if you want to query it using XPath.
		// Doesn't matter what the prefix is, using 'foo' here.
		// http://stackoverflow.com/questions/16490839/how-to-query-default-namespace-with-msxml
		xmlDoc.setProperty('SelectionNamespaces', 'xmlns:' + prefix + '=\'' + ns + '\'')
		xmlDoc.load(filename + '\\' + filename + projectEnding(platform))
		var err = xmlDoc.parseError
		if (err.errorCode !== 0) {
			console.log('ERROR (' + err.errorCode + '): ' + err.reason)
		}
		return xmlDoc
	}
	
	var cleanCsProj = function(xmlDoc, path, desiredBindings) {
		var deleted = 0
		// Find all the itemGroups
		var itemGroups = xmlDoc.selectNodes('//' + prefix + ':ItemGroup')
		for (var i=0,len=itemGroups.length; i<len; i++) {
			deleted += removeBindingsFromGroup(itemGroups[i], path, desiredBindings)
		}
		
		return deleted
	}
	
	var findBindings = function() {
		var fso = new ActiveXObject('Scripting.FileSystemObject')
		var bindings = []
		
		var addAllFromPath = function(path) {
			var addAllFromFolder = function (path, folder) {
				var subFolders = new Enumerator(folder.SubFolders)
				for (;!subFolders.atEnd();subFolders.moveNext()) {
					var subFolder = subFolders.item()
					addAllFromFolder(path + '\\' + subFolder.name, subFolder)
				}
				var files = new Enumerator(folder.files)
				
				var excludes = [ '.cache.html', '.symbolMap', 'manifest.txt' ]
				
				for (;!files.atEnd();files.moveNext()) {
					var file = files.item()
					var exclude = false
					for (var i=0,len=excludes.length; i<len; i++) {
						if (file.name.indexOf(excludes[i]) !== -1) {
							exclude = true
							break
						}
					}
					if (!exclude) {
						bindings.push(path + '\\' + file.name)
					}
				}
			}
		
			try {
				var rootFolder = fso.GetFolder('.\\' + filename + '\\' + path)
				addAllFromFolder(path, rootFolder)
			} catch(e) {
				console.log('No such folder: ' + filename + '\\' + path + ' (possibly not an error)')
			}
		}
		
		var addGoodiesFromFolder = function(folderName) {
			// Use the FileSystemObject to access our BINDINGS folder
			// http://msdn.microsoft.com/en-us/library/2z9ffy99(v=vs.84).aspx
			// http://msdn.microsoft.com/en-us/library/hww8txat(v=vs.84).aspx
			try {
				var folder = fso.GetFolder('.\\' + filename + '\\' + folderName)
				
				// 'new Enumerator', what?!?  Shoot me now...  http://msdn.microsoft.com/en-us/library/e1dthkks(v=vs.84).aspx
				var subFolders = new Enumerator(folder.SubFolders)
				for (;!subFolders.atEnd();subFolders.moveNext()) {
					var subFolder = subFolders.item()
					var files = new Enumerator(subFolder.files)
					for (;!files.atEnd();files.moveNext()) {
						var file = files.item()
						bindings.push(folderName + subFolder.name + '\\' + file.name)
					}
				}
			} catch(e) {
				console.log('No such folder: ' + folderName + ' (possibly not an error)')
			}
		}
		
		var bindingFolder = (platform === 'wp8') ? 'windows' : 'js'
		addAllFromPath('app\\BINDINGS\\' + bindingFolder)
		addAllFromPath('app\\SERVICE_BINDINGS\\' + bindingFolder)
		//addGoodiesFromFolder('app\\SERVICE_BINDINGS\\' + bindingFolder + '\\')
		
		return bindings
	}

	var addBindingsToProj = function(bindings, xmlDoc) {
		// For each binding we want to add something like this:
		//       <Compile Include="BINDINGS\js\toNative\KickOffScreen.cs" />
		// Put them all within a single ItemGroup, which must come under the document's Project element
		
		// TRIED creating a new ItemGroup but you get all sorts of headaches with namespaces.  So let's GRAB any old ItemGroup,
		// the first one in the document will do.  
		// EXCEPT if it has Label="ProjectConfigurations"
		var itemGroups = xmlDoc.selectNodes('//' + prefix + ':ItemGroup')
		var itemGroup 
		
		for (var i=0,len=itemGroups.length; i<len; i++) {
			itemGroup = itemGroups[i]
			var label = itemGroup.getAttribute('Label')
			
			if (!(label && label === 'ProjectConfigurations')) {
				break
			}
		}
		
		// And ADD all the new Compile nodes to that group
		for (var i=0,len=bindings.length; i<len; i++) {
			var binding = bindings[i]
			var compile = xmlDoc.createNode(1, includeNode(platform), ns)
			compile.setAttribute('Include', binding)
			itemGroup.appendChild(compile)
			if (debug) {
				console.log('adding ' + binding)
			}
		}
	}

	// Get the arguments, for some inexplicable reason this is NOT an array, you access them as WScript.Arguments(n), despite there being a property WScript.Arguments.length
	var args = WScript.Arguments
	var debug = false
	for (var i=0,len=args.length; i<len; i++) {
		console.log('args ' + i + ': ' + args(i))
		if (args(i) === 'debug') {
			debug = true
		}
	}

	if (args.length < 2) {
		console.log('USAGE: include_bindings.js project_name platform (either wp8 or w8js) <debug (optional)>')
		return
	}

	var filename = args(0)
	var platform = args(1)
	if (platform !== 'wp8' && platform !== 'w8js') {
		console.log('platform must be wp8 or w8js')
		return
	}
	
	// Find all bindings that we want to put in the project, this is a list of strings of files in the app folder windows toNative and fromNative subfolders
	var desiredBindings = findBindings()
	
	if (debug) {
		console.log('desiredBindings :: ' + desiredBindings.length)
		for (var i=0,len=desiredBindings.length; i<len; i++) {
			console.log(i + ": " + desiredBindings[i])
		}
	}
	
	// Open the .csproj XML doc
	var xmlDoc = loadDoc()
	
	// Find out how many files are currently in there that we have deleted
	var deleted = cleanCsProj(xmlDoc, 'app', desiredBindings)
	// Also, any that we want that were already in there will have been removed from the array
	
	// We have either deleted some, OR there are some we still need to add
	// Add the remaining bindings
	if (deleted > 0 || desiredBindings.length > 0) {
		addBindingsToProj(desiredBindings, xmlDoc)
	
		// Save it
		xmlDoc.save(filename + '\\' + filename + projectEnding(platform))
	} else {
		console.log('it\'s the same')
	}
}

main()