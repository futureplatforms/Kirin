package com.futureplatforms.kirin.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.futureplatforms.kirin.android.json.AndroidJson;
import com.futureplatforms.kirin.android.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;

public final class Kirin {
    public static void kickOff(Context context) {
        boolean isDebug =  0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE );
        
        StaticDependencies.getInstance().setDependencies(
                new AndroidLog(), 
                new AndroidSettings(context), 
                new AndroidTimer(), 
                new AndroidLocation(context), 
                new AndroidNetwork(), 
                new AndroidJson(),
                new JaxpXmlParser(),
                new AndroidFormatter(),
                isDebug ? Configuration.Debug : Configuration.Release);
    }
}
