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

	@Export
	public final void onLoad(JavaScriptObject moduleName) {
		if (mNativeObject instanceof IKirinProxied) {
			((IKirinProxied) mNativeObject).$setKirinNativeObject(moduleName);
		}
        _onLoad();
	}

    @NoExport
    public final void onPrototypeLoad(T nativeObject) {
        mNativeObject = nativeObject;
        _onLoad();
    }

    protected void _onLoad() {}

    @Export
	public void onUnload() {
		if (mNativeObject instanceof IKirinProxied) {
			((IKirinProxied) mNativeObject).$setKirinNativeObject(null);
		}
	}

    protected T getNativeObject() {
        return mNativeObject;
    }
	
}
