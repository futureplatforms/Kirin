package com.futureplatforms.kirin.dependencies.fb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.Formatter;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkFailType;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookRequestsCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.Permission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FacebookHelper {
	
	public static interface FBGraphResponse {
		public void onSuccess(JSONObject obj);
		public void onAuthFailed();
		public void onNetError();
	}
	
	private static final String GRAPH_ENDPOINT = "https://graph.facebook.com";
	
	private static FacebookDelegate _Delegate = StaticDependencies.getInstance().getFacebookDelegate();
	
	private static String formQueryString(Map<String, String> params) {
		Formatter form = StaticDependencies.getInstance().getFormatter();
		StringBuilder q = new StringBuilder();
		for (Entry<String, String> entry : params.entrySet()) {
			q.append(form.encodeURIComponent(entry.getKey()));
			q.append('=');
			q.append(form.encodeURIComponent(entry.getValue()));
			q.append('&');
		}
		if (q.length() == 0) { return ""; }
		return q.substring(0, q.length() - 1);
	}
	
	private static void hasAllPermissions(
			final AsyncCallback1<Boolean> cb,
			final Permission ... permissions) {
		final LogDelegate log = StaticDependencies.getInstance().getLogDelegate();
		log.log("hasAllPermissions:" + permissions);
		if (permissions != null) {
			for (Permission permission : permissions) {
				log.log(permission.toString());
			}
		}
		_Delegate.getCurrentPermissions(new AsyncCallback1<String[]>() {
			@Override
			public void onSuccess(String[] currentPermissions) {
				log.log("currentPermissions:");
				for (String permission : currentPermissions) {
					log.log(permission);
				}
				List<Permission> permCopy;
				if (permissions != null) {
					permCopy = Lists.newArrayList(permissions);
				} else {
					permCopy = Lists.newArrayList();
				}
				for (String permission : currentPermissions) {
					try {
						ReadPermission readPerm = ReadPermission.valueOf(permission);
						permCopy.remove(readPerm);
					} catch (IllegalArgumentException e) {
						try {
							PublishPermission pubPerm = PublishPermission.valueOf(permission);
							permCopy.remove(pubPerm);
						} catch (IllegalArgumentException e2) {
							log.log("UNKNOWN PERMISSION :: " + permission);
						}
					}
				}
				log.log("Remaining:");
				for (Permission permission : permCopy) {
					log.log(permission.toString());
				}
				cb.onSuccess(permCopy.isEmpty());
			}

			@Override
			public void onFailure() { }
		});
	}
	
	private static boolean _AuthFailed = false;
	
	public static void graph(final HttpVerb verb, final String endpoint, final Map<String, String> params, final FBGraphResponse resp) {
		final LogDelegate log = StaticDependencies.getInstance().getLogDelegate();
		final NetworkDelegate net = StaticDependencies.getInstance().getNetworkDelegate();
		final JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();
		_Delegate.getAccessToken(new AsyncCallback2<String, String>() {

			@Override
			public void onSuccess(String accessToken, String expDate) {
				params.put("access_token", accessToken);
				String url = GRAPH_ENDPOINT + endpoint + '?' + formQueryString(params);
				log.log("url: " + url);
				net.doHttp(verb, url, "", new NetworkResponse() {
					
					@Override
					public void onSuccess(int res, String result, Map<String, String> headers) {
						log.log("res: " + res + ", " + result);
						_AuthFailed = false;
						if (res == 400 || res == 403) {
							_AuthFailed = true;
							resp.onAuthFailed();
						} else if (res == 200) {
							JSONObject obj = null;
							try {
								obj = json.getJSONObject(result);
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
						log.log(verb + ": " + endpoint + " onFail(" + code + ")");
						resp.onNetError();
					}

					@Override
					protected void onFailWithStatus(String code,
							NetworkFailType failType) {
                        onFail(code);
						
					}
				});
			}

			@Override
			public void onFailure() {}
		});
	}
	
	@SuppressWarnings("serial")
	public static void fql(final String fql, FBGraphResponse nr) {
		LogDelegate log = StaticDependencies.getInstance().getLogDelegate();
		log.log("FQL :: " + fql);
		graph(	HttpVerb.GET,
				"/fql", 
				new HashMap<String, String>() {{ this.put("q", fql); }}, 
				nr);
	}
	
	private static void isLoggedInWithAllPermissions(final AsyncCallback1<Boolean> cb, final Permission ... permissions) {
		_Delegate.isLoggedIn(new AsyncCallback1<Boolean>() {

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
				cb.onSuccess(false);
			}
		});
	}
	
	public static void openSessionWithReadPermissions(final FacebookLoginCallback callback, final boolean allowUI, final ReadPermission ... permissions) {
		isLoggedInWithAllPermissions(new AsyncCallback1<Boolean>() {

			@Override
			public void onSuccess(Boolean isLoggedInWithAllPermissions) {
				if (isLoggedInWithAllPermissions) {
					callback.onSuccess();
				} else {
					_Delegate.nativeOpenSessionWithReadPermissions(callback, allowUI, permissions);
				}
			}

			@Override
			public void onFailure() {
				callback.onFailure();
			}
		}, permissions);
	}
	
	public static void requestPublishPermissions(final FacebookLoginCallback callback, final PublishPermission ... permissions) {
		isLoggedInWithAllPermissions(new AsyncCallback1<Boolean>() {

			@Override
			public void onSuccess(Boolean isLoggedInWithAllPermissions) {
				if (isLoggedInWithAllPermissions) {
					callback.onSuccess();
				} else {
					_Delegate.nativeRequestPublishPermissions(callback, permissions);
				}
			}

			@Override
			public void onFailure() {
				callback.onFailure();
			}
		});
	}
	
	public static void myId(final AsyncCallback1<String> cb) {
		Map<String, String> params = Maps.newHashMap();
		params.put("fields", "id");
		graph(HttpVerb.GET, "/me", params, new FBGraphResponse() {
			
			@Override
			public void onSuccess(JSONObject obj) {
				try {
					String id = obj.getString("id");
					cb.onSuccess(id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onNetError() {
				cb.onFailure();
			}
			
			@Override
			public void onAuthFailed() {
				cb.onFailure();
			}
		});
	}
	
	public static void signOut(AsyncCallback cb) {
		_Delegate.signOut(cb);
	}
	
	public static void presentShareDialogWithParams(ShareDialogParams params, FacebookShareCallback cb) {
		_Delegate.presentShareDialogWithParams(params, cb);
	}
	
	public static void presentRequestsDialog(FacebookRequestsCallback cb) {
		_Delegate.presentRequestsDialog(cb);
	}
	
	public static void isLoggedIn(AsyncCallback1<Boolean> cb) {
		_Delegate.isLoggedIn(cb);
	}
	
	public static void getAccessToken(AsyncCallback2<String, String> cb) {
		_Delegate.getAccessToken(cb);
	}
}