package com.futureplatforms.kirin.console;

import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;

public class ConsoleLog implements LogDelegate {

	@Override
	public void log(String s) {
		System.out.println(s);
	}

	@Override
	public void log(String tag, String s) {
		log(tag + ": " + s);
	}

	@Override
	public void log(String tag, String s, Throwable t) {
		log(tag + ": " + s);
		t.printStackTrace(System.out);
	}

}
