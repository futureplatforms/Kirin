package com.futureplatforms.kirin.gwt.compile.js;

import com.futureplatforms.kirin.gwt.compile.InterfaceGenerator;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;

public class JSToNativeGenerator extends InterfaceGenerator {
	private String _Name;
	public JSToNativeGenerator(String partialPath) {
		super(partialPath);
	}

	@Override
	public String preambleForClass(String name) {
		this._Name = name;
		StringBuilder sb = new StringBuilder();
		sb.append("/* Generated from " + name + "\n * Do not edit, as this WILL be overwritten\n */\n");
		// The generated module needs 'Binding' appending to the name to differentiate it from the actual module
		sb.append("defineModule('" + name + "Binding', function(require, exports) {");
		return sb.toString();
	}

	@Override
	public String postamble() {
		return "});";
	}

	@Override
	public String getMethodSignature(JMethod method) {
		StringBuilder sb = new StringBuilder();
		
		StringBuilder _s = new StringBuilder();
		for (JParameter param : method.getParameters()) {
			_s.append('_');
		}
		
		sb.append("  exports." + method.getName() + _s.toString() + " = function(");
		JParameter[] params = method.getParameters();
		for (int i=0, len=params.length; i<len; i++) {
			sb.append("arg" + i);
			if (i < (params.length - 1)) {
				sb.append(", ");
			}
		}
		sb.append(") {");
		sb.append("    console.log('" + _Name + "." + method.getName() + _s.toString() + "('");
		for (int i=0, len=params.length; i<len; i++) {
			sb.append(" + arg" + i + " + ', '");
		}
		sb.append(" + ')');");
		sb.append("  };");
		return sb.toString();	
	}

	@Override
	public String filenameExtension() {
		return ".js";
	}

}
