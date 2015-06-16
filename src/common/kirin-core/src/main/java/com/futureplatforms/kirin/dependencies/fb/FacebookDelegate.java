package com.futureplatforms.kirin.dependencies.fb;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookRequestsCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;
import com.futureplatforms.kirin.dependencies.fb.friends.FacebookFriends;

public abstract class FacebookDelegate {
	
	public void signOut(final AsyncCallback cb) {
		_SignOut(new AsyncCallback() {
			
			@Override
			public void onSuccess() {
				FacebookFriends.reset(cb);
				cb.onSuccess();
			}
			
			@Override
			public void onFailure() {
				FacebookFriends.reset(cb);
				cb.onFailure();
			}
		});
	}
	public abstract void _SignOut(AsyncCallback cb);
	public abstract void isLoggedIn(AsyncCallback1<Boolean> cb);
	public abstract void getAccessToken(AsyncCallback2<String, String> cb);
	public abstract void getAppId(AsyncCallback1<String> cb);
	public abstract void getCurrentPermissions(AsyncCallback1<String[]> cb);
	public abstract void presentShareDialogWithParams(ShareDialogParams params, FacebookShareCallback cb);
	public abstract void presentRequestsDialog(FacebookRequestsCallback cb);
	
	/**
	 * If needed, this creates an active session and requests basic permissions.
	 * 
	 * @param permissions The basic profile permissions required.
	 */ 
	public abstract void nativeOpenSessionWithReadPermissions(FacebookLoginCallback callback, boolean allowUI, ReadPermission ... permissions);
	
	/**
	 * If needed, this authorises the app with some publish permissions.
	 * 
	 * @param permissions The publish permissions required.
	 */ 
	public abstract void nativeRequestPublishPermissions(FacebookLoginCallback callback, PublishPermission ... permissions);

	
	public abstract void logEvent(String eventName, Map<String, String> parameters);
}
