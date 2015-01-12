package com.futureplatforms.kirin.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.futureplatforms.kirin.android.AndroidLog.CrashLog;
import com.futureplatforms.kirin.android.db.AndroidDatabase;
import com.futureplatforms.kirin.android.json.AndroidJson;
import com.futureplatforms.kirin.android.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;

public final class Kirin {
    public static void kickOff(Context context) {
       kickOff(context, true, null);
    }

    public static void kickOff(Context context, boolean includeLocation) {
        kickOff(context, includeLocation, null);
    }
    
    public static void kickOff(Context context, boolean includeLocation, final CrashLog crashLog) {
        boolean isDebug =  0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE );

        StaticDependencies.getInstance().setDependencies(
                isDebug ? new AndroidLog(crashLog) : new LogDelegate() {

					@Override
					public void log(String tag, String s, Throwable t) {
						if(crashLog != null) crashLog.log(tag,s, t);
					}

					@Override
					public void log(String tag, String s) { }

					@Override
					public void log(String s) { }
				},
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
