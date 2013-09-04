package com.futureplatforms.kirin.gwt.compile;

import org.timepedia.exporter.rebind.ExporterGenerator;

import com.futureplatforms.kirin.gwt.client.KirinService;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class NativeProxyInterfaceGenerator extends Generator {

	private final InterfaceGenerator[] mAppProtocolGenerators = {
			new CSClassGenerator("../BINDINGS/windows/fromNative/"),
			new ObjectiveCProtocolGenerator("../BINDINGS/ios/fromNative/")
	};
	
    private final InterfaceGenerator[] mServiceProtocolGenerators = {
            new CSClassGenerator("../SERVICE_BINDINGS/windows/fromNative/"),
            new ObjectiveCProtocolGenerator("../SERVICE_BINDINGS/ios/fromNative/")
    };
	 
	private final ExporterGenerator mExporter = new ExporterGenerator();
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		//System.out.println("Generating protocol for " + typeName);
		
		TypeOracle oracle = context.getTypeOracle();
		 
		JClassType moduleObjectType = oracle.findType(typeName);
		
		InterfaceGenerator[] generators;
        JClassType serviceObjectType = oracle.findType(KirinService.class.getName());
        
        if (serviceObjectType.isAssignableTo(moduleObjectType)) {
            generators = mServiceProtocolGenerators;
        } else {            
            generators = mAppProtocolGenerators;
        }
        
		for (InterfaceGenerator generator : generators) {
			generator.generateProtocolResource(logger, context, moduleObjectType);
		}
		return mExporter.generate(logger, context, typeName);
	}
}
