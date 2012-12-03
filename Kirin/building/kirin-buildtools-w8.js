var childProcess = require("child_process"), 
	path = require("path"),
	fileUtils = require("./fileUtils");

var verbose = fileUtils.verbose;
var iphoneSDK = null;

var _ = require("underscore");
function compileProject (environment, dir, callback, errback) {
	verbose = environment.verbose;
	var directory = path.join(dir, environment["w8.dir"] || "w8");
	var msbuildHome = environment["msbuild_home"] || "c:\\windows\\Microsoft.NET\\Framework\\v4.0.30319";
	if (!path.existsSync(directory)) {
		console.log("# cd " + directory + " # directory doesn't exist");
		callback();
		return;
	}

	if (!iphoneSDK) {
		childProcess.exec(msbuildHome + "\\MSBuild KirinW8.csproj", function (error, stdout, stderr) {
			if (!error) {
				iphoneSDK = stdout.toString().replace(/[\n\s]/g, "");
				compileProject (environment, dir, callback, errback);
			} else {
				errback(error);
			}
			
		});
		return;
	}

	var cwd = process.cwd();
	
	console.log("cd \"" + directory + "\"");
	var args = ["xcodebuild"];

	if (iphoneSDK) {
		args.push("-sdk " + iphoneSDK);
	}
	
	if (environment["w8.project"]) {
		args.push("-project " + environment["w8.project"]);		
	}
	
	if (environment["w8.code_sign_identity"]) {
		args.push("CODE_SIGN_IDENTITY=\"" + environment["w8.code_sign_identity"] + "\"");
	}

	if (environment["w8.other_code_sign_flags"]) {
		args.push("OTHER_CODE_SIGN_FLAGS=\"" + environment["w8.other_code_sign_flags"] + "\"");
	}

	if (environment["w8.provisioning_profile"]) {
		args.push("PROVISIONING_PROFILE=\"" + environment["w8.provisioning_profile"] + "\"");
	}
	
	if (environment["w8.workspace"]) {
		args.push("-workspace " + environment["w8.workspace"]);
	}
	
	if (environment["w8.target"]) {
		args.push("-target " + environment["w8.target"]);	
	}
	
	if (environment["w8.configuration"]) {
		args.push("-configuration " + environment["w8.configuration"]);	
	} else {
		environment["w8.configuration"] = "Release";
	}

	args.push("clean build");
	var cmd = args.join(" ");
	console.log(cmd);
	//process.chdir(directory);
	if (environment.dryRun) {
		console.log("cd -");
		callback();
		return;
	}
	childProcess.exec(cmd, {maxBuffer: 1024*1024, cwd: directory }, function (error, stdout, stderr) {
		console.log("cd -");
		if (error) {
			console.log("---------------------------------------");
			console.dir(error);
			console.log(stdout);
			console.log("---------------------------------------");
			console.log(stderr);
			if (errback) {
				errback(error);
			}
		} else {
			if (verbose) {
				console.log(stdout);
			}
			if (callback) {
				callback();
			}
		}
	});
}

function checkAndCompile (environment, dir, buildType, callback, errback) {

	var cb = callback;
	if (!environment["w8.isFramework"]) {
		cb = function () {
			var archiveFile = environment.appFile || path.join(process.cwd(), environment.name + "-" + environment["w8.configuration"] + "-" + buildType + ".zip");
			var appPath = path.join(dir, environment["w8.dir"] || "w8", "build", environment["w8.configuration"] + "-iphoneos");
			var zipCmd = "zip -r -T -y '" + archiveFile + "' *.app";
			console.log("cd " + appPath);
			console.log(zipCmd);
			if (environment.dryRun) {
				console.log("cd -");
				callback();
				return;
			}
			
			childProcess.exec(zipCmd, { cwd: appPath }, function (error, stdout, stderr) {
				if (!error) {
					console.log(stdout);
					if (callback) {
						console.log("cd -");
						callback();
					}
				}
			});
		}
	}
	
	compileProject(environment, dir, cb, errback);
}

/*
 * Native compiling methods.
 */
exports.compileApplication = function (environment, dir, buildType, callback, errback) {
	checkAndCompile(environment, dir, buildType, callback, errback);
};
exports.compileDependency = function (environment, dir, callback, errback) {
	checkAndCompile(environment, dir, null, callback, errback);
};

exports.deriveBuildPath = function (dir, environment) {
	return path.join(dir, (environment["w8.dir"] || "w8"), "generated-javascript");
}

exports.initializeProject = function (environment, subProjects) {
	
	console.dir(environment);
	console.log("-------------------------------------------");	
	var fileUtils = require("./fileUtils");
	var projects = _.map(subProjects, function (p) {
		var info = p.info;
		info.cwd = p.cwd;
		info['w8.dir'] = path.join(p.cwd, info['w8.dir']);
		return info;
	});
	
		
	_.each(projects, function (p) {
		console.dir(p);
			console.log("-------------------------------------------");	
	});
	
	var base = _.last(projects);
	var workspace = path.join(base.cwd, "w8", base.name + ".xcworkspace");
	fileUtils.mkdirs(workspace);

	var lines = [ 	'<?xml version="1.0" encoding="UTF-8"?>', 
					"<Workspace", 
					'   version = "1.0">' ];
	
	_.each(projects, function (p) {
		lines.push("   <FileRef");
		lines.push('      location = "absolute:' + path.join(p['w8.dir'], p['w8.project']) + '">');
		lines.push("   </FileRef>");
	});
	
	lines.push("</Workspace>");
	
	fileUtils.writeLines(path.join(workspace, "contents.xcworkspacedata"), lines);

};
