package com.futureplatforms.kirin.android;

import android.util.Log;

import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;

public class AndroidLog implements LogDelegate {

    @Override
    public void log(String s) {
        Log.i("Kirin", ""+s);    
    }

	@Override
	public void log(String tag, String s) {
		Log.i(tag, ""+s);
	}

}
