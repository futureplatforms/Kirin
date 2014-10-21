package com.futureplatforms.kirin.controllers;

import java.util.HashMap;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkFailType;
import com.futureplatforms.kirin.dependencies.TimerTask;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;

public final class NetworkHelper {
    
    private final StaticDependencies _Dependencies;
    private boolean _Cancelled = false;
    
    public NetworkHelper(StaticDependencies dependencies) {
        this._Dependencies = dependencies;
    }

    public final void execute(HttpVerb verb, String url, NetworkResponse callback) {
        execute(verb, url, null, new HashMap<String, String>(), callback);
    }
    
    public final void execute(HttpVerb verb, String url, String payload, NetworkResponse callback) {
        execute(verb, url, payload, new HashMap<String, String>(), callback);
    }
    
    public final void execute(HttpVerb verb, String url, String payload, Map<String, String> headers, final NetworkResponse callback) {
        final TimerTask tt = new TimerTask() {

            @Override
            public void run() {
            	if (!_Cancelled) {
            		_Cancelled = true;
            		callback.callOnFail("timeout");
            	}
            }
            
        };
        tt.schedule(30000);
        _Dependencies.getNetworkDelegate().doHttp(verb, url, payload, headers, new NetworkResponse() {
            
            public void onSuccess(int res, String result, Map<String, String> headers) {
            	tt.cancel();
                if (!_Cancelled) {
                    callback.callOnSuccess(res, result, headers);
                }
            }
            
            public void onFail(String code) {
            	tt.cancel();
                if (!_Cancelled) {
                    callback.callOnFail(code);
                }
            }

			@Override
			protected void onFailWithStatus(String code,
					NetworkFailType failType) {
            	tt.cancel();
                if (!_Cancelled) 
                {
                    callback.callOnFail(code);
                }
			}

        });
    }
    
    public final void cancel() {
        _Cancelled = true;
    }
}
