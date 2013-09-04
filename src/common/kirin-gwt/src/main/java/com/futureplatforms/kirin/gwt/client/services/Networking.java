package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.futureplatforms.kirin.KirinModule;
import com.futureplatforms.kirin.gwt.client.services.natives.NetworkingNative;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "Networking", all = true)
@ExportPackage("services")
public class Networking extends KirinModule<NetworkingNative> {
    
    @Export
    @ExportClosure
    public static interface NetworkingResult extends Exportable {
        public void result(int code, String[][] headers, String payload);
        public void error(int code);
    }
    
    public Networking() {
        super(GWT.<NetworkingNative>create(NetworkingNative.class));
    }
    
    private Map<Integer, NetworkingResult> results = Maps.newHashMap();
    private int nextId = Integer.MIN_VALUE;

    // This is the method that KirinNetworking uses to talk to us
    public void _retrieve(String method, String url, String postData, String[][] headers, NetworkingResult res) {
        int id = nextId;
        nextId++;
        results.put(id, res);
        getNativeObject().retrieve(id, method, url, postData, headers);
    }
    
    // These are the methods that native uses to call back to us
    public void payload(int id, String str) {
        results.remove(id).result(200, null, str);
    }
    
    public void onError(int id, int code) {
        
    }
}
