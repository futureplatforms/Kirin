package com.futureplatforms.kirin.android;

//todo, handle dialog result. Handle web site when no fb. Enough being shared ?
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

	private static final String FRIEND_UID = "friend_uid";

	private static final String ALLOW_LOGIN_UI = "allowLoginUI";

	private static final int IS_LOGGED_IN = 1;
	private static final int LOG_IN_READ = 2;
	private static final int LOG_IN_PUBLISH = 3;
	private static final int REQUESTS_DIALOG = 4;
	private static final int SHARE_DIALOG = 5;

	UiLifecycleHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = new UiLifecycleHelper(this, new StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
			}

		});
		helper.onCreate(savedInstanceState);

		Intent intent = getIntent();
		final Bundle args = intent.getExtras();

		int requestType = args.getInt(REQUEST_TYPE);

		switch (requestType) {
		case IS_LOGGED_IN:
			Session s = Session.getActiveSession();
			FacebookDelegateImpl.isLoggedInCallback.onSuccess(s.getState()
					.isOpened());
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
									if (!session.getPermissions().containsAll(
											readPermissions)) {
										session.requestNewReadPermissions(new NewPermissionsRequest(
												FacebookActivity.this,
												readPermissions));
									} else {
										if (FacebookDelegateImpl.newReadPermissionsCallback != null) {
											FacebookDelegateImpl.newReadPermissionsCallback
													.onSuccess();
											FacebookDelegateImpl.newReadPermissionsCallback = null;
										}
										finish();
									}
								} else {
									if (FacebookDelegateImpl.newReadPermissionsCallback != null) {
										FacebookDelegateImpl.newReadPermissionsCallback
												.onSuccess();
										FacebookDelegateImpl.newReadPermissionsCallback = null;
									}
									finish();
								}
							} else if (exception != null) {
								if (FacebookDelegateImpl.newReadPermissionsCallback != null) {
									FacebookDelegateImpl.newReadPermissionsCallback
											.onFailure();
									FacebookDelegateImpl.newReadPermissionsCallback = null;
								}
								finish();
							} else if (exception instanceof FacebookOperationCanceledException) {
								if (FacebookDelegateImpl.newReadPermissionsCallback != null) {
									FacebookDelegateImpl.newReadPermissionsCallback
											.onUserCancel();
									FacebookDelegateImpl.newReadPermissionsCallback = null;
								}
								finish();
							}
						}
					});
			if (readSession == null) {
				if (FacebookDelegateImpl.newReadPermissionsCallback != null) {
					FacebookDelegateImpl.newReadPermissionsCallback.onFailure();
					FacebookDelegateImpl.newReadPermissionsCallback = null;
				}
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
												FacebookActivity.this,
												publishPermissions));
									} else {
										if (FacebookDelegateImpl.newPublishPermissionsCallback != null) {
											FacebookDelegateImpl.newPublishPermissionsCallback
													.onSuccess();
											FacebookDelegateImpl.newPublishPermissionsCallback = null;
										}
										finish();
									}
								}
							} else if (exception != null) {
								if (FacebookDelegateImpl.newPublishPermissionsCallback != null) {
									FacebookDelegateImpl.newPublishPermissionsCallback
											.onFailure();
									FacebookDelegateImpl.newPublishPermissionsCallback = null;
								}
								finish();
							} else if (exception instanceof FacebookOperationCanceledException) {
								if (FacebookDelegateImpl.newPublishPermissionsCallback != null) {
									FacebookDelegateImpl.newPublishPermissionsCallback
											.onUserCancel();
									FacebookDelegateImpl.newPublishPermissionsCallback = null;
								}
								finish();
							}
						}
					});
			if (publishSession == null) {
				if (FacebookDelegateImpl.newPublishPermissionsCallback != null) {
					FacebookDelegateImpl.newPublishPermissionsCallback.onFailure();
					FacebookDelegateImpl.newPublishPermissionsCallback = null;
				}
				finish();
			}
			break;
		case REQUESTS_DIALOG:
			
			
			Bundle params = new Bundle();
			params.putString("message", "friendrequest");
			
			
			WebDialog dialog = new WebDialog.RequestsDialogBuilder(this,
					Session.getActiveSession(), params)
					.setOnCompleteListener(new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {
							if (error != null) {
								if (FacebookDelegateImpl.facebookRequestsCallback != null) {
									if (error instanceof FacebookOperationCanceledException) {
										FacebookDelegateImpl.facebookRequestsCallback
											.onUserCancel();
									} else {
										FacebookDelegateImpl.facebookRequestsCallback
												.onFailure();
									}
								}
							} else {

								// TODO: FINISH ME

								// String postIdKey = "post_id";
								// if (values.containsKey(postIdKey)) {
								// cb.onSuccess(values.getString(postIdKey));
								// } else {
								// cb.onFailure();
								// }
							}

							finish();
						}
					}).build();
			dialog.show();
			break;

		case SHARE_DIALOG:

			Session session = Session.getActiveSession();
			if (session == null || !session.isOpened()) {
				session = Session.openActiveSession(this, true,
						new StatusCallback() {

							@Override
							public void call(Session session,
									SessionState state, Exception arg2) {
								if (session != null && session.isOpened()) {
									presentShareDialog(args.getString(FRIEND_UID));
								}
							}
						});
			}

			else {
				presentShareDialog(args.getString(FRIEND_UID));
			}

			break;
		}
	}

	private void presentShareDialog(String uId) {
		// Fallback. For example, publish the post using the Feed
		// Dialog
		Bundle bundle = new Bundle();
		bundle.putString("to", uId);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
				Session.getActiveSession(), bundle)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle bundle,
							FacebookException error) {

						if (error != null) {
							if (FacebookDelegateImpl.facebookShareCallback != null) {
								if (error instanceof FacebookOperationCanceledException) {
									FacebookDelegateImpl.facebookShareCallback
											.onUserCancel();
								} else {
									FacebookDelegateImpl.facebookShareCallback
											.onFailure();
								}
							}
						} else {
							if (FacebookDelegateImpl.facebookShareCallback != null) {
								FacebookDelegateImpl.facebookShareCallback
									.onSuccess("success");
							}
							android.util.Log.e("DSDS", "feedDialog on success");
							finish();
						}

					}
				}).build();
		feedDialog.show();

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
		if (permissions == null)
			return null;
		ArrayList<String> names = Lists
				.newArrayListWithCapacity(permissions.length);

		for (int i = 0; i < permissions.length; i++) {
			names.add(permissions[i].name());
		}

		return names;
	}

	public static int defaultFlags = Intent.FLAG_ACTIVITY_NEW_TASK
			| Intent.FLAG_ACTIVITY_NO_ANIMATION;

	public static Intent newIntentForIsLoggedIn(Context context) {
		return new Intent(context, FacebookActivity.class).setFlags(
				defaultFlags).putExtra(REQUEST_TYPE, IS_LOGGED_IN);
	}

	public static Intent newIntentForLogIn(Context context,
			boolean allowLoginUI, ReadPermission... readPermissions) {
		return new Intent(context, FacebookActivity.class)
				.setFlags(defaultFlags).putExtra(REQUEST_TYPE, LOG_IN_READ)
				.putExtra(PERMISSIONS, names(readPermissions))
				.putExtra(ALLOW_LOGIN_UI, allowLoginUI);
	}

	public static Intent newIntentForLogInPublish(Context context,
			boolean allowLoginUI, PublishPermission... publishPermissions) {
		return new Intent(context, FacebookActivity.class)
				.setFlags(defaultFlags).putExtra(REQUEST_TYPE, LOG_IN_PUBLISH)
				.putExtra(PERMISSIONS, names(publishPermissions))
				.putExtra(ALLOW_LOGIN_UI, allowLoginUI);
	}

	public static Intent newIntentForRequestsDialog(Context context) {
		return new Intent(context, FacebookActivity.class).setFlags(
				defaultFlags).putExtra(REQUEST_TYPE, REQUESTS_DIALOG);
	}

	public static Intent newIntentForShareDialog(Context context, String uId) {
		return new Intent(context, FacebookActivity.class)
				.setFlags(defaultFlags).putExtra(REQUEST_TYPE, SHARE_DIALOG)
				.putExtra(FRIEND_UID, uId);
	}

}
