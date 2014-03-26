package com.futureplatforms.kirin.gwt.client.delegates.fb;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.fb.FileUploadDelegate;
import com.futureplatforms.kirin.gwt.client.services.fb.GwtFileUploadService;

public class GwtFileUpload implements FileUploadDelegate {

	@Override
	public void uploadFile(String url, Map<String, String> params, String filename, String token,
			AsyncCallback1<String> cb) {
		GwtFileUploadService.BACKDOOR()._uploadFile(url, params, filename, token, cb);
	}

}
