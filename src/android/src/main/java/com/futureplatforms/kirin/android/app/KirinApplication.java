package com.futureplatforms.kirin.android.app;

import android.app.Application;

import com.futureplatforms.kirin.IKirinNativeObject;
import com.futureplatforms.kirin.KirinModule;

abstract public class KirinApplication<Module extends KirinModule<KirinNativeObj>, KirinNativeObj extends IKirinNativeObject>
		extends Application implements IKirinNativeObject, IKirinModuleHost<Module> {
	private Module module;

	@Override
	public void onCreate() {
		super.onCreate();
		KirinAndroidMethods.onCreate(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		KirinAndroidMethods.onUnload(this);
	}

	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public void setModule(Module module) {
		this.module = module;
	}

}
