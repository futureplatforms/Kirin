package com.futureplatforms.kirin.proxo;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.futureplatforms.kirin.dependencies.TimerTask;
import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.db.Transaction;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
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
	private String _Bakage, _Password;
	
	
	/**
	 * Set bakage if you want to kick off Proxocube with some baked-in data
	 * @param url
	 * @param client
	 * @param bakage
	 */
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
	
	public void setBakedInData(String baked) {
		_Bakage = baked;
	}
	
	public void setAESPassword(String password) {
		this._Password = password;
	}
	
	public boolean hasSynced() {
		return _Revision > -1;
	}
	
	private void process(final String result, final Database db, final int startRevision, final boolean didBake) {
		db.transaction(new TxRunner() {
			
			@Override
			public void run(Transaction tx) {
				// A proxo response is a JSON array
				try {
					JSONArray arr = _Json.getJSONArray(result);
					for (int i=0; i<arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						
						// As we iterate through the objects, keep track of the highest revision ID
						int rev = obj.getInt("revision");
						_Revision = Math.max(rev, _Revision);
						
						if (obj.has("deleted") && obj.getBoolean("deleted")) {
							_Client.onDelete(tx, obj);
						} else {
							_Client.onInsertOrUpdate(tx, obj);
						}
					}
					_Client.onSyncCompleting(tx);
				} catch (JSONException e) {
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
				_Client.onSyncComplete(_Revision != startRevision, didBake);
				
				if (didBake) {
					// OK kick off another sync now
					new TimerTask() {

						@Override
						public void run() {
							sync(db);
						}
						
					}.schedule(1);
				}
			}
		});
	}
	
	/**
	 * Proxo sync is encapsulated within a database transaction
	 * We don't consider ourselves to be at the latest revision until the transaction
	 * is complete
	 * @param db
	 */
	public void sync(final Database db) {
		final int startRevision = _Revision;
		if (_Revision == -1 && !Strings.isNullOrEmpty(_Bakage)) {
			process(_Bakage, db, startRevision, true);
		} else {
			_Net.doHttp(HttpVerb.GET, _Url + "/" + (_Revision + 1), new NetworkResponse() {
				
				@Override
				public void onSuccess(int res, String result, Map<String, String> headers) {
					if (!Strings.isNullOrEmpty(_Password)) {
						result = StaticDependencies.getInstance().getFormatter().decryptAES(result, _Password);
					}
					process(result, db, startRevision, false);
				}
				
				@Override
				public void onFail(String code) {
					_Client.onError();
				}
			});
		}
	}
	
	public static String sha512Base64(String value) {
		return StaticDependencies.getInstance().getFormatter().sha512B64(value);
	}
}
