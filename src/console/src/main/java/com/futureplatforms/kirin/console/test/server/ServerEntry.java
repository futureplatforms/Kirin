package com.futureplatforms.kirin.console.test.server;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ServerEntry {

	public interface Condition {
		boolean matches(String postData);
	}
	
	private final String _UrlContains, _ToReturn;
	private final Condition _Condition;
	
	public ServerEntry(String urlContains, String toReturn) {
		this(urlContains, new Condition() {
			
			@Override
			public boolean matches(String postData) {
				return true;
			}
		}, toReturn);
	}
	
	public ServerEntry(String urlContains, Condition condition, String toReturn) {
		this._UrlContains = urlContains;
		this._Condition = condition;
		this._ToReturn = toReturn;
	}
	
	public boolean matches(String url, String postData) {
		return url.contains(this._UrlContains) && _Condition.matches(postData);
	}
	
	public void result(NetworkResponse callback) {
		try {
			URL res = Server.class.getResource("/" + _ToReturn);
			String toReturn = Resources.toString(res, Charsets.UTF_8);
			toReturn = toReturn.replaceAll("&\\s+", "&amp;");
			callback.callOnSuccess(200, toReturn, new HashMap<String, String>());
		} catch (IOException e) {
			callback.callOnFail("err");
		}
	}
}
