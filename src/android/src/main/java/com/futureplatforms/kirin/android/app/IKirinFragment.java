package com.futureplatforms.kirin.android.app;

import com.futureplatforms.kirin.KirinModule;

public interface IKirinFragment<Module extends KirinModule<?>> {
	public Module getModule();
	public void setModule(Module module);
}
