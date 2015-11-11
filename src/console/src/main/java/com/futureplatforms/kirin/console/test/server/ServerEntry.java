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
	
	private final String _UrlEndsWith, _XmlReturn;
	private final Condition _Condition;
	
	public ServerEntry(String urlEndsWith, String xmlReturn) {
		this(urlEndsWith, new Condition() {
			
			@Override
			public boolean matches(String postData) {
				return true;
			}
		}, xmlReturn);
	}
	
	public ServerEntry(String urlEndsWith, Condition condition, String xmlReturn) {
		this._UrlEndsWith = urlEndsWith;
		this._Condition = condition;
		this._XmlReturn = xmlReturn;
	}
	
	public boolean matches(String url, String postData) {
		return url.contains(this._UrlEndsWith) && _Condition.matches(postData);
	}
	
	public void result(NetworkResponse callback) {
		try {
			URL res = Server.class.getResource("/" + _XmlReturn);
			String inputXML = Resources.toString(res, Charsets.UTF_8);
			inputXML = inputXML.replaceAll("&\\s+", "&amp;");
			callback.callOnSuccess(200, inputXML, new HashMap<String, String>());
		} catch (IOException e) {
			callback.callOnFail("err");
		}
	}
}
