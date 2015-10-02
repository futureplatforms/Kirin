package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface NetworkingServiceNative extends IKirinNativeService {
    void retrieve(int connId, String method, String url, String postData, String[] headerKeys, String[] headerVals);
    void retrieveB64(int connId, String method, String url, String postData, String[] headerKeys, String[] headerVals);
    void retrieveToken(int connId, String method, String url, String postData, String[] headerKeys, String[] headerVals);
}
