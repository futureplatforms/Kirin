package com.futureplatforms.kirin.dependencies.internal;

import com.futureplatforms.kirin.controllers.TimerTask.TimerDelegate;
import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate;

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
	
	private DatabaseDelegate _DatabaseBackend;
	public DatabaseDelegate getDatabaseAccessor(){
		return _DatabaseBackend;
	}
	
	public void setDependencies(TimerDelegate timerDelegate, DatabaseDelegate databaseBackend) {
		this._TimerDelegate = timerDelegate;
		this._DatabaseBackend = databaseBackend;
	}
}
