package com.futureplatforms.kirin.gwt.compile.bindings;

import com.futureplatforms.kirin.gwt.compile.InterfaceGenerator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public abstract class BindingGenerator {
    
    private final InterfaceGenerator[] mAppProtocolGenerators = {
            new CSClassGenerator("../BINDINGS/windows/" + getBindingType() + "/"),
            new ObjectiveCProtocolGenerator("../BINDINGS/ios/" + getBindingType() + "/")
    };
    
    private final InterfaceGenerator[] mServiceProtocolGenerators = {
            new CSClassGenerator("../SERVICE_BINDINGS/windows/" + getBindingType() + "/"),
            new ObjectiveCProtocolGenerator("../SERVICE_BINDINGS/ios/" + getBindingType() + "/")
    };
    
    public abstract String getBindingType();
    
    public void generate(TreeLogger logger, GeneratorContext context,
            String typeName) throws UnableToCompleteException {
        
    }
}
