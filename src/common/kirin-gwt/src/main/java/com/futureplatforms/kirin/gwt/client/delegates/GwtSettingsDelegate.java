package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;

public class GwtSettingsDelegate implements SettingsDelegate {
    public native String get(String key) /*-{
        // This is required for the junit tests which won't have access to the module
        if (!$wnd['require']) {
            return null;
        }
    
        var settings = $wnd.require("Settings");
        return settings.get(key);
    }-*/;
    
    public native void put(String key, String value) /*-{
        // This is required for the junit tests which won't have access to the module
        if (!$wnd['require']) {
            return;
        }
        
        var settings = $wnd.require("Settings");
        if (value == null) {
            settings.remove(key);
        } else {
            settings.put(key, value);
        }
        settings.commit();
    }-*/;
    
    public native void clear() /*-{
    	if (!$wnd['require']) {
            return;
        }
        
        var settings = $wnd.require("Settings");
        settings.clear();
    }-*/;
}
