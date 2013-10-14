package com.futureplatforms.kirin.dependencies.internal;

import com.futureplatforms.kirin.dependencies.internal.TimerTask.TimerDelegate;

// Apps should not use this!
public class InternalDependencies {
	private InternalDependencies() {}
	private static InternalDependencies instance = new InternalDependencies();
	public static InternalDependencies getInstance() {
		return instance;
	}
	private TimerDelegate _TimerDelegate;
	public TimerDelegate getTimerDelegate() {
		return _TimerDelegate;
	}
	
	private DatabaseBackend _DatabaseBackend;
	public DatabaseBackend getDatabaseBackend(){
		return _DatabaseBackend;
	}
	
	public void setDependencies(TimerDelegate timerDelegate, DatabaseBackend databaseBackend) {
		this._TimerDelegate = timerDelegate;
		this._DatabaseBackend = databaseBackend;
	}
}
