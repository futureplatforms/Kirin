package com.futureplatforms.kirin.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.google.common.collect.Lists;

public class FacebookActivity extends Activity {
	private static final String PERMISSIONS = "permissions";

	private static final String REQUEST_TYPE = "request_type";

	private static final String ALLOW_LOGIN_UI = "allowLoginUI";

	private static final int IS_LOGGED_IN = 1;
	private static final int LOG_IN_READ = 2;
	private static final int LOG_IN_PUBLISH = 3;
	private static final int REQUESTS_DIALOG = 4;

	UiLifecycleHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = new UiLifecycleHelper(this, new StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {}
		});
		helper.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle args = intent.getExtras();

		int requestType = args.getInt(REQUEST_TYPE);

		switch (requestType) {
			case IS_LOGGED_IN:
				Session s = Session.getActiveSession();
				FacebookDelegateImpl.isLoggedInCallback.onSuccess(s.getState().isOpened());
				FacebookDelegateImpl.isLoggedInCallback = null;
				finish();
				break;
			case LOG_IN_READ:
				final ArrayList<String> readPermissions = (ArrayList<String>) args
						.getSerializable(PERMISSIONS);
				Session readSession = Session.openActiveSession(this,
						args.getBoolean(ALLOW_LOGIN_UI), new StatusCallback() {

							@Override
							public void call(Session session, SessionState state,
									Exception exception) {
								if (state.isOpened()) {
									if (readPermissions != null) {
										if (!session.getPermissions().containsAll(readPermissions)) {
											session.requestNewReadPermissions(new NewPermissionsRequest(
													FacebookActivity.this, readPermissions));
										} else {
											FacebookDelegateImpl.newReadPermissionsCallback
													.onSuccess();
											FacebookDelegateImpl.newReadPermissionsCallback = null;
											finish();
										}
									} else {
										FacebookDelegateImpl.newReadPermissionsCallback.onSuccess();
										FacebookDelegateImpl.newReadPermissionsCallback = null;
										finish();
									}
								} else if (exception != null) {
									FacebookDelegateImpl.newReadPermissionsCallback.onFailure();
									FacebookDelegateImpl.newReadPermissionsCallback = null;
									finish();
								} else if (exception instanceof FacebookOperationCanceledException) {
									FacebookDelegateImpl.newReadPermissionsCallback.onUserCancel();
									FacebookDelegateImpl.newReadPermissionsCallback = null;
									finish();
								}
							}
						});
				if (readSession == null) {
					FacebookDelegateImpl.newReadPermissionsCallback.onFailure();
					FacebookDelegateImpl.newReadPermissionsCallback = null;
					finish();
				}
				break;
			case LOG_IN_PUBLISH:
				final ArrayList<String> publishPermissions = (ArrayList<String>) args
						.getSerializable(PERMISSIONS);
				Session publishSession = Session.openActiveSession(this,
						args.getBoolean(ALLOW_LOGIN_UI), new StatusCallback() {

							@Override
							public void call(Session session, SessionState state,
									Exception exception) {
								if (state.isOpened()) {
									if (publishPermissions != null) {
										if (!session.getPermissions().containsAll(
												publishPermissions)) {
											session.requestNewPublishPermissions(new NewPermissionsRequest(
													FacebookActivity.this, publishPermissions));
										} else {
											FacebookDelegateImpl.newPublishPermissionsCallback
													.onSuccess();
											FacebookDelegateImpl.newPublishPermissionsCallback = null;
											finish();
										}
									}
								} else if (exception != null) {
									FacebookDelegateImpl.newPublishPermissionsCallback.onFailure();
									FacebookDelegateImpl.newPublishPermissionsCallback = null;
									finish();
								} else if (exception instanceof FacebookOperationCanceledException) {
									FacebookDelegateImpl.newPublishPermissionsCallback
											.onUserCancel();
									FacebookDelegateImpl.newPublishPermissionsCallback = null;
									finish();
								}
							}
						});
				if (publishSession == null) {
					FacebookDelegateImpl.newPublishPermissionsCallback.onFailure();
					FacebookDelegateImpl.newPublishPermissionsCallback = null;
					finish();
				}
				break;
			case REQUESTS_DIALOG:
				WebDialog dialog = new WebDialog.RequestsDialogBuilder(this,
						Session.getActiveSession(), new Bundle()).setOnCompleteListener(
						new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values, FacebookException error) {
								// TODO complete me
								/*
								 * if (error != null) { if (err instanceof
								 * FacebookOperationCanceledException) {
								 * cb.onUserCancel(); } else { cb.onFailure(); }
								 * } else { String postIdKey = "post_id"; if
								 * (bundle.containsKey(postIdKey)) {
								 * cb.onSuccess(bundle.getString(postIdKey)); }
								 * else { cb.onFailure(); } }
								 */
								finish();
							}
						}).build();
				dialog.show();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		helper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		helper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		helper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		helper.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		helper.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.onDestroy();
	}

	public static ArrayList<String> names(Enum[] permissions) {
		if (permissions == null) return null;
		ArrayList<String> names = Lists.newArrayListWithCapacity(permissions.length);

		for (int i = 0; i < permissions.length; i++) {
			names.add(permissions[i].name());
		}

		return names;
	}

	public static int defaultFlags = Intent.FLAG_ACTIVITY_NEW_TASK
			| Intent.FLAG_ACTIVITY_NO_ANIMATION;

	public static Intent newIntentForIsLoggedIn(Context context) {
		return new Intent(context, FacebookActivity.class).setFlags(defaultFlags).putExtra(
				REQUEST_TYPE, IS_LOGGED_IN);
	}

	public static Intent newIntentForLogIn(Context context, boolean allowLoginUI,
			ReadPermission... readPermissions) {
		return new Intent(context, FacebookActivity.class).setFlags(defaultFlags)
				.putExtra(REQUEST_TYPE, LOG_IN_READ).putExtra(PERMISSIONS, names(readPermissions))
				.putExtra(ALLOW_LOGIN_UI, allowLoginUI);
	}

	public static Intent newIntentForLogInPublish(Context context, boolean allowLoginUI,
			PublishPermission... publishPermissions) {
		return new Intent(context, FacebookActivity.class).setFlags(defaultFlags)
				.putExtra(REQUEST_TYPE, LOG_IN_PUBLISH)
				.putExtra(PERMISSIONS, names(publishPermissions))
				.putExtra(ALLOW_LOGIN_UI, allowLoginUI);
	}

	public static Intent newIntentForRequestsDialog(Context context) {
		return new Intent(context, FacebookActivity.class).setFlags(defaultFlags).putExtra(
				REQUEST_TYPE, REQUESTS_DIALOG);
	}
}
