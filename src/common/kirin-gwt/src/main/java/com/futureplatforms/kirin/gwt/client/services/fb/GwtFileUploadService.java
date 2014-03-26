package com.futureplatforms.kirin.gwt.client.services.fb;

import java.util.Map;
import java.util.Map.Entry;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.fb.GwtFileUploadServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.shared.GWT;

@Export(value = "GwtFileUploadService", all = true)
@ExportPackage("screens")
public class GwtFileUploadService extends KirinService<GwtFileUploadServiceNative> {
	private static GwtFileUploadService _Instance;
    
    @NoBind
    @NoExport
    public static GwtFileUploadService BACKDOOR() { return _Instance; }
    
	public GwtFileUploadService() {
		super(GWT.<GwtFileUploadServiceNative>create(GwtFileUploadServiceNative.class));
		_Instance = this;
	}
	
    private int _UploadId = Integer.MIN_VALUE;
    private Map<Integer, AsyncCallback1<String>> _UploadCallbacks = Maps.newHashMap();
    

	@NoExport
	public void _uploadFile(String url, Map<String, String> params, String filename, String token,
			AsyncCallback1<String> cb) {
		int id = _UploadId;
		_UploadId++;
		_UploadCallbacks.put(id, cb);
		String[] paramKeys = new String[params.size()];
		String[] paramVals = new String[params.size()];
		params.entrySet();
		int i=0;
		for (Entry<String, String> entry : params.entrySet()) {
			paramKeys[i] = entry.getKey();
			paramVals[i] = entry.getValue();
			i++;
		}
		getNativeObject().uploadFile(url, paramKeys, paramVals, filename, token, id);
	}
	
	public void fileUploadedOK(String url, int id) {
		_UploadCallbacks.remove(id).onSuccess(url);
	}
	
	public void fileUploadError(int id) {
		_UploadCallbacks.remove(id).onFailure();
	}
}
