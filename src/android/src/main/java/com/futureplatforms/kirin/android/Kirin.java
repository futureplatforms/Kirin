package com.futureplatforms.kirin.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.futureplatforms.kirin.android.db.AndroidDatabase;
import com.futureplatforms.kirin.android.json.AndroidJson;
import com.futureplatforms.kirin.android.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;

public final class Kirin {
    public static void kickOff(Context context) {
       kickOff(context, true);
    }
    public static void kickOff(Context context, boolean includeLocation) {
        boolean isDebug =  0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE );
        
        StaticDependencies.getInstance().setDependencies(
                new AndroidLog(), 
                new AndroidSettings(context), 
                includeLocation ? new AndroidLocation(context) : null, 
                new AndroidNetwork(), 
                new AndroidJson(),
                new JaxpXmlParser(),
                new AndroidFormatter(),
                isDebug ? Configuration.Debug : Configuration.Release,
                new AndroidDatabase(context),
                new AndroidTimer(),
                new AndroidNotification(context),
                new FacebookDelegateImpl(context));
    }
}
