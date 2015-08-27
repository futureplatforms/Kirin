package com.futureplatforms.kirin.gwt.compile.tonative;

import java.io.PrintWriter;

import com.futureplatforms.kirin.IKirinProxied;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class NativeObjectImplementationGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
						   String typeName) throws UnableToCompleteException {
		TypeOracle oracle = context.getTypeOracle();
		JClassType nativeObjectType = oracle.findType(typeName);
		JClassType genericNativeObjectType = oracle.findType(IKirinProxied.class.getName());
		final String genPackageName = nativeObjectType.getPackage().getName();
		final String genClassName = nativeObjectType.getSimpleSourceName() + "Impl";

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				genPackageName, genClassName);

		composer.addImport("com.google.gwt.core.client.JavaScriptObject");
		composer.addImport(typeName);
		composer.addImport(genericNativeObjectType.getQualifiedSourceName());

		composer.addImplementedInterface(nativeObjectType.getName());
		composer.addImplementedInterface(genericNativeObjectType.getName());

		PrintWriter printWriter = context.tryCreate(logger, genPackageName,
				genClassName);

		if (printWriter != null) {
			SourceWriter sourceWriter = composer.createSourceWriter(context,
					printWriter);
			// Create parameterless constructor
			sourceWriter.println(genClassName + "() {}");

			// Add $setKirinNativeObject method to implement IKirinProxied
			sourceWriter.println("private JavaScriptObject jso;");
			sourceWriter
					.println("public void $setKirinNativeObject(JavaScriptObject jso) {");
			sourceWriter.indent();
			sourceWriter.println("this.jso = jso;");
			sourceWriter.outdent();
			sourceWriter.println("}");

			// Now public and native implementations of each method of the
			// screen interface
			printAllMethods(nativeObjectType, genericNativeObjectType, sourceWriter);
			sourceWriter.commit(logger);
		}

		return composer.getCreatedClassName();
	}

	/**
	 * Recursively print all methods in the supplied type and its supertypes and implemented interfaces.
	 * @param jct
	 * @param genericNativeObjectType
	 * @param sourceWriter
	 */
	private void printAllMethods(JClassType jct, JClassType genericNativeObjectType, SourceWriter sourceWriter) {
		JMethod[] methods = jct.getMethods();
		for (JMethod method : methods) {
			if (!method.getEnclosingType().equals(genericNativeObjectType)) {
				printMethods(method, sourceWriter);
			}
		}

		JClassType[] interfaces = jct.getImplementedInterfaces();
		if (interfaces != null) {
			for (JClassType inter : interfaces) {
				printAllMethods(inter, genericNativeObjectType, sourceWriter);
			}
		}
	}

	private void printMethods(JMethod method, SourceWriter sw) {
		JParameter[] params = method.getParameters();

		// first of all the regular public implementation of the method
		sw.print("public " + method.getReturnType().getQualifiedSourceName()
				+ " " + method.getName() + "(");
		for (int i = 0; i < params.length; i++) {
			JParameter param = params[i];
			sw.print(param.getType().getQualifiedSourceName() + " "
					+ param.getName());
			if (i != params.length - 1) {
				sw.print(", ");
			}
		}
		sw.println(") {");
		sw.indent();

		// now invoke the native method, which will have the same name prefixed
		// with underscore
		// FIXME: this will be horrible if we have multiple overloaded functions
		// with same name, perhaps include signature in the name somehow?
		// but this would probably have unintended consequences anyway, best not
		// to overload i guess.
		sw.print("_" + method.getName() + "(");
		for (JParameter param : params) {
			sw.print(param.getName() + ", ");
		}
		sw.println("jso);");
		sw.outdent();
		sw.println("}");

		// ok now we define the native method
		sw.print("private static native void _" + method.getName() + "(");
		for (JParameter param : params) {
			sw.print(param.getType().getQualifiedSourceName() + " "
					+ param.getName() + ", ");
		}
		// passing in the handle to the JS object
		sw.println("JavaScriptObject jso) /*-{");
		sw.indent();

		// now invoke the method on the JS object. This is the method name,
		// followed by as many underscores as there are parameters
		sw.print("jso." + method.getName());
		for (int i = 0; i < params.length; i++) {
			sw.print("_");
		}
		// and pass all parameters in
		sw.print("(");
		for (int i = 0; i < params.length; i++) {
			sw.print(params[i].getName());
			if (i != params.length - 1) {
				sw.print(", ");
			}
		}
		sw.println(");");
		sw.outdent();
		sw.println("}-*/;");
	}
}
