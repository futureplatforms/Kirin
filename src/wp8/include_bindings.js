// DH 2014-03-02
// This is a javascript program to get all the old generated bindings out of our .csproj and get the new ones in

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
		
	/**
	 *  Removes any compile includes for Kirin bindings.  We are looking for entries like:
	 *      <Compile Include="app\BINDINGS\windows\toNative\KickOffScreen.cs" />
	 */
	var removeBindingsFromGroup = function(itemGroup, desiredBindings) {
		var indexOf = function(list, str) {
			for (var i=0,len=list.length; i<len; i++) {
				if (list[i] === str) { return i }
			}
			return -1
		}
		var deleted = 0
		var compiles = itemGroup.selectNodes('.//' + prefix + ':Compile')
		for (var i=0,len=compiles.length; i<len; i++) {
			var compile = compiles[i]
			var include = compile.getAttribute('Include')
			if (include.indexOf('app') === 0) {
				// OK this is a potential.
				// Is it in our desired bindings?
				var index = indexOf(desiredBindings, include)
				if (index === -1) {
					// NO this is not a desired binding!  Remove it!
					compile.parentNode.removeChild(compile)
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
		xmlDoc.load(filename + '\\' + filename + '.csproj')
		var err = xmlDoc.parseError
		if (err.errorCode !== 0) {
			console.log('ERROR (' + err.errorCode + '): ' + err.reason)
		}
		return xmlDoc
	}
	
	var cleanCsProj = function(xmlDoc, desiredBindings) {
		var deleted = 0
		// Find all the itemGroups
		var itemGroups = xmlDoc.selectNodes('//' + prefix + ':ItemGroup')
		for (var i=0,len=itemGroups.length; i<len; i++) {
			deleted += removeBindingsFromGroup(itemGroups[i], desiredBindings)
		}
		
		return deleted
	}
	
	var findBindings = function() {
		var fso = new ActiveXObject('Scripting.FileSystemObject')
		var bindings = []
		
		var addGoodiesFromFolder = function(folderName) {
			// Use the FileSystemObject to access our BINDINGS folder
			// http://msdn.microsoft.com/en-us/library/2z9ffy99(v=vs.84).aspx
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
		
		addGoodiesFromFolder('app\\BINDINGS\\windows\\')
		addGoodiesFromFolder('app\\SERVICE_BINDINGS\\windows\\')
		
		return bindings
	}

	var addBindingsToProj = function(bindings, xmlDoc) {
		// For each binding we want to add something like this:
		//       <Compile Include="BINDINGS\windows\toNative\KickOffScreen.cs" />
		// Put them all within a single ItemGroup, which must come under the document's Project element
		
		// TRIED creating a new ItemGroup but you get all sorts of headaches with namespaces.  So let's GRAB any old ItemGroup,
		// the first one in the document will do
		var itemGroup = xmlDoc.selectNodes('//' + prefix + ':ItemGroup')[0]
		
		// And ADD all the new Compile nodes to that group
		for (var i=0,len=bindings.length; i<len; i++) {
			var binding = bindings[i]
			var compile = xmlDoc.createNode(1, 'Compile', ns)
			compile.setAttribute('Include', binding)
			itemGroup.appendChild(compile)
		}
	}

	// Get the arguments, for some inexplicable reason this is NOT an array, you access them as WScript.Arguments(n), despite there being a property WScript.Arguments.length
	var args = WScript.Arguments
	for (var i=0,len=args.length; i<len; i++) {
		console.log('args ' + i + ': ' + args(i))
	}

	if (args.length !== 1) {
		console.log('USAGE: include_bindings.js project_name')
		return
	}

	var filename = args(0)

	// Find all bindings that we want to put in the project, this is a list of strings of files in the app folder windows toNative and fromNative subfolders
	var desiredBindings = findBindings()
	
	// Open the .csproj XML doc
	var xmlDoc = loadDoc()
	
	// Find out how many files are currently in there that we have deleted
	var numDeleted = cleanCsProj(xmlDoc, desiredBindings)
	// Also, any that we want that were already in there will have been removed from the array
	
	if (numDeleted > 0 || desiredBindings.length > 0) {
		// We have either deleted some, OR there are some we still need to add
		// Add the remaining bindings
		addBindingsToProj(desiredBindings, xmlDoc)
		
		// Save it
		xmlDoc.save(filename + '\\' + filename + '.csproj')
	} else {
		// No changes this time!  Not touching the .csproj file means Visual Studio won't give us a nasty warning popup
		console.log('not changed')
	}
}

main()