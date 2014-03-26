package com.futureplatforms.kirin.dependencies.fb;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;

public interface FileUploadDelegate {
	public void uploadFile(String url, Map<String, String> params, String filename, String token, AsyncCallback1<String> cb);
}
