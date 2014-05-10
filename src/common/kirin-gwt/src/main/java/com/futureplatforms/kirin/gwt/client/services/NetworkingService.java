package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.NetworkingServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "NetworkingService", all = true)
@ExportPackage("screens")
public class NetworkingService extends KirinService<NetworkingServiceNative> {
    
    private static NetworkingService _Instance;
    
    @NoBind
    @NoExport
    public static NetworkingService BACKDOOR() { return _Instance; }
    
    public NetworkingService() {
        super(GWT.<NetworkingServiceNative>create(NetworkingServiceNative.class));
        _Instance = this;
    }
    
    private Map<Integer, NetworkResponse> results = Maps.newHashMap();
    private int nextId = Integer.MIN_VALUE;

    // This is the method that KirinNetworking uses to talk to us
    @NoBind
    public void _retrieve(String method, String url, String postData, String[] headerKeys, String[] headerVals, NetworkResponse cb) {
        int thisId = nextId;
        nextId++;
        results.put(thisId, cb);
        
        getNativeObject().retrieve(thisId, method, url, postData, headerKeys, headerVals);
    }
    
    private static Map<String, String> fromArrays(String[] keys, String[] vals) {
    	Map<String, String> map = Maps.newHashMap();
    	for (int i=0; i<keys.length; i++) {
    		String key = keys[i], val = vals[i];
    		map.put(key, val);
    	}
    	return map;
    }
    
    // These are the methods that native uses to call back to us
    public void payload(int connId, int respCode, String str, String[] headerKeys, String[] headerVals) {
        NetworkResponse resp = results.remove(connId);
        if (respCode >= 200 && respCode <= 299) {
        	resp.callOnSuccess(respCode, str, fromArrays(headerKeys, headerVals));
        } else {
        	resp.callOnFail(""+respCode);
        }
    }
    
    public void onError(int connId) {
        results.remove(connId).callOnFail("");
    }
}
