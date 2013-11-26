package com.futureplatforms.kirin.gwt.compile.tonative;


import com.futureplatforms.kirin.gwt.client.IKirinNativeService;
import com.futureplatforms.kirin.gwt.compile.InterfaceGenerator;
import com.futureplatforms.kirin.gwt.compile.bindings.CSInterfaceGenerator;
import com.futureplatforms.kirin.gwt.compile.bindings.ObjectiveCProtocolGenerator;
import com.futureplatforms.kirin.gwt.compile.js.JSToNativeGenerator;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class ToNativeBindingGenerator extends Generator {

    private final InterfaceGenerator[] mAppProtocolGenerators = {
            new CSInterfaceGenerator("../BINDINGS/windows/toNative/"),
            new ObjectiveCProtocolGenerator("../BINDINGS/ios/toNative/"),
            new JSToNativeGenerator("../BINDINGS/js/toNative/")
    };
    
    private final InterfaceGenerator[] mServiceProtocolGenerators = {
            new CSInterfaceGenerator("../SERVICE_BINDINGS/windows/toNative/"),
            new ObjectiveCProtocolGenerator("../SERVICE_BINDINGS/ios/toNative/"),
            new JSToNativeGenerator("../SERVICE_BINDINGS/js/toNative/")
    };
	 
	public String generate(TreeLogger logger, GeneratorContext context,
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
		
		return null;
	}
}
