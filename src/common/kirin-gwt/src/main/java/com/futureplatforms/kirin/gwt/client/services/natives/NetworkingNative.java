package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface NetworkingNative extends IKirinNativeService {
    public void retrieve(int id, String method, String url, String postData, String[][] headers);
}
