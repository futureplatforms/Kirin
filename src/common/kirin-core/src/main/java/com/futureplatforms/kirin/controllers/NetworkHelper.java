package com.futureplatforms.kirin.controllers;

import java.util.HashMap;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.TimerTask;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;

public class NetworkHelper {
    
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
        final TimerTask tt = new TimerTask(_Dependencies) {

            @Override
            public void run() {
                _Cancelled = true;
                callback.onFail("timeout");
            }
            
        };
        tt.schedule(30000);
        
        _Dependencies.getNetworkDelegate().doHttp(verb, url, payload, headers, new NetworkResponse() {
            
            public void onSuccess(int res, String result, Map<String, String> headers) {
                if (!_Cancelled) {
                    callback.onSuccess(res, result, headers);
                }
            }
            
            public void onFail(String code) {
                if (!_Cancelled) {
                    callback.onFail(code);
                }
            }
        });
    }
    
    public final void cancel() {
        _Cancelled = true;
    }
}
