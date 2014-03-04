package com.futureplatforms.kirin.proxo;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.db.Transaction;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate.KirinJsonException;
import com.google.common.base.Strings;

public class Proxocube {
	private static final StaticDependencies _Deps = StaticDependencies.getInstance();
	private static final NetworkDelegate _Net = _Deps.getNetworkDelegate();
	private static final JSONDelegate _Json = _Deps.getJsonDelegate();
	private static final SettingsDelegate _Settings = _Deps.getSettingsDelegate();
	private static final LogDelegate _Log = _Deps.getLogDelegate();
	
	private final String _Url;
	private final ProxoClient _Client;
	private int _Revision;
	
	protected Proxocube(String url, ProxoClient client) {
		_Url = url;
		_Client = client;
		String rev = _Settings.get("proxocube.revision." + url);
		if (!Strings.isNullOrEmpty(rev)) {
			_Revision = Integer.parseInt(rev, 10);
		} else {
			_Revision = -1;
		}
	}
	
	/**
	 * Proxo sync is encapsulated within a database transaction
	 * We don't consider ourselves to be at the latest revision until the transaction
	 * is complete
	 * @param db
	 */
	public void sync(final Database db) {
		_Net.doHttp(HttpVerb.GET, _Url, new NetworkResponse() {
			
			@Override
			public void onSuccess(int res, final String result, Map<String, String> headers) {
				db.transaction(new TxRunner() {
					
					@Override
					public void run(Transaction tx) {
						// A proxo response is a JSON array
						try {
							JSONArray arr = _Json.getJSONArray(result);
							for (int i=0; i<arr.length(); i++) {
								JSONObject obj = arr.getJSONObject(i);
								
								// As we iterate through the objects, keep track of the highest revision ID
								if (obj.has("revision")) {
									int rev = obj.getInt("revision");
									_Revision = Math.max(rev, _Revision);
								}
								
								if (obj.has("deleted") && obj.getBoolean("deleted")) {
									_Client.onDelete(tx, obj);
								} else {
									_Client.onInsertOrUpdate(tx, obj);
								}
							}
						} catch (KirinJsonException e) {
							_Client.onError();
						}
					}
					
					@Override
					public void onError() {
						_Log.log("Proxocube database error");
						_Client.onError();
					}
					
					@Override
					public void onComplete() {
						// Complete!  Log the new revision
						_Settings.put("proxocube.revision." + _Url, "" + _Revision);
						_Log.log("Proxo sync done, now at revision " + _Revision);
						_Client.onSyncComplete();
					}
				});
				
			}
			
			@Override
			public void onFail(String code) {
				_Client.onError();
			}
		});
	}
}
