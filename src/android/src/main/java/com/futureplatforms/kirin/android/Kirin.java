package com.futureplatforms.kirin.android;

import android.content.Context;

import com.futureplatforms.kirin.android.json.AndroidJson;
import com.futureplatforms.kirin.android.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.StaticDependencies;

public final class Kirin {
    public static void kickOff(Context context) {
        StaticDependencies.getInstance().setDependencies(
                new AndroidLog(), 
                new AndroidSettings(context), 
                new AndroidTimer(), 
                new AndroidLocation(context), 
                new AndroidNetwork(), 
                new AndroidJson(),
                new JaxpXmlParser(),
                null);
    }
}
