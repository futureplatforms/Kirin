package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

/**
 * Created by douglashoskins on 27/07/2015.
 */
public interface GwtSettingsServiceNative extends IKirinNativeService {
    void updateContents(String[] addKeys, String[] addVals, String[] deletes);
    void clear();
}
