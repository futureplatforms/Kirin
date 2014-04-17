package com.futureplatforms.kirin.dependencies.fb.friends;

import java.util.Date;
import java.util.List;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.futureplatforms.kirin.dependencies.fb.FacebookHelper;
import com.futureplatforms.kirin.dependencies.fb.FacebookHelper.FBGraphResponse;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.google.common.collect.Lists;

public class FacebookFriends {
	private static final StaticDependencies _Deps = StaticDependencies.getInstance();
	private static final SettingsDelegate _Settings = _Deps.getSettingsDelegate();
	
	private static final String fqlFriendsWithApp = "SELECT uid, first_name, last_name, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me()) AND is_app_user";
	
	public static class Friend {
		public final String _FirstName, _LastName, _Uid, _PicSquare;
		public Friend(String first, String last, String uid, String pic) {
			this._FirstName = first;
			this._LastName = last;
			this._Uid = uid;
			this._PicSquare = pic;
		}
	}
	
	public static void setFacebookFriendsDB(FacebookFriendsDBPlugin plugin, long intervalMs) {
		_DBPlugin = plugin;
		_IntervalMs = intervalMs;
	}
	private static FacebookFriendsDBPlugin _DBPlugin;
	private static long _IntervalMs;
	
	private static final String LastSyncedKey = "kirin.friends.last.synced";
	private static final String FbAccessTokenKey = "kirin.friends.access.token";
	
	private static void checkDBValid(final AsyncCallback1<Boolean> cb) {
		FacebookHelper.getAccessToken(new AsyncCallback2<String, String>() {
			
			@Override
			public void onSuccess(String accessToken, String exp) {
				String savedAccessToken = _Settings.get(FbAccessTokenKey);
				if (!accessToken.equals(savedAccessToken)) {
					_DBPlugin.resetDB(new AsyncCallback() {
						
						@Override
						public void onSuccess() {
							_Settings.put(FbAccessTokenKey, "");
							_Settings.put(LastSyncedKey, "");
							cb.onSuccess(false);
						}
						
						@Override
						public void onFailure() { }
					});
				} else {
					cb.onSuccess(true);
				}
			}
			
			@Override
			public void onFailure() {
				cb.onFailure();
			}
		});
	}
	
	private static void tryDB(final AsyncCallback1<List<Friend>> cb) {
		// is DB valid?
		if (_DBPlugin != null) {
			checkDBValid(new AsyncCallback1<Boolean>() {
	
				@Override
				public void onSuccess(Boolean dbValid) {
					if (dbValid) {
						_DBPlugin.getFriends(cb);
					}
				}
	
				@Override
				public void onFailure() { }
			});
		}
	}
	
	private static boolean shouldUseNetwork() {
		if (_DBPlugin != null) {
			try {
				long lastSynced = Long.parseLong(_Settings.get(LastSyncedKey), 10);
				if ((new Date().getTime() - lastSynced) > _IntervalMs) {
					return true;
				} else {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return true;
		}
	}
	
	public static void listFriendsWithAppInstalled(final AsyncCallback1<List<Friend>> cb) {
		tryDB(cb);
		if (shouldUseNetwork()) {
			FacebookHelper.fql(fqlFriendsWithApp, new FBGraphResponse() {
				
				@Override
				public void onSuccess(JSONObject obj) {
					final List<Friend> friends = Lists.newArrayList();
					try {
						JSONArray arr = obj.getJSONArray("data");
						for (int i=0, len=arr.length(); i<len; i++) {
							JSONObject jsonFriend = arr.getJSONObject(i);
							try {
								Friend f = new Friend(
										jsonFriend.getString("first_name"),
										jsonFriend.getString("last_name"),
										""+jsonFriend.getInt("uid"),
										jsonFriend.getString("pic_square"));
								friends.add(f);
							} catch (JSONException e) {
								StaticDependencies.getInstance().getLogDelegate().log("failed to process friend, " + e.getLocalizedMessage());
							}
						}
					} catch (JSONException e) {
						StaticDependencies.getInstance().getLogDelegate().log("failed to process friends, " + e.getLocalizedMessage());
					}
					if (_DBPlugin != null) {
						_DBPlugin.saveFriends(friends, new AsyncCallback() {

							@Override
							public void onSuccess() {
								cb.onSuccess(friends);
							}

							@Override
							public void onFailure() { }
							
						});
					} else {
						cb.onSuccess(friends);
					}
					_Settings.put(LastSyncedKey, ""+new Date().getTime());
				}
				
				@Override
				public void onNetError() {
					cb.onFailure();
				}
				
				@Override
				public void onAuthFailed() { }
			});
		}
	}
}
