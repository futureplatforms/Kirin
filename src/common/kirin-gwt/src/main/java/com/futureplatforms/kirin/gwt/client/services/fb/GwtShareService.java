package com.futureplatforms.kirin.gwt.client.services.fb;

import java.util.List;
import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate.Platform;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate.ShareResponse;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate.ShareSheetResponse;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.fb.GwtShareServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "GwtShareService", all = true)
@ExportPackage("screens")
public class GwtShareService extends KirinService<GwtShareServiceNative> {
	private static GwtShareService _Instance;
    
    @NoBind
    @NoExport
    public static GwtShareService BACKDOOR() { return _Instance; }
    
	public GwtShareService() {
		super(GWT.<GwtShareServiceNative>create(GwtShareServiceNative.class));
		_Instance = this;
	}

	private ShareSheetResponse _ShareSheetResp;
	private List<Platform> _ShareSheetPlatforms;
	
	private int _NextId = Integer.MIN_VALUE;
    private Map<Integer, ShareResponse> _CbMap = Maps.newHashMap();
    private AsyncCallback1<Platform[]> _PlatformCb;
    
	@NoBind
	@NoExport
	public void _showShareSheet(ShareSheetResponse resp, List<Platform> platforms) {
		_ShareSheetResp = resp;
		_ShareSheetPlatforms = platforms;
		String[] strs = new String[platforms.size()];
		for (int i=0; i<platforms.size(); i++) {
			strs[i] = platforms.get(i).name();
		}
		getNativeObject().showShareSheet(strs);
	}
	
	@NoBind
	@NoExport
	public void _shareSms(String text, ShareResponse cb) {
		int id = _NextId;
		_NextId++;
		_CbMap.put(id, cb);
		getNativeObject().shareSms(text, id);
	}
	
	@NoBind
	@NoExport
	public void _shareEmail(String subject, String text, ShareResponse cb) {
		int id = _NextId;
		_NextId++;
		_CbMap.put(id, cb);
		getNativeObject().shareEmail(subject, text, id);
	}
	
	@NoBind
	@NoExport
	public void _shareTwitter(String text, String imgToken, String link, ShareResponse cb) {
		int id = _NextId;
		_NextId++;
		_CbMap.put(id, cb);
		getNativeObject().shareTwitter(text, imgToken, link, id);
	}
	
	@NoBind
	@NoExport
	public void _getSupportedPlatforms(AsyncCallback1<Platform[]> cb) {
		_PlatformCb = cb;
		getNativeObject().getSupportedPlatforms();
	}
	
	public void shareSheetResponse(int index) {
		_ShareSheetResp.onOK(_ShareSheetPlatforms.get(index));
	}
	
	public void shareSheetCancel() {
		_ShareSheetResp.onCancel();
	}
	
	public void shareResponseWin(int platform, int cbId) {
		_CbMap.remove(cbId).onOK(Platform.values()[platform]);
	}
	
	public void shareResponseFail(int cbId) {
		_CbMap.remove(cbId).onFailure();
	}
	
	public void supportedPlatformsResponse(int[] platforms) {
		Platform[] ps = new Platform[platforms.length];
		for (int i=0; i<platforms.length; i++) {
			ps[i] = Platform.values()[platforms[i]];
		}
		_PlatformCb.onSuccess(ps);
	}
}
