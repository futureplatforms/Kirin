package com.futureplatforms.kirin.console;


import com.futureplatforms.kirin.console.json.ConsoleJson;
import com.futureplatforms.kirin.console.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.futureplatforms.kirin.dependencies.internal.InternalDependencies;

public final class Kirin {
    public static void kickOff() {
        
        StaticDependencies.getInstance().setDependencies(
                new ConsoleLog(), 
                new ConsoleSettings(), 
                new ConsoleLocation(), 
                new ConsoleNetwork(), 
                new ConsoleJson(),
                new JaxpXmlParser(),
                new ConsoleFormatter(),
                Configuration.Debug);
        
        InternalDependencies.getInstance().setDependencies(new ConsoleTimer(), null);
    }
}
