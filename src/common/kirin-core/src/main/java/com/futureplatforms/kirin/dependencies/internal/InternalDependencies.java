package com.futureplatforms.kirin.dependencies.internal;

import com.futureplatforms.kirin.controllers.TimerTask.TimerDelegate;

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
	
	public void setDependencies(TimerDelegate timerDelegate) {
		this._TimerDelegate = timerDelegate;
	}
}
