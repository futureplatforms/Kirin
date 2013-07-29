package com.futureplatforms.kirin.gwt.compile;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;

public class CSInterfaceGenerator extends InterfaceGenerator {
	public CSInterfaceGenerator(String partialPath) {
		super(partialPath);
	}
	
	@Override
	public String preambleForClass(String name) {
		String s = "/* Generated from " + name + "\n * Do not edit, as this WILL be overwritten\n */\n";
		s += "namespace Generated {\n";
		s += "interface " + name + " {\n";
		return s;
	}

	@Override
	public String postamble() {
		return "}}";
	}

	protected String getCsMethodSignature(JMethod method) {
		String s = "void " + method.getName() + "(";
		JParameter[] params = method.getParameters();
		for (int i=0; i<params.length; i++) {
			JParameter param = params[i];
			s += getCsTypeForJType(param.getType()) + " " + param.getName();
			if (i < params.length - 1) {
				s += ", ";
			}
		}
		s += ")";
		return s;
	}
	
	@Override
	public String getMethodSignature(JMethod method) {
		return getCsMethodSignature(method) + ";";
	}
	
	protected String getCsTypeForJType(JType type) {
		JClassType classType = type.isClassOrInterface();
		String javaTypeName = type.getQualifiedSourceName();
		String csTypeName = null;
		if (type.isPrimitive() != null) {
			if (javaTypeName.equals("boolean")) {
				csTypeName = "bool";
			} else {
				csTypeName = type.getSimpleSourceName(); 
			}
		} else if (javaTypeName.equals("java.lang.String")) {
			csTypeName = "string";
		} else if (type.isArray() != null) {
			JArrayType arrType = type.isArray();
			csTypeName = getCsTypeForJType(arrType.getComponentType()) + "[] ";
		} else if (classType != null) {
		    if (javaTypeName.equals("com.google.gwt.core.client.JsArray")) {
                csTypeName = "string[]";
            } else if (classType.isAssignableTo(jsObjType)) {
				csTypeName = "Dictionary<string, string>";
			}
		}
		
		if (csTypeName != null) {
			return csTypeName;
		} else {
			System.out.println("Cannot find a corresponding type for " + javaTypeName);
			return null;
		}
	}

	@Override
	public String filenameExtension() {
		return ".cs";
	}
}
