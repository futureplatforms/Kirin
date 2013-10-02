package com.futureplatforms.kirin.android;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;

public class AndroidNetwork implements NetworkDelegateClient {
	private class GetAsyncTask extends AsyncTask<Object, Void, Boolean> {
		String url;
		Map<String, String> headers;
		NetworkResponse callback;

		int res = 200;
		String result;
		Map<String, String> responseHeaderMap;

		String code;

		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Object... params) {
			url = (String) params[0];
			headers = (Map<String, String>) params[1];
			callback = (NetworkResponse) params[2];

			try {

				HttpClient client = new DefaultHttpClient();

				HttpGet get = new HttpGet(url);

				if (headers != null)
					for (String name : headers.keySet()) {
						get.setHeader(name, headers.get(name));
					}

				HttpResponse getResponse = null;

				getResponse = client.execute(get);

				Header[] responseHeaders = getResponse.getAllHeaders();

				responseHeaderMap = new HashMap<String, String>();
				for (Header h : responseHeaders) {
					responseHeaderMap.put(h.getName(), h.getValue());
				}

				result = EntityUtils.toString(getResponse.getEntity());
				return true;

			} catch (Exception e) {
				code = e.getLocalizedMessage();
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			if (success) {
				callback.onSuccess(res, result, responseHeaderMap);
			} else {
				callback.onFail(code);
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
				if (headers != null)
					for (String name : headers.keySet()) {
						base.setHeader(name, headers.get(name));
					}

				base.setEntity(new StringEntity(toPost));

				HttpResponse postResponse = null;

				postResponse = client.execute(base);

				Header[] responseHeaders = postResponse.getAllHeaders();

				responseHeaderMap = new HashMap<String, String>();
				for (Header h : responseHeaders) {
					responseHeaderMap.put(h.getName(), h.getValue());
				}

				result = EntityUtils.toString(postResponse.getEntity());
				return true;

			} catch (Exception e) {
				code = e.getLocalizedMessage();
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			if (success) {
				callback.onSuccess(res, result, responseHeaderMap);
			} else {
				callback.onFail(code);
			}
		}

	}

	@Override
	public void doHttp(HttpVerb verb, String url, String payload,
			Map<String, String> headers, NetworkResponse callback) {
		switch (verb) {
		case GET:
			new GetAsyncTask().execute(url, headers, callback);
			break;
		case POST:
		case PUT:
			new PostPutAsyncTask(verb).execute(url, payload, headers, callback);
			break;

		default:
			throw new IllegalArgumentException("");
		}
	}
}
