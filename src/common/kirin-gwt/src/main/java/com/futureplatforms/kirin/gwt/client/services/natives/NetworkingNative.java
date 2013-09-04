package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.IKirinNativeObject;

public interface NetworkingNative extends IKirinNativeObject {
    public void retrieve(int id, String method, String url, String postData, String[][] headers);
}
