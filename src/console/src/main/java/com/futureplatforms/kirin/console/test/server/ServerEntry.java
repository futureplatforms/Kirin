package com.futureplatforms.kirin.console.test.server;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerEntry {

	public interface Condition {
		boolean matches(String postData);
	}
	
	private final String _UrlContains, _ToReturn;
	private final Condition _Condition;
	private final Map<String, String> _HeadersToReturn;
	
	public ServerEntry(String urlContains, String toReturn) {
		this(urlContains, new Condition() {
			
			@Override
			public boolean matches(String postData) {
				return true;
			}
		}, toReturn);
	}

	public ServerEntry(String urlContains, String toReturn, Map<String, String> headersToReturn) {
		this(urlContains, new Condition() {

			@Override
			public boolean matches(String postData) {
				return true;
			}
		}, toReturn, headersToReturn);
	}

	public ServerEntry(String urlContains, Condition condition, String toReturn) {
		this(urlContains, condition, toReturn, new HashMap<String, String>());
	}

	public ServerEntry(String urlContains, Condition condition, String toReturn, Map<String, String> headersToReturn) {
		this._UrlContains = urlContains;
		this._Condition = condition;
		this._ToReturn = toReturn;
		this._HeadersToReturn = headersToReturn;
	}
	
	public boolean matches(String url, String postData) {
		return url.contains(this._UrlContains) && _Condition.matches(postData);
	}
	
	public void result(NetworkResponse callback) {
		try {
			URL res = Server.class.getResource("/" + _ToReturn);
			String toReturn = Resources.toString(res, Charsets.UTF_8);
			toReturn = toReturn.replaceAll("&\\s+", "&amp;");
			callback.callOnSuccess(200, toReturn, _HeadersToReturn);
		} catch (IOException e) {
			callback.callOnFail("err");
		}
	}
}
