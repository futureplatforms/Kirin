package com.futureplatforms.kirin.gwt.client.delegates.net;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;
import com.futureplatforms.kirin.gwt.client.services.NetworkingService;

public class GwtNetworking implements NetworkDelegateClient {

	@Override
	public void doHttp(HttpVerb verb, String url, String payload,
			Map<String, String> headers, NetworkResponse callback) {
		NetworkingService.BACKDOOR()._retrieve(
				verb.name(), 
				url, 
				payload, 
				headers.keySet().toArray(new String[0]), 
				headers.values().toArray(new String[0]), 
				callback);
		
	}

}
