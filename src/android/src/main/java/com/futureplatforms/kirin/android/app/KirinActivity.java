package com.futureplatforms.kirin.android.app;

import java.lang.reflect.ParameterizedType;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.futureplatforms.kirin.gwt.client.modules.IKirinNativeObject;
import com.futureplatforms.kirin.gwt.client.modules.KirinModule;

abstract public class KirinActivity<Module extends KirinModule<NativeObject>, NativeObject extends IKirinNativeObject>
		extends FragmentActivity implements IKirinNativeObject {
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

		module.onPrototypeLoad((NativeObject) this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
