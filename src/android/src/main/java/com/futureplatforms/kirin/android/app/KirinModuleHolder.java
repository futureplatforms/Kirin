package com.futureplatforms.kirin.android.app;

import com.futureplatforms.kirin.IKirinNativeObject;
import com.futureplatforms.kirin.KirinModule;

public abstract class KirinModuleHolder<Module extends KirinModule<Native>, Native extends IKirinNativeObject>
		implements IKirinNativeObject, IKirinModuleHost<Module> {
	private Module module;
	private Native listener;

	protected KirinModuleHolder() {
		KirinAndroidMethods.onCreate(this);
		
	}

	public void onUnload() {
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

	public void setListener(Native listener) {
		this.listener = listener;
	}

}
