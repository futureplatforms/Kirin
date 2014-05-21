package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookRequestsCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.GwtFacebookServiceNative;
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
    
    // share callbacks
    private int _ShareId = Integer.MIN_VALUE;
    private Map<Integer, FacebookShareCallback> _ShareCallbacks = Maps.newHashMap();
    
    // requests callbacks
    private int _RequestId = Integer.MIN_VALUE;
    private Map<Integer, FacebookRequestsCallback> _RequestCallbacks = Maps.newHashMap();
    
    // accesstoken callbacks
    private int _AccessTokenId = Integer.MIN_VALUE;
    private Map<Integer, AsyncCallback2<String, String>> _AccessTokenCallbacks = Maps.newHashMap();
    
    // logged-in callbacks
    private int _IsLoggedInId = Integer.MIN_VALUE;
    private Map<Integer, AsyncCallback1<Boolean>> _IsLoggedInCallbacks = Maps.newHashMap();
    
    // appid callbacks
    private int _AppIdId = Integer.MIN_VALUE;
    private Map<Integer, AsyncCallback1<String>> _AppIdCallbacks = Maps.newHashMap();
    
    // permissions callbacks
    private int _CurrentPermissionsId = Integer.MIN_VALUE;
    private Map<Integer, AsyncCallback1<String[]>> _CurrentPermissionsCallbacks = Maps.newHashMap();
    
    @NoExport
    public void _getAccessToken(AsyncCallback2<String, String> cb) {
    	int id = _AccessTokenId;
    	_AccessTokenId++;
    	_AccessTokenCallbacks.put(id, cb);
    	getNativeObject().getAccessToken(id);
    }
    
    @NoExport
    public void _isLoggedIn(AsyncCallback1<Boolean> cb) {
    	int id = _IsLoggedInId;
    	_IsLoggedInId++;
    	_IsLoggedInCallbacks.put(id, cb);
    	getNativeObject().isLoggedIn(id);
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
		int id = _AppIdId;
		_AppIdId++;
		_AppIdCallbacks.put(id, cb);
		getNativeObject().getAppId(id);
	}
	
	@NoExport
	public void _signOut() {
		getNativeObject().signOut();
	}
	
	@NoExport
	public void _getCurrentPermissions(AsyncCallback1<String[]> cb) {
		int id = _CurrentPermissionsId;
		_CurrentPermissionsId++;
		_CurrentPermissionsCallbacks.put(id, cb);
		getNativeObject().getCurrentPermissions(id);
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
				params._Friends.toArray(new String[0]),
				id);
	}
	
	@NoExport
	public void _presentRequestsDialog(FacebookRequestsCallback cb) {
		int id = _RequestId;
		_RequestId++;
		_RequestCallbacks.put(id, cb);
		getNativeObject().presentRequestsDialog(id);
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
	
	public void setAccessToken(int cbId, String accessToken, String expirationDate) {
		_AccessTokenCallbacks.remove(cbId).onSuccess(accessToken, expirationDate);
	}
	public void setIsLoggedIn(int cbId, boolean isLoggedIn) {
		_IsLoggedInCallbacks.remove(cbId).onSuccess(isLoggedIn);
	}
	public void setAppId(int cbId, String appId) {
		_AppIdCallbacks.remove(cbId).onSuccess(appId);
	}
	
	public void setCurrentPermissions(int cbId, String[] currentPermissions) {
		_CurrentPermissionsCallbacks.remove(cbId).onSuccess(currentPermissions);
	}
	

	public void requestsDialogSuccess(int cbId, String[] tos) {
		_RequestCallbacks.remove(cbId).onSuccess(tos);
	}
	public void requestsDialogCancel(int cbId) {
		_RequestCallbacks.remove(cbId).onUserCancel();
	}
	public void requestsDialogFail(int cbId) {
		_RequestCallbacks.remove(cbId).onFailure();
	}
}
