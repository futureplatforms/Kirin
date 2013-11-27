package com.futureplatforms.kirin.gwt.client.delegates;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;

public class KirinNetworking implements NetworkDelegateClient {
    private static class NetworkRunner {
        private NetworkResponse mCB;
        public NetworkRunner(NetworkResponse cb) {
            mCB = cb;
        }
        
        public void payload(String s) {
            try {
                mCB.onSuccess(200, s, null);
            } catch (Throwable t) {
                mCB.onFail(t.getLocalizedMessage());
            }
        }
        
        public void onError(String s) {
            mCB.onFail(s);
        }
        
        public void doIt(String method, String url, Map<String, String> headers, String postData) {
            String[] keyArr = headers.keySet().toArray(new String[0]);
            String[] valArr = headers.values().toArray(new String[0]);
            NetworkRunner.doIt(method, url, keyArr, valArr, postData, this);
        }

        private static native void doIt(String method, String url, String[] headerKeys, String[] headerVals, String postData, NetworkRunner p) /*-{
            var networking = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("NetworkingService");
            
            // This closure is an "instance" of NetworkingSuccess
            var success = function(payload) {
            	var fn = p.@com.futureplatforms.kirin.gwt.client.delegates.KirinNetworking$NetworkRunner::payload(Ljava/lang/String;);
                fn(payload);
            };
            
            // This closure is an "instance" of NetworkingFailure
            var failure = function(err) {
            	var fn = p.@com.futureplatforms.kirin.gwt.client.delegates.KirinNetworking$NetworkRunner::onError(Ljava/lang/String;);
                fn(err);
            };
            
            networking._retrieve(method, url, postData, headerKeys, headerVals, success, failure);
        }-*/;
    }
    
    @Override
    public void doHttp(NetworkDelegate.HttpVerb verb, String url, String payload, Map<String, String> headers, NetworkResponse callback) {
        new NetworkRunner(callback).doIt(verb.toString(), url, headers, payload);
    }
}
