package com.futureplatforms.kirin.android;

import java.io.UnsupportedEncodingException;
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

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;

public class AndroidNetwork implements NetworkDelegateClient {

	private Context context;

	public AndroidNetwork(Context context) {
		this.context = context;
	}

	@Override
	public void doHttp(HttpVerb verb, String url, final String payload,
			final Map<String, String> headers, final NetworkResponse callback) {

		int method;

		switch (verb) {
			case GET:
				method = Method.GET;
				break;
			case POST:
				method = Method.POST;
			case PUT:
				method = Method.PUT;
				break;

			default:
				throw new IllegalArgumentException("");
		}

		Request<NetworkResult> r = new Request<NetworkResult>(method, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onFail(error.getLocalizedMessage());
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (headers != null) return headers;
				else return super.getHeaders();
			}

			@Override
			public byte[] getBody() throws AuthFailureError {
				if (payload != null) return payload.getBytes();
				else return super.getBody();
			}

			@Override
			protected Response<NetworkResult> parseNetworkResponse(
					com.android.volley.NetworkResponse response) {
				try {
					String data = new String(response.data,
							HttpHeaderParser.parseCharset(response.headers));
					return Response.success(new NetworkResult(data, response.statusCode,
							response.headers), HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return Response.error(new VolleyError(e));
				}

			}

			@Override
			protected void deliverResponse(NetworkResult response) {
				callback.onSuccess(response.statusCode, response.data, response.headers);

			}
		};

		Volley.newRequestQueue(context).add(r);
	}

	class NetworkResult {
		String data;
		int statusCode;
		Map<String, String> headers;

		public NetworkResult(String data, int statusCode, Map<String, String> headers) {
			this.data = data;
			this.statusCode = statusCode;
			this.headers = headers;
		}
	}

}
