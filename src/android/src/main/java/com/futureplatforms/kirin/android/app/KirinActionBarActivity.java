package com.futureplatforms.kirin.android.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.futureplatforms.kirin.IKirinNativeObject;
import com.futureplatforms.kirin.KirinModule;

abstract public class KirinActionBarActivity<Module extends KirinModule<KirinNativeObj>, KirinNativeObj extends IKirinNativeObject>
		extends ActionBarActivity implements IKirinNativeObject, IKirinModuleHost<Module> {
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
