package com.futureplatforms.kirin.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.futureplatforms.kirin.android.dropbox.AndroidDropboxes;
import com.futureplatforms.kirin.dependencies.StaticDependencies;

import android.os.AsyncTask;
import android.os.Build;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;
import com.google.common.io.BaseEncoding;

public class AndroidNetwork implements NetworkDelegateClient {
	private static final String KIRIN_VERSION = "1.0";
	final String userAgent;

	public AndroidNetwork() {
		userAgent = "Kirin/" + KIRIN_VERSION + " (Android " + Build.VERSION.RELEASE + "); "
				+ Build.MODEL;
	}
	
	private enum HttpReturnType {
		Plain, Base64, Token
	}

	private static String getResult(HttpReturnType returnType, HttpURLConnection conn) throws IOException {
		InputStream is = conn.getInputStream();
		if (returnType == HttpReturnType.Plain) {
			return slurpString(is, 1024);
		} else if (returnType == HttpReturnType.Base64){
			return BaseEncoding.base64().encode(slurpBytes(is, 1024));
		} else {
			return AndroidDropboxes.getInstance()._NetworkDropbox.putItem(slurpBytes(is, 1024));
		}
	}

	private static byte[] slurpBytes(final InputStream is, final int bufferSize)
	{
		final byte[] buffer = new byte[bufferSize];
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			try {
				for (;;) {
					int rsz = is.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.write(buffer, 0, rsz);
				}
			}
			finally {
				is.close();
			}
		}
		catch (UnsupportedEncodingException ex) {
	    /* ... */
		}
		catch (IOException ex) {
	      /* ... */
		}
		return out.toByteArray();
	}

	private static String slurpString(final InputStream is, final int bufferSize)
	{
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "UTF-8");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			}
			finally {
				in.close();
			}
		}
		catch (UnsupportedEncodingException ex) {
	    /* ... */
		}
		catch (IOException ex) {
	      /* ... */
		}
		return out.toString();
	}

	private class NetAsyncTask extends AsyncTask<Object, Void, Boolean> {
		private String url;
		private Map<String, String> headers;
		private NetworkResponse callback;

		private int res = 200;
		private String result, payload;
		private Map<String, String> responseHeaderMap;

		private String code;
		private HttpReturnType _ReturnType;
		private final HttpVerb verb;

		private NetAsyncTask(HttpVerb verb) {
			this.verb = verb;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Object... params) {
			url = (String) params[0];
			payload = (String) params[1];
			headers = (Map<String, String>) params[2];
			callback = (NetworkResponse) params[3];
			_ReturnType = (HttpReturnType) params[4];
			
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestProperty("User-Agent", userAgent);
				conn.setRequestMethod(verb.name());
				if (headers != null) for (String name : headers.keySet()) {
					conn.setRequestProperty(name, headers.get(name));
				}

				if (verb._HasPayload) {
					conn.setDoOutput(true);
					OutputStream os = conn.getOutputStream();
					os.write(payload.getBytes());
					os.close();
				}

				res = conn.getResponseCode();
				Map<String, List<String>> reqProps = conn.getHeaderFields();
				responseHeaderMap = new HashMap<>();
				Set<String> reqKeys = reqProps.keySet();
				for (String reqKey : reqKeys) {
					responseHeaderMap.put(reqKey, reqProps.get(reqKey).get(0));
				}
				result = getResult(_ReturnType, conn);
				return true;

			} catch (Exception e) {
				StaticDependencies.getInstance().getLogDelegate().log("Kirin","Network Exception",e);
				code = e.getLocalizedMessage();
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);
	        
			if (success && res >= 200 && res <= 299) {
				callback.callOnSuccess(res, result, responseHeaderMap);
			} else {
				callback.callOnFail(code);
			}
		}
	}

	@Override
	public void doHttp(HttpVerb verb, String url, String payload, Map<String, String> headers,
			NetworkResponse callback) {
		new NetAsyncTask(verb).execute(url, payload, headers, callback, HttpReturnType.Plain);
	}

	@Override
	public void doHttpWithBase64Return(HttpVerb verb, String url,
			String payload, Map<String, String> headers,
			NetworkResponse callback) {
		new NetAsyncTask(verb).execute(url, payload, headers, callback, HttpReturnType.Plain);
	}

	@Override
	public void doHttpWithTokenReturn(HttpVerb verb, String url,
									   String payload, Map<String, String> headers,
									   NetworkResponse callback) {
		new NetAsyncTask(verb).execute(url, payload, headers, callback, HttpReturnType.Plain);
	}
}
