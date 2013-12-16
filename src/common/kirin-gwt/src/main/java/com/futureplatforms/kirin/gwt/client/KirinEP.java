package com.futureplatforms.kirin.gwt.client;

import java.util.Map;
import java.util.Set;

import org.timepedia.exporter.client.ExporterUtil;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.internal.InternalDependencies;
import com.futureplatforms.kirin.gwt.client.delegates.GwtFormatter;
import com.futureplatforms.kirin.gwt.client.delegates.GwtSettingsDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.GwtTimerDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.KirinLocation;
import com.futureplatforms.kirin.gwt.client.delegates.KirinNetworking;
import com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabaseDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.json.GwtJSON;
import com.futureplatforms.kirin.gwt.client.delegates.xml.GwtXMLParserImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class KirinEP implements EntryPoint {
    public static JavaScriptObject createMap(Map<String, String> map) {
        JavaScriptObject jsMap = JavaScriptObject.createObject();
        if (map != null) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                addMapping(jsMap, key, map.get(key));
            }
        }
        return jsMap;
    }
    
    private static native void addMapping(JavaScriptObject map, String key, String value) /*-{
        map[key] = value;
    }-*/;
    
    private final LogDelegate realLogDelegate = new LogDelegate() {
        @Override
        public native void log(String s) /*-{
            if ($wnd['console']) {
                $wnd.console.log(s);
            }
        }-*/;
    };
    
    private final LogDelegate noopLogDelegate = new LogDelegate() {
        
        @Override
        public void log(String s) { }
    };
    
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
        GwtConfiguration gp = GWT.create(GwtConfiguration.class);
        final Configuration profile = gp.getConfiguration();
        _execute();
        final LogDelegate ld = profile == Configuration.Debug ? realLogDelegate : noopLogDelegate;
        
        StaticDependencies.getInstance().setDependencies(
                ld, 
                new GwtSettingsDelegate(), 
                new KirinLocation(), 
                new KirinNetworking(), 
                new GwtJSON(), 
                new GwtXMLParserImpl(),
                new GwtFormatter(),
                profile,
                new GwtDatabaseDelegate());
        
        InternalDependencies.getInstance().setDependencies(new GwtTimerDelegate());
        
        ExporterUtil.exportAll();
        
        ld.log(GWT.getPermutationStrongName());
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            
            @Override
            public void onUncaughtException(Throwable e) {
                StackTraceElement[] stes = e.getStackTrace();
                for (StackTraceElement ste : stes) {
                    ld.log(ste.toString());
                }
            }
        });
  }
    private static native void _execute() /*-{
        $wnd.kirinKickOff();
    }-*/;
  
}
