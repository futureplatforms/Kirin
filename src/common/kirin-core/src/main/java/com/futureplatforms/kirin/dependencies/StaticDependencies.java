package com.futureplatforms.kirin.dependencies;

import java.util.HashMap;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.TimerTask.TimerDelegate;
import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate;
import com.futureplatforms.kirin.dependencies.fb.FacebookDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.xml.parser.XMLParser;

public final class StaticDependencies {
	public enum Configuration {
		Debug, Release
	}
	
	public enum NetworkFailType { NetworkFail, TimeoutFail, UnknownFail }

	private static StaticDependencies instance;

	public static StaticDependencies getInstance() {
		if (instance == null) {
			instance = new StaticDependencies();
		}
		return instance;
	}

	public interface LogDelegate {
		void log(String s);
		void log(String tag, String s);
		void log(String tag, String s, Throwable t);
	}

	public interface SettingsDelegate {
		String get(String key);
		void put(String key, String value);
		void clear();
	}

	public interface NetworkDelegateClient {
		void doHttp(HttpVerb verb, String url, String payload, Map<String, String> headers,
				NetworkResponse callback);
		void doHttpWithBase64Return(HttpVerb verb, String url, String payload, Map<String, String> headers,
				NetworkResponse callback);
		void doHttpWithTokenReturn(HttpVerb verb, String url, String payload, Map<String, String> headers,
									NetworkResponse callback);
	}

	public static class NetworkDelegate {
		private NetworkDelegateClient _Client;
		private static Boolean hasNetworkConnection;
		
		public NetworkDelegate(NetworkDelegateClient client) {
			this._Client = client;
		}

		public enum HttpVerb {
			DELETE(false), GET(false), POST(true), PUT(true);
			public final boolean _HasPayload;

			HttpVerb(boolean hasPayload) {
				this._HasPayload = hasPayload;
			}
		}

		public static void UpdateNetworkConnectionStatus(boolean enabled)
		{
			hasNetworkConnection = new Boolean(enabled);
		}
		
		public static abstract class NetworkResponse {
			private boolean cancelled;
			private OnCancelledListener mOnCancelledListener;

			protected abstract void onSuccess(int res, String result, Map<String, String> headers);
			protected abstract void onFail(String code);
			protected void onFailWithStatus(String code, NetworkFailType failType) {}
			
			public void callOnSuccess(int res, String result, Map<String, String> headers) {
				if (!cancelled) onSuccess(res, result, headers);
			}

			public void callOnFail(String code) 
			{
				if (!cancelled)
				{
					if (Boolean.TRUE.equals(hasNetworkConnection))
						onFailWithStatus(code, NetworkFailType.TimeoutFail); 
					else
						onFail(code);
				}
			}

			public void cancel() {
				cancelled = true;
				if(mOnCancelledListener != null) mOnCancelledListener.onCancel();
			}

			public boolean isCancelled() {
				return cancelled;
			}

			public void setOnCancelledListener(OnCancelledListener listener) {
				this.mOnCancelledListener = listener;
			}

			public interface OnCancelledListener {
				void onCancel();
			}

		}

		public final void doHttp(HttpVerb verb, String url, NetworkResponse callback) {
			doHttp(verb, url, null, new HashMap<String, String>(), callback);
		}

		public final void doHttp(HttpVerb verb, String url, String payload, NetworkResponse callback) {
			doHttp(verb, url, payload, new HashMap<String, String>(), callback);
		}

		public final void doHttp(HttpVerb verb, String url, Map<String, String> headers,
				NetworkResponse callback) {
			doHttp(verb, url, null, headers, callback);
		}

		public final void doHttp(HttpVerb verb, String url, String payload,
				Map<String, String> headers, NetworkResponse callback) {
			_Client.doHttp(verb, url, payload, headers, callback);
		}
		
		public final void doHttpWithBase64Return(HttpVerb verb, String url, String payload,
				Map<String, String> headers, NetworkResponse callback) {
			_Client.doHttpWithBase64Return(verb, url, payload, headers, callback);
		}

		public final void doHttpWithTokenReturn(HttpVerb verb, String url, String payload,
												 Map<String, String> headers, NetworkResponse callback) {
			_Client.doHttpWithTokenReturn(verb, url, payload, headers, callback);
		}
	}

	private LogDelegate mLogDelegate;
	private SettingsDelegate mSettingsDelegate;
	private LocationDelegate mLocationDelegate;
	private JSONDelegate mJsonDelegate;
	private NetworkDelegate mNetworkDelegate;
	private XMLParser mXmlParser;
	private Formatter mFormatter;
	private boolean mInitialised;
	private DatabaseDelegate _DatabasesDelegate;
	private Configuration _Profile;
	private TimerDelegate _TimerDelegate;
	private NotificationDelegate _NotificationDelegate;
	private FacebookDelegate _FacebookDelegate;
	
	public TimerDelegate getTimerDelegate() {
		return _TimerDelegate;
	}

	public LogDelegate getLogDelegate() {
		return mLogDelegate;
	}

	public SettingsDelegate getSettingsDelegate() {
		return mSettingsDelegate;
	}

	public LocationDelegate getLocationDelegate() {
		return mLocationDelegate;
	}

	public NetworkDelegate getNetworkDelegate() {
		return mNetworkDelegate;
	}

	public NotificationDelegate getNotificationDelegate() {
		return _NotificationDelegate;
	}

	public JSONDelegate getJsonDelegate() {
		return mJsonDelegate;
	}

	public XMLParser getXmlParser() {
		return mXmlParser;
	}

	public Formatter getFormatter() {
		return mFormatter;
	}

	public boolean initialised() {
		return mInitialised;
	}

	public Configuration getProfile() {
		return _Profile;
	}

	public DatabaseDelegate getDatabasesDelegate() {
		return _DatabasesDelegate;
	}
	
	public FacebookDelegate getFacebookDelegate() {
		return _FacebookDelegate;
	}

	public void setDependencies(LogDelegate logDelegate, SettingsDelegate settingsDelegate,
			LocationDelegate locationDelegate, NetworkDelegateClient networkDelegateClient,
			JSONDelegate jsonDelegate, XMLParser xmlParser, Formatter formatter,
			Configuration profile, DatabaseDelegate databasesDelegate, TimerDelegate timerDel,
			NotificationDelegate notificationDelegate, FacebookDelegate fbDel) {
		this.mLogDelegate = logDelegate;
		this.mSettingsDelegate = settingsDelegate;
		this.mLocationDelegate = locationDelegate;
		this.mNetworkDelegate = new NetworkDelegate(networkDelegateClient);
		this.mJsonDelegate = jsonDelegate;
		this.mXmlParser = xmlParser;
		this.mFormatter = formatter;
		this._Profile = profile;
		this.mInitialised = true;
		this._DatabasesDelegate = databasesDelegate;
		this._TimerDelegate = timerDel;
		this._NotificationDelegate = notificationDelegate;
		this._FacebookDelegate = fbDel;
	}
}
