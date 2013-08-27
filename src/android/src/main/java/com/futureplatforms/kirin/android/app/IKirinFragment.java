package com.futureplatforms.kirin.android.app;

import com.futureplatforms.kirin.gwt.client.modules.KirinModule;

public interface IKirinFragment<Module extends KirinModule<?>> {
	public Module getModule();
}
