package com.futureplatforms.kirin.dependencies.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.Formatter;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.Permission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.google.common.collect.Lists;

public abstract class FacebookDelegate {
	
	public static interface FBGraphResponse {
		public void onSuccess(JSONObject obj);
		public void onAuthFailed();
		public void onNetError();
	}
	
	private static final StaticDependencies _Deps = StaticDependencies.getInstance();
	private static final NetworkDelegate _Net = _Deps.getNetworkDelegate();
	private static final Formatter _Form = _Deps.getFormatter();
	private static final LogDelegate _Log = _Deps.getLogDelegate();
	private static final JSONDelegate _JSON = _Deps.getJsonDelegate();
	
	private static final String GRAPH_ENDPOINT = "https://graph.facebook.com";
	
	private static String formQueryString(Map<String, String> params) {
		StringBuilder q = new StringBuilder();
		for (Entry<String, String> entry : params.entrySet()) {
			q.append(_Form.encodeURIComponent(entry.getKey()));
			q.append('=');
			q.append(_Form.encodeURIComponent(entry.getValue()));
			q.append('&');
		}
		if (q.length() == 0) { return ""; }
		return q.substring(0, q.length() - 1);
	}
	
	private void hasAllPermissions(
			final AsyncCallback1<Boolean> cb,
			final Permission ... permissions) {
		_Log.log("hasAllPermissions:");
		for (Permission permission : permissions) {
			_Log.log(permission.toString());
		}
		getCurrentPermissions(new AsyncCallback1<String[]>() {
			@Override
			public void onSuccess(String[] currentPermissions) {
				_Log.log("currentPermissions:");
				for (String permission : currentPermissions) {
					_Log.log(permission);
				}
				List<Permission> permCopy = Lists.newArrayList(permissions);
				for (String permission : currentPermissions) {
					try {
						ReadPermission readPerm = ReadPermission.valueOf(permission);
						permCopy.remove(readPerm);
					} catch (IllegalArgumentException e) {
						try {
							PublishPermission pubPerm = PublishPermission.valueOf(permission);
							permCopy.remove(pubPerm);
						} catch (IllegalArgumentException e2) {
							_Log.log("_Log is " + _Log);
							_Log.log("UNKNOWN PERMISSION :: " + permission);
						}
					}
				}
				_Log.log("Remaining:");
				for (Permission permission : permCopy) {
					_Log.log(permission.toString());
				}
				cb.onSuccess(permCopy.isEmpty());
			}

			@Override
			public void onFailure() { }
		});
	}
	
	public abstract void isLoggedIn(AsyncCallback1<Boolean> cb);
	public abstract void getAccessToken(AsyncCallback1<String> cb);
	public abstract void getAppId(AsyncCallback1<String> cb);
	public abstract void getCurrentPermissions(AsyncCallback1<String[]> cb);
	public abstract void presentShareDialogWithParams(ShareDialogParams params, FacebookShareCallback cb);
	
	private boolean _AuthFailed = false;
	
	public void graph(final HttpVerb verb, final String endpoint, final Map<String, String> params, final FBGraphResponse resp) {
		getAccessToken(new AsyncCallback1<String>() {

			@Override
			public void onSuccess(String accessToken) {
				params.put("access_token", accessToken);
				String url = GRAPH_ENDPOINT + endpoint + '?' + formQueryString(params);
				_Log.log("url: " + url);
				_Net.doHttp(verb, url, "", new NetworkResponse() {
					
					@Override
					public void onSuccess(int res, String result, Map<String, String> headers) {
						_Log.log("res: " + res + ", " + result);
						_AuthFailed = false;
						if (res == 400 || res == 403) {
							_AuthFailed = true;
							resp.onAuthFailed();
						} else if (res == 200) {
							JSONObject obj = null;
							try {
								obj = _JSON.getJSONObject(result);
							} catch (JSONException e) {
								resp.onNetError();
								return;
							}
							resp.onSuccess(obj);
						} else {
							resp.onNetError();
						}
					}
					
					@Override
					public void onFail(String code) {
						_Log.log(verb + ": " + endpoint + " onFail(" + code + ")");
						resp.onNetError();
					}
				});
			}

			@Override
			public void onFailure() {}
		});
	}
	
	@SuppressWarnings("serial")
	public void fql(final String fql, FBGraphResponse nr) {
		_Log.log("FQL :: " + fql);
		graph(	HttpVerb.GET,
				"/fql", 
				new HashMap<String, String>() {{ this.put("q", fql); }}, 
				nr);
	}
	
	public abstract void signOut(AsyncCallback cb);
	
	private void isLoggedInWithAllPermissions(final AsyncCallback1<Boolean> cb, final Permission ... permissions) {
		isLoggedIn(new AsyncCallback1<Boolean>() {

			@Override
			public void onSuccess(Boolean isLoggedIn) {
				if (isLoggedIn) {
					if (_AuthFailed) {
						cb.onSuccess(false);
					} else {
						hasAllPermissions(cb, permissions);
					}
				} else {
					cb.onSuccess(false);
				}
			}

			@Override
			public void onFailure() {
				cb.onFailure();
			} 
		});
	}
	
	public void openSessionWithReadPermissions(final FacebookLoginCallback callback, final boolean allowUI, final ReadPermission ... permissions) {
		isLoggedInWithAllPermissions(new AsyncCallback1<Boolean>() {

			@Override
			public void onSuccess(Boolean isLoggedInWithAllPermissions) {
				if (isLoggedInWithAllPermissions) {
					callback.onSuccess();
				} else {
					nativeOpenSessionWithReadPermissions(callback, allowUI, permissions);
				}
			}

			@Override
			public void onFailure() {
				callback.onFailure();
			}
		}, permissions);
	}
	
	public void requestPublishPermissions(final FacebookLoginCallback callback, final PublishPermission ... permissions) {
		isLoggedInWithAllPermissions(new AsyncCallback1<Boolean>() {

			@Override
			public void onSuccess(Boolean isLoggedInWithAllPermissions) {
				if (isLoggedInWithAllPermissions) {
					callback.onSuccess();
				} else {
					nativeRequestPublishPermissions(callback, permissions);
				}
			}

			@Override
			public void onFailure() {
				callback.onFailure();
			}
		});
	}
	
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
}