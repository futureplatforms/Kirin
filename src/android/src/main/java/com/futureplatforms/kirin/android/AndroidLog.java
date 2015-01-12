package com.futureplatforms.kirin.android;

import android.util.Log;

import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;

public class AndroidLog implements LogDelegate {


	private CrashLog crashLog;

	public interface CrashLog {
		public void log(String tag, String s,Throwable t); 
	}


	public AndroidLog(CrashLog crashLog) {
		this.crashLog = crashLog;
	}

	@Override
	public void log(String s) {
		Log.i("Kirin", "" + s);
	}

	@Override
	public void log(String tag, String s) {
		Log.i("" +tag, "" + s);
	}

	@Override
	public void log(String tag, String s, Throwable t) {
		Log.i("" + tag, "" + s, t);
		if(crashLog != null) crashLog.log(tag, s, t);
	}

}
