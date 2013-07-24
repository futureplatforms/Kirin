package com.futureplatforms.kirinhello.modules;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.gwt.client.modules.KirinModule;
import com.futureplatforms.kirinhello.modules.natives.TestModuleNative;
import com.google.gwt.core.client.GWT;

@Export(value = "TestModule", all = true)
@ExportPackage("screens") 
public class TestModule extends KirinModule<TestModuleNative> {

    public TestModule() { super((TestModuleNative) GWT.create(TestModuleNative.class)); }

    public void testyMethod(String str, int i) { 
        getNativeObject().testyNativeMethod("Hello from kirin!! " + str + ", " + i);
        getNativeObject().testyNativeMethod2("Hello again");
        final StaticDependencies deps = StaticDependencies.getInstance();
        deps.getLogDelegate().log("Logging via the logging delegate");
        
        NetworkDelegate net = deps.getNetworkDelegate();
        net.doHttp(HttpVerb.GET, "http://proxocube-devtest.appspot.com/proxo/dev_data/lineup/0", new NetworkResponse() {
            
            public void onSuccess(int res, String result, Map<String, String> headers) {
                LogDelegate log = deps.getLogDelegate();
                log.log("Network SUCCESS :: " + result);
                JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();
                JSONArray arr = json.getJSONArray(result);
                log.log("found array with " + arr.length() + " elements");
            }
            
            public void onFail(String code) {
                deps.getLogDelegate().log("Network FAIL :: " + code);
            }
        });
    }
}
