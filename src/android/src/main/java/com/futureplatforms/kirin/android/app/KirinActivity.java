package com.futureplatforms.kirin.android.app;

import android.app.Activity;
import android.os.Bundle;

import com.futureplatforms.kirin.IKirinNativeObject;
import com.futureplatforms.kirin.KirinModule;

abstract public class KirinActivity<Module extends KirinModule<KirinNativeObj>, KirinNativeObj extends IKirinNativeObject>
		extends Activity implements IKirinNativeObject, IKirinModuleHost<Module> {
	private Module module;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KirinAndroidMethods.onCreate(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
