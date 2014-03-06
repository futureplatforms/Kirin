package com.futureplatforms.kirin.gwt.client;

import java.util.Map;
import java.util.Set;

import org.timepedia.exporter.client.ExporterUtil;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.TimerTask;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.GwtFormatter;
import com.futureplatforms.kirin.gwt.client.delegates.GwtSettingsDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.GwtTimerDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.KirinLocation;
import com.futureplatforms.kirin.gwt.client.delegates.KirinNetworking;
import com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabaseDelegate;
import com.futureplatforms.kirin.gwt.client.delegates.json.GwtJSON;
import com.futureplatforms.kirin.gwt.client.delegates.xml.GwtXMLParserImpl;
import com.futureplatforms.kirin.gwt.client.services.SymbolMapService;
import com.futureplatforms.kirin.gwt.client.services.SymbolMapService.MappedJavaMethod;
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

		@Override
		public void log(String tag, String s) {
			log(tag+": "+s);
		}
    };
    
    private final LogDelegate noopLogDelegate = new LogDelegate() {
        
        @Override
        public void log(String s) { }

		@Override
		public void log(String tag, String s) { }
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
                new GwtDatabaseDelegate(),
                new GwtTimerDelegate());
        
        ExporterUtil.exportAll();
        
        if (profile == Configuration.Debug) {
	        ld.log(GWT.getPermutationStrongName());
	        
	        // Set this in a moment, to give time for the native SymbolMapService to bind on
	        // TODO fix the timings of all this, currently exceptions thrown in the first screen
	        // cannot use the symbol map service.
	        new TimerTask() {
				@Override
				public void run() {
					SymbolMapService.BACKDOOR()._setStrongName(GWT.getPermutationStrongName());
				}
			}.schedule(1);;
        }
        
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            private static final String UNKNOWN_DOT = "Unknown.";
            private static final String UNKNOWN_SOURCE = "(Unknown Source)";
            @Override
            public void onUncaughtException(Throwable e) {
                StackTraceElement[] stes = e.getStackTrace();
                ld.log("==== UNCAUGHT EXCEPTION ON KIRIN ====");
                for (StackTraceElement ste : stes) {
                	String exc = ste.toString();
                	SymbolMapService sms = SymbolMapService.BACKDOOR();
                	if (sms != null) {
	                	if (exc.startsWith(UNKNOWN_DOT)) {
	                		if (exc.endsWith(UNKNOWN_SOURCE)) {
	                			String symbol = exc.substring(UNKNOWN_DOT.length(), exc.length() - UNKNOWN_SOURCE.length());
	                			if (sms._SymbolMap != null && sms._SymbolMap.containsKey(symbol)) {
		                			MappedJavaMethod method = sms._SymbolMap.get(symbol);
		                			ld.log(method._ClassName + "::" + method._MemberName);
		                			continue;
	                			}
	                		}
	                	}
                	}
                    ld.log(ste.toString());
                }
            }
        });
  }
    private static native void _execute() /*-{
        $wnd.kirinKickOff();
    }-*/;
  
}
