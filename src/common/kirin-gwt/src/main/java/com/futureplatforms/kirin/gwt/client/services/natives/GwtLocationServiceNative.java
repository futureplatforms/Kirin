package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface GwtLocationServiceNative extends IKirinNativeService {
	public void hasPermission(int cbId);
	public void startUpdating(int accuracy);
	public void stopUpdating();
}
