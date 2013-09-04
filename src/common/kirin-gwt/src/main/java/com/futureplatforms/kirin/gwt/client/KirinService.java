package com.futureplatforms.kirin.gwt.client;

import com.futureplatforms.kirin.KirinModule;

public class KirinService<T extends IKirinNativeService> extends KirinModule<T> {

    protected KirinService(T nativeObject) {
        super(nativeObject);
    }

}
