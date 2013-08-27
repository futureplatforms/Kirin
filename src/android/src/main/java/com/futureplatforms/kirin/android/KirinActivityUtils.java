package com.futureplatforms.kirin.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.futureplatforms.kirin.android.app.IKirinFragment;
import com.futureplatforms.kirin.gwt.client.modules.KirinModule;

public class KirinActivityUtils{

	public static String loadBackgroundModule(FragmentActivity activity,
			IKirinFragment<?> fragment) {
		String tag = fragment.getClass().getSimpleName();
		activity.getSupportFragmentManager().beginTransaction()
				.add((Fragment) fragment, tag).commit();
		return tag;
	}

	public static <Module extends KirinModule<?>> Module getBackgroundModule(FragmentActivity activity, String tag)
			throws KirinFragmentException {
		Fragment f = activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (f instanceof IKirinFragment<?>) {
			return (Module) ((IKirinFragment) f).getModule();
		} else
			throw new KirinFragmentException("Fragment is not a Kirin Fragment");

	}

	public static class KirinFragmentException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public KirinFragmentException(String message) {
			super(message);
		}

	}
}
