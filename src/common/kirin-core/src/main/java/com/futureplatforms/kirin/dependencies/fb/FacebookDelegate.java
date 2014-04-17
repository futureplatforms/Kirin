package com.futureplatforms.kirin.dependencies.fb;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookRequestsCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;

public interface FacebookDelegate {
	public void signOut(AsyncCallback cb);
	public void isLoggedIn(AsyncCallback1<Boolean> cb);
	public void getAccessToken(AsyncCallback2<String, String> cb);
	public void getAppId(AsyncCallback1<String> cb);
	public void getCurrentPermissions(AsyncCallback1<String[]> cb);
	public void presentShareDialogWithParams(ShareDialogParams params, FacebookShareCallback cb);
	public void presentRequestsDialog(FacebookRequestsCallback cb);
	
	/**
	 * If needed, this creates an active session and requests basic permissions.
	 * 
	 * @param permissions The basic profile permissions required.
	 */ 
	public void nativeOpenSessionWithReadPermissions(FacebookLoginCallback callback, boolean allowUI, ReadPermission ... permissions);
	
	/**
	 * If needed, this authorises the app with some publish permissions.
	 * 
	 * @param permissions The publish permissions required.
	 */ 
	public void nativeRequestPublishPermissions(FacebookLoginCallback callback, PublishPermission ... permissions);

}
