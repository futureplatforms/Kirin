package com.futureplatforms.kirin.gwt.compile;

import java.text.MessageFormat;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;

public class ObjectiveCProtocolGenerator extends InterfaceGenerator {

	public ObjectiveCProtocolGenerator(String partialPath) {
		super(partialPath);
	}
	

	@Override
	public String preambleForClass(String name) {
		String s = "/* Generated from " + name + "\n * Do not edit, as this WILL be overwritten\n */\n";
		s += "@protocol " + name + " <NSObject>\n\n";
		return s;
	}

	@Override
	public String getMethodSignature(JMethod method) {
		StringBuilder sb = new StringBuilder("- (void) ").append(method.getName());
		boolean first = true;
		for (JParameter param : method.getParameters()) {
			String s = getIosParam(param);
			if (s == null) {
				return null;
			}
			if (first) {
				first = false;
			} else {
				sb.append(" ");
			}
			sb.append(s);
		}
		
		sb.append(";\n");
		return sb.toString();
	}

	private String getIosParam(JParameter param) {
		JType type = param.getType();
		String name = param.getName();
		
		JClassType classType = type.isClassOrInterface();
		String javaTypeName = type.getQualifiedSourceName();
		String objcTypeName = null;
		if (type.isPrimitive() != null) {
			
			if (javaTypeName.equals("boolean")) {
				objcTypeName = "BOOL";
			} else {
				objcTypeName = type.getSimpleSourceName(); 
			}
		} else if (javaTypeName.equals("java.lang.String")) {
			objcTypeName = "NSString*";
		} else if (type.isArray() != null) {
			objcTypeName = "NSArray*";
		} else if (classType != null) {
			//if (classType.isAssignableTo(mClassCollection)) {
			//} else if (classType.isAssignableTo(mClassMap)) {
		    if (javaTypeName.equals("com.google.gwt.core.client.JsArray")) {
                objcTypeName = "NSArray*";
            } else if (classType.isAssignableTo(jsObjType)) {
				objcTypeName = "NSDictionary*";
			} else {
				
			}
		}
		
		if (objcTypeName != null) {
			return MessageFormat.format(": ({0}) {1}", objcTypeName, name);
		} else {
			System.out.println("Cannot find a corresponding type for " + javaTypeName);
			return null;
		}
	}

	@Override
	public String postamble() {
		return "@end";
	}


	@Override
	public String filenameExtension() {
		return ".h";
	}


}
