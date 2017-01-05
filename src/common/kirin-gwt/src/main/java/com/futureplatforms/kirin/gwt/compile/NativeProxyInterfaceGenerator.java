package com.futureplatforms.kirin.gwt.compile;

import org.timepedia.exporter.rebind.ExporterGenerator;

import com.futureplatforms.kirin.gwt.compile.bindings.CSClassGenerator;
import com.futureplatforms.kirin.gwt.compile.bindings.ObjectiveCProtocolGenerator;
import com.futureplatforms.kirin.gwt.compile.js.JSFromNativeGenerator;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class NativeProxyInterfaceGenerator extends Generator {

	private final InterfaceGenerator[] mProtocolGenerators = {
			new CSClassGenerator("../BINDINGS/windows/fromNative/"),
			new ObjectiveCProtocolGenerator("../BINDINGS/ios/fromNative/"),
			new JSFromNativeGenerator("../BINDINGS/js/fromNative/")
	};
	 
	private final ExporterGenerator mExporter = new ExporterGenerator();
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		//System.out.println("Generating protocol for " + typeName);
		
		TypeOracle oracle = context.getTypeOracle();
		 
		JClassType nativeObjectType = oracle.findType(typeName);
		for (InterfaceGenerator generator : mProtocolGenerators) {
			generator.generateProtocolResource(logger, context, nativeObjectType);
		}
		return mExporter.generate(logger, context, typeName);
	}
}
