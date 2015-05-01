package com.futureplatforms.kirin.dependencies.fb.friends;

import java.util.Date;
import java.util.List;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
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
	private static final LogDelegate _Log = _Deps.getLogDelegate();
	
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
	
	private static void tryDB(final AsyncCallback1<List<Friend>> cb) {
		// is DB valid?
		if (_DBPlugin != null) {
			_DBPlugin.getFriends(cb);
		}
	}
	
	private static boolean shouldUseNetwork() {
		if (_DBPlugin != null) {
			try {
				long lastSynced = Long.parseLong(_Settings.get(LastSyncedKey), 10);
				
				// If we haven't thrown an exception, we have a valid last synced time
				if ((new Date().getTime() - lastSynced) > _IntervalMs) {
					// And it is GREATER than the interval, so DO use network 
					return true;
				} else {
					// It is LESS than the interval so DO NOT use network
					return false;
				}
			} catch (NumberFormatException e) {
				// There is no valid last synced time, so DO use network this time. 
				return true;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * onFailure gets invoked if you're not currently logged in
	 * @param cb
	 */
	public static void listFriendsWithAppInstalled(final AsyncCallback1<List<Friend>> cb) {
		tryDB(cb);
		if (shouldUseNetwork()) {
			FacebookHelper.isLoggedIn(new AsyncCallback1<Boolean>() {

				@Override
				public void onSuccess(Boolean isLoggedIn) {
					if (!isLoggedIn) {
						cb.onFailure();
					} else {
						FacebookHelper.fql(fqlFriendsWithApp, new FBGraphResponse() {
							
							@Override
							public void onSuccess(final JSONObject obj) {
								FacebookHelper.getAccessToken(new AsyncCallback2<String, String>() {

									@Override
									public void onSuccess(String accessToken, String exp) {
										final List<Friend> friends = Lists.newArrayList();
										try {
											JSONArray arr = obj.getJSONArray("data");
											for (int i=0, len=arr.length(); i<len; i++) {
												JSONObject jsonFriend = arr.getJSONObject(i);
												try {
													Friend f = new Friend(
															jsonFriend.getString("first_name"),
															jsonFriend.getString("last_name"),
															jsonFriend.getString("uid"),
															jsonFriend.getString("pic_square"));
													friends.add(f);
												} catch (JSONException e) {
													StaticDependencies.getInstance().getLogDelegate().log("FACEBOOK","failed to process friend, ", e);
												}
											}
										} catch (JSONException e) {
											StaticDependencies.getInstance().getLogDelegate().log("FACEBOOK","failed to process friends, ", e);
										}
										
										_Settings.put(LastSyncedKey, ""+new Date().getTime());
										
										if (_DBPlugin != null) {
											_DBPlugin.saveFriends(friends, new AsyncCallback() {
												
												@Override
												public void onSuccess() {
													// Don't just return the friends array
													// as it's not in the right order.
													// Retrieve from the database
													tryDB(cb);
												}
												
												@Override
												public void onFailure() { }
												
											});
										} else {
											cb.onSuccess(friends);
										}
									}

									@Override
									public void onFailure() {
										
									}
									
								});
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

				@Override
				public void onFailure() { cb.onFailure(); }
			});
		}
	}
	
	public static void friendForUid(String uid, AsyncCallback1<Friend> cb) {
		if (_DBPlugin != null) {
			_DBPlugin.friendForUid(uid, cb);
		}
	}
}
