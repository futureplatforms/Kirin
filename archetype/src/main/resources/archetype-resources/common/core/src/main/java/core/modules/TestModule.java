#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.modules;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.gwt.client.modules.KirinModule;
import ${package}.core.modules.natives.TestModuleNative;
import com.google.gwt.core.client.GWT;

@Export(value = "TestModule", all = true)
@ExportPackage("screens") 
public class TestModule extends KirinModule<TestModuleNative> {

    public TestModule() { super((TestModuleNative) GWT.create(TestModuleNative.class)); }

    public void testyMethod(String str, int i) { 
        getNativeObject().testyNativeMethod("Hello from kirin!! " + str + ", " + i);
        final StaticDependencies deps = StaticDependencies.getInstance();
        final LogDelegate ld = deps.getLogDelegate();
        deps.getLogDelegate().log("Logging via the logging delegate");
        
        NetworkDelegate net = deps.getNetworkDelegate();
        net.doHttp(HttpVerb.GET, "http://www.google.co.uk", new NetworkResponse() {
            
            public void onSuccess(int res, String result, Map<String, String> headers) {
                ld.log("Network SUCCESS :: " + result);
            }
            
            public void onFail(String code) {
                ld.log("Network FAIL :: " + code);
            }
        });
    }
}
