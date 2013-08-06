package com.futureplatforms.kirin.android.app;

import java.lang.reflect.ParameterizedType;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.futureplatforms.kirin.gwt.client.modules.IKirinNativeObject;
import com.futureplatforms.kirin.gwt.client.modules.KirinModule;

abstract public class KirinListFragment<Module extends KirinModule<NativeObject>, NativeObject extends IKirinNativeObject>
		extends ListFragment implements IKirinNativeObject {
	private Module module;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Instantiate the module
		try {
			module = (Module) ((Class<?>) ((ParameterizedType) this.getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0])
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		module.onPrototypeLoad((NativeObject) this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		module.onUnload();
	}

	public Module getModule() {
		return module;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

}
