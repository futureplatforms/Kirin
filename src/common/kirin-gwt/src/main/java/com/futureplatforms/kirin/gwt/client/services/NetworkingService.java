package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.NetworkingServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "NetworkingService", all = true)
@ExportPackage("screens")
public class NetworkingService extends KirinService<NetworkingServiceNative> {
    
    public NetworkingService() {
        super(GWT.<NetworkingServiceNative>create(NetworkingServiceNative.class));
    }
    
    private Map<Integer, NetworkingResult> results = Maps.newHashMap();
    private int nextId = Integer.MIN_VALUE;

    // This is the method that KirinNetworking uses to talk to us
    @NoBind
    public void _retrieve(String method, String url, String postData, String[] headerKeys, String[] headerVals, NetworkingResult res) {
        int id = nextId;
        nextId++;
        results.put(id, res);
        
        if (getNativeObject() == null) {
            StaticDependencies.getInstance().getLogDelegate().log("native object is null");
        } else {
            getNativeObject().retrieve(id, method, url, postData, headerKeys, headerVals);
        }
    }
    
    @NoBind
    public void _tryThis(String hey) {
        StaticDependencies.getInstance().getLogDelegate().log(hey);
    }
    
    // These are the methods that native uses to call back to us
    public void payload(int id, String str) {
        StaticDependencies.getInstance().getLogDelegate().log("payload " + id + ", " + str);
        results.remove(id).result(200, null, str);
    }
    
    public void onError(int id, int code) {
        
    }
}
