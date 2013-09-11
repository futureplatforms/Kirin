package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface NetworkingServiceNative extends IKirinNativeService {
    public void retrieve(int ref, String method, String url, String postData, String[][] headers);
}
