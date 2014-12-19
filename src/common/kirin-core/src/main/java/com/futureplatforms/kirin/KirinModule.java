package com.futureplatforms.kirin;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class KirinModule<T extends IKirinNativeObject> implements Exportable {

	protected T mNativeObject;
	
	private KirinModule() {}
	
	@NoExport
	public KirinModule(T nativeObject) {
		this();
		mNativeObject = nativeObject;
	}
	
	@NoExport
	public final void __AndroidOnLoad(T nativeObject) {
		mNativeObject = nativeObject;
	}
	
	@Export
	public final void __KirinOnLoad(JavaScriptObject kirinObject) {
		if (mNativeObject instanceof IKirinProxied) {
			((IKirinProxied) mNativeObject).$setKirinNativeObject(kirinObject);
		}
	}
	
	@Export
	public void __KirinOnUnload() {
		if (mNativeObject instanceof IKirinProxied) {
			((IKirinProxied) mNativeObject).$setKirinNativeObject(null);
		}
	}

    protected T getNativeObject() {
        return mNativeObject;
    }
	
}
