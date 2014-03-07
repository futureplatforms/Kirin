package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.NetworkingServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "NetworkingService", all = true)
@ExportPackage("screens")
public class NetworkingService extends KirinService<NetworkingServiceNative> {
    
    @Export
    @ExportClosure
    public static interface NetworkingSuccess extends Exportable {
        @Export
        public void onSuccess(int code, String headers, String payload);
    }
    
    @Export
    @ExportClosure
    public static interface NetworkingFailure extends Exportable {
        @Export
        public void onFail();
    }
    
    private static class NetworkingCB {
        public final NetworkingSuccess _Success;
        public final NetworkingFailure _Fail;
        public NetworkingCB(NetworkingSuccess success, NetworkingFailure fail) {
            this._Success = success; this._Fail = fail;
        }
    }
    
    public NetworkingService() {
        super(GWT.<NetworkingServiceNative>create(NetworkingServiceNative.class));
    }
    
    private Map<Integer, NetworkingCB> results = Maps.newHashMap();
    private int nextId = Integer.MIN_VALUE;

    // This is the method that KirinNetworking uses to talk to us
    @NoBind
    public void _retrieve(String method, String url, String postData, String[] headerKeys, String[] headerVals, NetworkingSuccess win, NetworkingFailure fail) {
        int thisId = nextId;
        nextId++;
        results.put(thisId, new NetworkingCB(win, fail));
        
        getNativeObject().retrieve(thisId, method, url, postData, headerKeys, headerVals);
    }
    
    // These are the methods that native uses to call back to us
    public void payload(int connId, String str) {
        StaticDependencies.getInstance().getLogDelegate().log("payload " + connId + ", " + str);
        results.remove(connId)._Success.onSuccess(200, null, str);
    }
    
    public void onError(int connId) {
        results.remove(connId)._Fail.onFail();
    }
}
