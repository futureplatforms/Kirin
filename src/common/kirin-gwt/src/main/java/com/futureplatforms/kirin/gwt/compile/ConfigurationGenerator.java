package com.futureplatforms.kirin.gwt.compile;

import java.io.PrintWriter;

import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This sets GwtConfiguration's Configuration value depending on the .gwt.xml's 
 * kirin.config property.
 * @author douglashoskins
 *
 */
public class ConfigurationGenerator extends Generator {
    private static final String KIRIN_CONFIG_PROPERTY = "kirin.config";
    @Override
    public String generate(TreeLogger logger, GeneratorContext context,
            String typeName) throws UnableToCompleteException {
        PropertyOracle propOracle = context.getPropertyOracle();
        Configuration p;
        try {
            ConfigurationProperty configProp = propOracle.getConfigurationProperty(KIRIN_CONFIG_PROPERTY);
            String profilePropStr = configProp.getValues().get(0);
            if ("release".equalsIgnoreCase(profilePropStr)) {
                p = Configuration.Release;
            } else if ("debug".equalsIgnoreCase(profilePropStr)) {
                p = Configuration.Debug;
            } else {
                throw new BadPropertyValueException(KIRIN_CONFIG_PROPERTY + " must be either debug or release (it's " + profilePropStr + ")");
            }
        } catch (BadPropertyValueException e) {
            p = Configuration.Debug;
            logger.log(Type.WARN, "Missing/invalid value for " + KIRIN_CONFIG_PROPERTY + ", defaulting to debug", e);
        }
        
        TypeOracle typeOracle = context.getTypeOracle();
        JClassType gwtConfigurationType;
        try {
            gwtConfigurationType = typeOracle.getType(typeName);
        } catch (NotFoundException e) {
            logger.log(TreeLogger.ERROR, "Unable to find metadata for type: " + typeName, e);
            throw new UnableToCompleteException();
        }
        
        String packageName = gwtConfigurationType.getPackage().getName();
        String className = gwtConfigurationType.getName() + "Impl";
        
        ClassSourceFileComposerFactory compFactory = 
                new ClassSourceFileComposerFactory(
                    packageName,
                    className);
        
        compFactory.addImplementedInterface(gwtConfigurationType.getQualifiedSourceName());
        compFactory.addImport(Configuration.class.getCanonicalName());
        PrintWriter pw = context.tryCreate(logger, packageName, className);
        if (pw != null) {
            SourceWriter sw = compFactory.createSourceWriter(context, pw);
            sw.println();
            sw.println();
            sw.println("public Configuration getConfiguration() {");
            sw.indent();
            if (p == Configuration.Debug) {
                sw.println("return Configuration.Debug;");
            } else {
                sw.println("return Configuration.Release;");
            }
            sw.outdent();
            sw.println("}");
            sw.commit(logger);
        }
        return compFactory.getCreatedClassName();
    }

}
