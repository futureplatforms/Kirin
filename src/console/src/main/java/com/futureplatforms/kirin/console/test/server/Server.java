package com.futureplatforms.kirin.console.test.server;

import com.futureplatforms.kirin.console.ConsoleNetwork;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server implements NetworkDelegateClient {

	public final List<ServerEntry> _Entries = new ArrayList<>();

	public Server() {}
	
	private boolean checkMap(String url, String postData, NetworkResponse callback) {
		ServerEntry lastMatch = null;
		for (ServerEntry entry : _Entries) {
			if (entry.matches(url, postData)) {
				lastMatch = entry;
			}
		}
		if (lastMatch != null) {
			lastMatch.result(callback);
			return true;
		}
		return false;
	}

	private enum TestServerNetworkType { FullyOffline, FallbackToOnline }

	private TestServerNetworkType networkType = TestServerNetworkType.FullyOffline;

	public void fallbackToOnline() {
		networkType = TestServerNetworkType.FallbackToOnline;
	}

	@Override
	public void doHttp(HttpVerb verb, String url, String payload,
			Map<String, String> headers, NetworkResponse callback) {
		if (!checkMap(url, payload, callback)) {
			System.out.println("Can't find:");
			System.out.println(verb.name());
			System.out.println(url);
			System.out.println(payload);
			if (networkType == TestServerNetworkType.FallbackToOnline) {
				new ConsoleNetwork().doHttp(verb, url, payload, headers, callback);
			} else {
				callback.callOnFail("404");
			}
		}
	}

	@Override
	public void doHttpWithBase64Return(HttpVerb verb, String url,
			String payload, Map<String, String> headers,
			NetworkResponse callback) {

	}

	@Override
	public void doHttpWithTokenReturn(HttpVerb httpVerb, String s, String s1, Map<String, String> map, NetworkResponse networkResponse) {

	}

}
