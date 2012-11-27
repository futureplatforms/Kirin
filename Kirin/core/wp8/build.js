#!/usr/bin/env node
var kirin_home = process.env["KIRIN_HOME"];
if (kirin_home) {
	console.log("kirin home is " + kirin_home);
	process.env["KIRIN_HOME"] = kirin_home;
	require(kirin_home + "/build.js").build(process.argv, __dirname);
} else {
	console.error("Need to set environment variable KIRIN_HOME");
	process.exit(1);
}
