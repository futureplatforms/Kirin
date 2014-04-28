package com.futureplatforms.kirin.android;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.Callback;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
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
import com.google.common.collect.Maps;

public class FacebookDelegateImpl implements FacebookDelegate {

	private Activity _Activity;

	private Map<Activity, UiLifecycleHelper> _LifecycleMap = Maps.newHashMap();

	private Context context;

	public FacebookDelegateImpl(Context context) {
		this.context = context;
	}

	public void setCurrentActivity(Activity activity) {
		this._Activity = activity;
	}

	public UiLifecycleHelper getLifecycleHelper(Activity activity) {
		UiLifecycleHelper helper = new UiLifecycleHelper(activity, new StatusCallback() {

			@Override
			public void call(final Session session, SessionState state, Exception exc) {
				Log.d("STATE", "" + state.name());
			}
		}) {
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data) {
				Callback cb = new Callback() {

					@Override
					public void onError(PendingCall arg0, Exception arg1, Bundle arg2) {
						_Callback.onFailure();
					}

					@Override
					public void onComplete(PendingCall pc, Bundle bundle) {
						String completionGestureKey = "com.facebook.platform.extra.COMPLETION_GESTURE";
						String postIDKey = "com.facebook.platform.extra.POST_ID";
						if (bundle.containsKey(completionGestureKey)
								&& "post".equals(bundle.getString(completionGestureKey))) {
							if (bundle.containsKey(postIDKey)) {
								_Callback.onSuccess(bundle.getString(postIDKey));
							} else {
								_Callback.onSuccess("");
							}
						} else {
							_Callback.onFailure();
						}

					}
				};
				super.onActivityResult(requestCode, resultCode, data, cb);
			}
		};
		_LifecycleMap.put(activity, helper);
		_Activity = activity;
		return helper;
	}

	@Override
	public void getAccessToken(AsyncCallback2<String, String> cb) {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			cb.onSuccess(session.getAccessToken(), f.format(session.getExpirationDate()));
		} else {
			cb.onFailure();
		}
	}

	@Override
	public void getAppId(AsyncCallback1<String> cb) {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			cb.onSuccess(session.getApplicationId());
		} else {
			cb.onFailure();
		}
	}

	@Override
	public void getCurrentPermissions(AsyncCallback1<String[]> cb) {
		Session session = Session.getActiveSession();
		if (session != null & session.isOpened()) {
			List<String> permissions = session.getPermissions();
			for (String permission : permissions) {
				Log.d("permission", permission);
			}
			cb.onSuccess(permissions.toArray(new String[0]));
		} else {
			cb.onFailure();
		}
	}

	// Nicked this off http://stackoverflow.com/a/14048644
	// private static Session openActiveSession(Activity activity, boolean
	// allowLoginUI,
	// StatusCallback callback, List<String> permissions) {
	// Log.d("FB", "allowLoginUI :: " + allowLoginUI);
	// Session session = new Builder(activity).build();
	// if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) ||
	// allowLoginUI) {
	// if (allowLoginUI || hasAllPermissions(session.getPermissions(),
	// permissions)) {
	// Log.d("FB", "about to open for read");
	// return openForRead(activity, permissions, callback);
	// }
	// }
	// return null;
	// }

	private FacebookShareCallback _Callback;

	@Override
	public void presentShareDialogWithParams(final ShareDialogParams params,
			final FacebookShareCallback cb) {
		Log.d("FB", "PresentShareDialogWithParams");
		if (FacebookDialog.canPresentShareDialog(_Activity,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(_Activity)
					.setCaption(params._Caption).setDescription(params._Description)
					.setFriends(params._Friends.asList()).setLink(params._Link)
					.setName(params._Name).setPicture(params._Picture).setPlace(params._Place)
					.setRef(params._Ref).build();
			PendingCall pendingCall = shareDialog.present();
			_Callback = cb;
			_LifecycleMap.get(_Activity).trackPendingDialogCall(pendingCall);
		} else {
			Session session = Session.getActiveSession();
			if (session == null || !session.isOpened()) {
				session = Session.openActiveSession(_Activity, true, new StatusCallback() {

					@Override
					public void call(Session session, SessionState state, Exception arg2) {
						if (session != null && session.isOpened()) {
							presentShareDialogWithParams(params, cb);
						}
					}
				});
			} else {
				// Fallback. For example, publish the post using the Feed Dialog
				Bundle bundle = new Bundle();
				bundle.putString("caption", params._Caption);
				bundle.putString("description", params._Description);
				bundle.putString("link", params._Link);
				bundle.putString("name", params._Name);
				bundle.putString("picture", params._Picture);
				bundle.putString("ref", params._Ref);
				WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(_Activity,
						Session.getActiveSession(), bundle)).setOnCompleteListener(
						new OnCompleteListener() {

							@Override
							public void onComplete(Bundle bundle, FacebookException err) {
								if (err != null) {
									if (err instanceof FacebookOperationCanceledException) {
										cb.onUserCancel();
									} else {
										cb.onFailure();
									}
								} else {
									String postIdKey = "post_id";
									if (bundle.containsKey(postIdKey)) {
										cb.onSuccess(bundle.getString(postIdKey));
									} else {
										cb.onFailure();
									}
								}
							}
						}).build();
				feedDialog.show();
			}
		}
	}

	@Override
	public void signOut(AsyncCallback cb) {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			session.closeAndClearTokenInformation();
		}
	}

	public static FacebookLoginCallback newReadPermissionsCallback;

	@Override
	public void nativeOpenSessionWithReadPermissions(final FacebookLoginCallback callback,
			boolean allowUI, ReadPermission... permissions) {
		Log.d("FB", "nativeOpenSessionWithReadPermissions");
		newReadPermissionsCallback = callback;
		context.startActivity(FacebookActivity.newIntentForLogIn(context, allowUI, permissions));
	}

	public static FacebookLoginCallback newPublishPermissionsCallback;

	@Override
	public void nativeRequestPublishPermissions(final FacebookLoginCallback callback,
			PublishPermission... permissions) {
		Log.d("FB", "nativeRequestPublishPermissions");
		newPublishPermissionsCallback = callback;
		context.startActivity(FacebookActivity.newIntentForLogInPublish(context, true, permissions));
	}

	@Override
	public void presentRequestsDialog(FacebookRequestsCallback cb) {
		WebDialog dialog = new WebDialog.RequestsDialogBuilder(_Activity,
				Session.getActiveSession(), new Bundle()).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error) {
						// TODO complete me
						/*
						 * if (error != null) { if (err instanceof
						 * FacebookOperationCanceledException) {
						 * cb.onUserCancel(); } else { cb.onFailure(); } } else
						 * { String postIdKey = "post_id"; if
						 * (bundle.containsKey(postIdKey)) {
						 * cb.onSuccess(bundle.getString(postIdKey)); } else {
						 * cb.onFailure(); } }
						 */
					}
				}).build();
		dialog.show();
	}

	// isLoggedIn

	public static AsyncCallback1<Boolean> isLoggedInCallback;

	@Override
	public void isLoggedIn(AsyncCallback1<Boolean> cb) {
		Session session = Session.getActiveSession();
		if (session == null || !session.getState().isOpened()) {
			isLoggedInCallback = cb;
			context.startActivity(FacebookActivity.newIntentForIsLoggedIn(context));
		} else cb.onSuccess(true);
	}
}
