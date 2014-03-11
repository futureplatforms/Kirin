package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface SymbolMapServiceNative extends IKirinNativeService {
	public void setSymbolMapDetails(String moduleName, String strongName);
}
