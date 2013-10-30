package com.futureplatforms.kirin.gwt.compile;

import com.google.gwt.core.ext.typeinfo.JMethod;

public class JSBindingGenerator extends InterfaceGenerator {

	public JSBindingGenerator(String partialPath) {
		super(partialPath);
	}

	@Override
	public String preambleForClass(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("/* Generated from " + name + "\n * Do not edit, as this WILL be overwritten\n */\n");
		// The generated module needs 'Binding' appending to the name to differentiate it from the actual module
		sb.append("defineModule('" + name + "Binding', function(require, exports) {");
		sb.append("	 var kirinHelper;");
		sb.append("	 exports.onLoad = function(moduleName, native) {");
		sb.append("    var kirin = require('Win8JSKirin');");
		sb.append("    kirinHelper = kirin.bindModule(native, moduleName, true);");
		sb.append("    kirinHelper.onLoad();");
		sb.append("  };");
		
		// Synthesize an onResume method, all modules need it but for some arcane reason it gets treated specially by Kirin
		sb.append("  exports.onResume = function() {");
		sb.append("    kirinHelper.invokeMethod('onResume');");
		sb.append("  };");
		return sb.toString();
	}

	@Override
	public String postamble() {
		return "});";
	}

	@Override
	public String getMethodSignature(JMethod method) {
		StringBuilder sb = new StringBuilder();
		sb.append("  exports." + method.getName() + " = function() {");
		sb.append("    kirinHelper.invokeMethod('" + method.getName() + "', arguments);");
		sb.append("  };");
		return sb.toString();
	}

	@Override
	public String filenameExtension() {
		return ".js";
	}

}
