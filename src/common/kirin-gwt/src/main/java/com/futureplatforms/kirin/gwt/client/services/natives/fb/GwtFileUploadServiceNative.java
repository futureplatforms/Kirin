package com.futureplatforms.kirin.gwt.client.services.natives.fb;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface GwtFileUploadServiceNative extends IKirinNativeService {
	public void uploadFile(String url, String[] paramKeys, String[] paramVals, String filename, String token, int cbId);
}
