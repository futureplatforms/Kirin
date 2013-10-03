package com.futureplatforms.kirin.gwt.compile.tonative;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class NativeObjectGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context,
            String typeName) throws UnableToCompleteException {
        // ToNativeBindingGenerator just generates iOS and Windows binding headers
        new ToNativeBindingGenerator().generate(logger, context, typeName);
        
        // NativeObjectImplementationGenerator actually generates a class implementation
        // that we want to use, so it needs to be returned
        return new NativeObjectImplementationGenerator().generate(logger, context, typeName);
    }

}
