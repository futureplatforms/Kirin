package com.futureplatforms.kirin.gwt.client.delegates.fb;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.fb.FacebookDelegate;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookRequestsCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;
import com.futureplatforms.kirin.gwt.client.services.GwtFacebookService;
import com.futureplatforms.kirin.gwt.client.services.GwtFacebookService.GwtFacebookCallback;

public class GwtFacebook implements FacebookDelegate {
	public GwtFacebook() { }
	
	@Override
	public void nativeOpenSessionWithReadPermissions(
			final FacebookLoginCallback callback,
			boolean allowUI, 
			final ReadPermission... permissions) {
		final String[] perms = new String[permissions.length];
		for (int i = 0; i < permissions.length; i++) {
			final ReadPermission readPerm = permissions[i];
			perms[i] = readPerm.name();
		}
		GwtFacebookService.BACKDOOR()._openSessionWithReadPermissions(perms, allowUI, new GwtFacebookCallback() {
			
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}
			
			@Override
			public void onError() {
				callback.onFailure();
			}
			
			@Override
			public void onCancel() {
				callback.onUserCancel();
			}

			@Override
			public void onAuthFailed() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onErrorWithUserMessage(String msg) {
				callback.onFailWithUserMessage(msg);
			}
		});
	}

	@Override
	public void nativeRequestPublishPermissions(final FacebookLoginCallback callback, PublishPermission... permissions) {
		final String[] perms = new String[permissions.length];
		for (int i = 0; i < permissions.length; i++) {
			final PublishPermission readPerm = permissions[i];
			perms[i] = readPerm.name();
		}
		GwtFacebookService.BACKDOOR()._requestPublishPermissions(perms, new GwtFacebookCallback() {
			
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}
			
			@Override
			public void onError() {
				callback.onFailure();
			}
			
			@Override
			public void onCancel() {
				callback.onUserCancel();
			}
			
			@Override
			public void onAuthFailed() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onErrorWithUserMessage(String msg) {
				callback.onFailWithUserMessage(msg);
			}
		});
	}

	@Override
	public void getAccessToken(AsyncCallback2<String, String> cb) {
		GwtFacebookService.BACKDOOR()._getAccessToken(cb);
	}

	@Override
	public void getAppId(AsyncCallback1<String> cb) {
		GwtFacebookService.BACKDOOR()._getAppId(cb);
	}

	@Override
	public void signOut(AsyncCallback cb) {
		GwtFacebookService.BACKDOOR()._signOut(cb);
	}

	@Override
	public void getCurrentPermissions(AsyncCallback1<String[]> cb) {
		GwtFacebookService.BACKDOOR()._getCurrentPermissions(cb);
	}

	@Override
	public void presentShareDialogWithParams(ShareDialogParams params,
			FacebookShareCallback cb) {
		GwtFacebookService.BACKDOOR()._presentShareDialogWithParams(params, cb);
	}

	@Override
	public void presentRequestsDialog(FacebookRequestsCallback cb) {
		GwtFacebookService.BACKDOOR()._presentRequestsDialog(cb);
	}

	@Override
	public void isLoggedIn(AsyncCallback1<Boolean> cb) {
		GwtFacebookService.BACKDOOR()._isLoggedIn(cb);
	}

}