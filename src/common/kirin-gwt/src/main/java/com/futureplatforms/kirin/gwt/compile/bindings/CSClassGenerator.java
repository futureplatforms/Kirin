package com.futureplatforms.kirin.gwt.compile.bindings;

import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;

public class CSClassGenerator extends CSInterfaceGenerator {

	public CSClassGenerator(String partialPath) {
		super(partialPath);
	}

	@Override
	public String preambleForClass(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("/* Generated from " + name + "\n * Do not edit, as this WILL be overwritten\n */\n");
		sb.append("using KirinWindows.Core;\n");
		sb.append("namespace Generated {\n");
		sb.append("public class " + name + " {\n");
		sb.append("private KirinAssistant Assistant;\n");
		sb.append("public " + name + "(KirinAssistant ka) {\n");
		sb.append("  this.Assistant = ka;\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	@Override
	public String getMethodSignature(JMethod method) {
		StringBuilder sb = new StringBuilder("public " + getCsMethodSignature(method));
		sb.append("{\n");
		sb.append("  Assistant.jsMethod(\"");
		sb.append(method.getName() + "\", new object[] {");
		JParameter[] params = method.getParameters();
		for (int i=0; i<params.length; i++) {
			sb.append(params[i].getName());
			if (i < params.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("});\n");
		sb.append("}\n");
		return sb.toString();
	}
}
