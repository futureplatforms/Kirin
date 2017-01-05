package com.futureplatforms.kirin.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.futureplatforms.kirin.android.dropbox.AndroidDropboxes;
import com.futureplatforms.kirin.dependencies.StaticDependencies;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Build;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse.OnCancelledListener;
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

	private static String getResult(HttpReturnType returnType, HttpEntity entity) throws IOException {
		if (returnType == HttpReturnType.Plain) {
			return EntityUtils.toString(entity, "UTF-8");
		} else if (returnType == HttpReturnType.Base64){
			return BaseEncoding.base64().encode(EntityUtils.toByteArray(entity));
		} else {
			return AndroidDropboxes.getInstance()._NetworkDropbox.putItem(EntityUtils.toByteArray(entity));
		}
	}

	private class GetAsyncTask extends AsyncTask<Object, Void, Boolean> {
		String url;
		Map<String, String> headers;
		NetworkResponse callback;

		int res = 200;
		String result;
		Map<String, String> responseHeaderMap;

		String code;
		private HttpReturnType _ReturnType;
		
		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Object... params) {
			url = (String) params[0];
			headers = (Map<String, String>) params[1];
			callback = (NetworkResponse) params[2];
			_ReturnType = (HttpReturnType) params[3];
			
			try {

				HttpClient client = new DefaultHttpClient();
				final HttpGet get = new HttpGet(url);

				get.setHeader("User-Agent", userAgent);
				if (headers != null) for (String name : headers.keySet()) {
					get.setHeader(name, headers.get(name));
				}
				callback.setOnCancelledListener(new OnCancelledListener() {
					
					@Override
					public void onCancel() {
						get.abort();
					}
				});

				HttpResponse getResponse = client.execute(get);
				try {
					res = getResponse.getStatusLine().getStatusCode();
				} catch (Throwable t) {}
				Header[] responseHeaders = getResponse.getAllHeaders();

				responseHeaderMap = new HashMap<>();
				for (Header h : responseHeaders) {
					responseHeaderMap.put(h.getName(), h.getValue());
				}

				result = getResult(_ReturnType, getResponse.getEntity());
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

	private class PostPutAsyncTask extends AsyncTask<Object, Void, Boolean> {
		String url;
		String toPost;
		Map<String, String> headers;
		NetworkResponse callback;

		int res = 200;
		String result;
		Map<String, String> responseHeaderMap;

		String code;
		private HttpVerb _Verb;
		private HttpReturnType _ReturnType;
		
		public PostPutAsyncTask(HttpVerb verb) {
			this._Verb = verb;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Object... params) {
			url = (String) params[0];
			toPost = (String) params[1];
			headers = (Map<String, String>) params[2];
			callback = (NetworkResponse) params[3];
			_ReturnType = (HttpReturnType) params[4];
			
			try {
				HttpClient client = new DefaultHttpClient();

				HttpEntityEnclosingRequestBase base;
				if (_Verb == HttpVerb.POST) {
					base = new HttpPost(url);
				} else if (_Verb == HttpVerb.PUT) {
					base = new HttpPut(url);
				} else {
					throw new IllegalStateException("verb is " + _Verb
							+ ", can only be put or post");
				}
				if (headers != null) for (String name : headers.keySet()) {
					base.setHeader(name, headers.get(name));
				}

				base.setEntity(new StringEntity(toPost, HTTP.UTF_8));

				HttpResponse postResponse = client.execute(base);
				try {
					res = postResponse.getStatusLine().getStatusCode();
				} catch (Throwable t) {}

				Header[] responseHeaders = postResponse.getAllHeaders();

				responseHeaderMap = new HashMap<>();
				for (Header h : responseHeaders) {
					responseHeaderMap.put(h.getName(), h.getValue());
				}

				result = getResult(_ReturnType, postResponse.getEntity());

				return true;

			} catch (Exception e) {
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
		switch (verb) {
			case GET:
				new GetAsyncTask().execute(url, headers, callback, HttpReturnType.Plain);
				break;
			case POST:
			case PUT:
				new PostPutAsyncTask(verb).execute(url, payload, headers, callback, HttpReturnType.Plain);
				break;

			default:
				throw new IllegalArgumentException(verb + " unsupported");
		}
	}

	@Override
	public void doHttpWithBase64Return(HttpVerb verb, String url,
			String payload, Map<String, String> headers,
			NetworkResponse callback) {
		switch (verb) {
			case GET:
				new GetAsyncTask().execute(url, headers, callback, HttpReturnType.Base64);
				break;
			case POST:
			case PUT:
				new PostPutAsyncTask(verb).execute(url, payload, headers, callback, HttpReturnType.Base64);
				break;
	
			default:
				throw new IllegalArgumentException(verb + " unsupported");
		}
	}

	@Override
	public void doHttpWithTokenReturn(HttpVerb verb, String url,
									   String payload, Map<String, String> headers,
									   NetworkResponse callback) {
		switch (verb) {
			case GET:
				new GetAsyncTask().execute(url, headers, callback, HttpReturnType.Token);
				break;
			case POST:
			case PUT:
				new PostPutAsyncTask(verb).execute(url, payload, headers, callback, HttpReturnType.Token);
				break;

			default:
				throw new IllegalArgumentException(verb + " unsupported");
		}
	}
}
