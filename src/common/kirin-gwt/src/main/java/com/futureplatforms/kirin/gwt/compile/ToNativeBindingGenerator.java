package com.futureplatforms.kirin.gwt.compile;


import com.futureplatforms.kirin.gwt.client.IKirinNativeService;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class ToNativeBindingGenerator {

    private final InterfaceGenerator[] mAppProtocolGenerators = {
            new CSInterfaceGenerator("../BINDINGS/windows/toNative/"),
            new ObjectiveCProtocolGenerator("../BINDINGS/ios/toNative/")
    };
    
    private final InterfaceGenerator[] mServiceProtocolGenerators = {
            new CSInterfaceGenerator("../SERVICE_BINDINGS/windows/toNative/"),
            new ObjectiveCProtocolGenerator("../SERVICE_BINDINGS/ios/toNative/")
    };
	 
	public void generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
	    System.out.println("----> ToNativeBindingGenerator: " + typeName);
	    TypeOracle oracle = context.getTypeOracle();
	    JClassType nativeObjectType = oracle.findType(typeName);

	    InterfaceGenerator[] generators;
        JClassType nativeServiceType = oracle.findType(IKirinNativeService.class.getName());
        
        if (nativeObjectType.isAssignableTo(nativeServiceType)) {
            generators = mServiceProtocolGenerators;
        } else {            
            generators = mAppProtocolGenerators;
        }
        
		for (InterfaceGenerator generator : generators) {
			generator.generateProtocolResource(logger, context, nativeObjectType);
		}
	}
}
