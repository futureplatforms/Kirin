package com.futureplatforms.kirin.gwt.client.delegates;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;
import com.futureplatforms.kirin.gwt.client.KirinEP;
import com.google.gwt.core.client.JavaScriptObject;

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
            JavaScriptObject headersJS = KirinEP.createMap(headers);
            String[] keyArr = headers.keySet().toArray(new String[0]);
            String[] valArr = headers.values().toArray(new String[0]);
            NetworkRunner.doIt(method, url, keyArr, valArr, postData, this);
        }

        private static native void doIt(String method, String url, String[] headerKeys, String[] headerVals, String postData, NetworkRunner p) /*-{
            console.log("networking.doIt(" + url + ")");
            var networking = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("NetworkingService");
            console.log("networking is " + networking);
            for (var i in networking) {
                console.log("networking." + i + " = " + networking[i]);
            }
            var callback = function(code, headers, payload) {
                console.log("callback! " + payloaad);
            };
            networking._retrieve(method, url, postData, headerKeys, headerVals, callback);
        }-*/;
    }
    
    @Override
    public void doHttp(NetworkDelegate.HttpVerb verb, String url, String payload, Map<String, String> headers, NetworkResponse callback) {
        new NetworkRunner(callback).doIt(verb.toString(), url, headers, payload);
    }
}
