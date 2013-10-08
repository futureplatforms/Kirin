package com.futureplatforms.kirin.android.app;

import java.lang.reflect.ParameterizedType;

import com.futureplatforms.kirin.IKirinNativeObject;
import com.futureplatforms.kirin.KirinModule;

public class KirinFragmentMethods {

	@SuppressWarnings("unchecked")
	public static <Module extends KirinModule<KirinNativeObj>, KirinNativeObj extends IKirinNativeObject> void onCreate(
			IKirinFragment<Module> f) {
		Module module = null;
		try {
			module = (Module) ((Class<?>) ((ParameterizedType) f.getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0])
					.newInstance();
			module.onPrototypeLoad((KirinNativeObj) f);
			f.setModule(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <Module extends KirinModule<KirinNativeObj>, KirinNativeObj extends IKirinNativeObject> void onUnload(
			IKirinFragment<Module> f) {
		f.getModule().onUnload();
	}

}
