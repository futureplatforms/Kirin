package com.futureplatforms.kirin.gwt.client.services.fb;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.fb.GwtFacebookServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "GwtFacebookService", all = true)
@ExportPackage("screens")
public class GwtFacebookService extends KirinService<GwtFacebookServiceNative> {
	public static interface GwtFacebookCallback {
		public void onSuccess();
		public void onError();
		public void onCancel();
		public void onAuthFailed();
	}
	
	private static GwtFacebookService _Instance;
    
    @NoBind
    @NoExport
    public static GwtFacebookService BACKDOOR() { return _Instance; }
    
	public GwtFacebookService() {
		super(GWT.<GwtFacebookServiceNative>create(GwtFacebookServiceNative.class));
		_Instance = this;
	}
	// open with read permission callbacks
    private int _NextOpenSessionWithReadPermissionsId = Integer.MIN_VALUE;
    private Map<Integer, GwtFacebookCallback> _OpenCallbacks = Maps.newHashMap();
    
    // publish permission callbacks
    private int _NextRequestPublishPermissionsId = Integer.MIN_VALUE;
    private Map<Integer, GwtFacebookCallback> _PublishCallbacks = Maps.newHashMap();
    
    // upload image callbacks
    private int _StageImageId = Integer.MIN_VALUE;
    private Map<Integer, AsyncCallback1<String>> _StageImageCallbacks = Maps.newHashMap();
    
    // share callbacks
    private int _ShareId = Integer.MIN_VALUE;
    private Map<Integer, FacebookShareCallback> _ShareCallbacks = Maps.newHashMap();
    
    private AsyncCallback1<String> _AccessTokenCallback;
    private AsyncCallback1<Boolean> _IsLoggedInCallback;
    private AsyncCallback1<String> _AppIdCallback;
    private AsyncCallback1<String[]> _CurrentPermissionsCallback;
    
    @NoExport
    public void _getAccessToken(AsyncCallback1<String> cb) {
    	_AccessTokenCallback = cb;
    	getNativeObject().getAccessToken();
    }
    
    @NoExport
    public void _isLoggedIn(AsyncCallback1<Boolean> cb) {
    	_IsLoggedInCallback = cb;
    	getNativeObject().isLoggedIn();
    }
    
	@NoExport
	public void _openSessionWithReadPermissions(String[] readPermissions, boolean allowUI, GwtFacebookCallback cb) {
		int id = _NextOpenSessionWithReadPermissionsId;
		_NextOpenSessionWithReadPermissionsId++;
		_OpenCallbacks.put(id, cb);
		getNativeObject().openSessionWithReadPermissions(readPermissions, allowUI, id);
	}
	
	@NoExport
	public void _requestPublishPermissions(String[] publishPermissions, GwtFacebookCallback cb) {
		int id = _NextRequestPublishPermissionsId;
		_NextRequestPublishPermissionsId++;
		_PublishCallbacks.put(id, cb);
		getNativeObject().requestPublishPermissions(publishPermissions, id);
	}
	
	@NoExport
	public void _getAppId(AsyncCallback1<String> cb) {
		_AppIdCallback = cb;
		getNativeObject().getAppId();
	}
	
	@NoExport
	public void _stageImage(String message, String image, AsyncCallback1<String> cb) {
		int id = _StageImageId;
		_StageImageId++;
		_StageImageCallbacks.put(id, cb);
		getNativeObject().requestForUploadPhoto(message, image, id);
	}
	
	@NoExport
	public void _signOut() {
		getNativeObject().signOut();
	}
	
	@NoExport
	public void _getCurrentPermissions(AsyncCallback1<String[]> cb) {
		_CurrentPermissionsCallback = cb;
		getNativeObject().getCurrentPermissions();
	}
	
	@NoExport
	public void _presentShareDialogWithParams(ShareDialogParams params, FacebookShareCallback cb) {
		int id = _ShareId;
		_ShareId++;
		_ShareCallbacks.put(id, cb);
		getNativeObject().presentShareDialogWithParams(
				params._Caption, 
				params._Description, 
				params._Link, 
				params._Name, 
				params._Picture, 
				params._Place, 
				params._Ref, 
				id);
	}
	
	public void openSessionSuccess(int fbId) {
		_OpenCallbacks.remove(fbId).onSuccess();
	}
	public void openSessionCancel(int fbId) {
		_OpenCallbacks.remove(fbId).onCancel();
	}
	public void openSessionError(int fbId) {
		_OpenCallbacks.remove(fbId).onError();
	}
	public void openSessionAuthenticationFailed(int fbId) {
		_OpenCallbacks.remove(fbId).onAuthFailed();
	}
	
	public void requestPublishSuccess(int fbId) {
		_PublishCallbacks.remove(fbId).onSuccess();
	}
	public void requestPublishCancel(int fbId) {
		_PublishCallbacks.remove(fbId).onCancel();
	}
	public void requestPublishError(int fbId) {
		_PublishCallbacks.remove(fbId).onError();
	}
	public void requestPublishAuthenticationFailed(int fbId) {
		_PublishCallbacks.remove(fbId).onAuthFailed();
	}
	
	public void shareSuccess(int cbId, String res) {
		_ShareCallbacks.remove(cbId).onSuccess(res);
	}
	public void shareCancel(int cbId) {
		_ShareCallbacks.remove(cbId).onUserCancel();
	}
	public void shareErr(int cbId) {
		_ShareCallbacks.remove(cbId).onFailure();
	}
	
	public void requestForUploadPhotoSuccess(String url, int fbId) {
		_StageImageCallbacks.remove(fbId).onSuccess(url);
	}
	public void requestForUploadPhotoError(int fbId) {
		_StageImageCallbacks.remove(fbId).onFailure();
	}
	
	public void setAccessToken(String accessToken) {
		_AccessTokenCallback.onSuccess(accessToken);
	}
	public void setIsLoggedIn(boolean isLoggedIn) {
		_IsLoggedInCallback.onSuccess(isLoggedIn);
	}
	public void setAppId(String appId) {
		_AppIdCallback.onSuccess(appId);
	}
	
	public void setCurrentPermissions(String[] currentPermissions) {
		_CurrentPermissionsCallback.onSuccess(currentPermissions);
	}
}
