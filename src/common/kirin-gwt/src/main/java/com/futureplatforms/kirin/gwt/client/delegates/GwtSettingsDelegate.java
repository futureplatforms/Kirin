package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.futureplatforms.kirin.gwt.client.services.GwtSettingsService;

public class GwtSettingsDelegate implements SettingsDelegate {
    public String get(String key) {
        return GwtSettingsService.BACKDOOR()._get(key);
    }
    
    public void put(String key, String value) {
        if (value == null) {
            GwtSettingsService.BACKDOOR()._remove(key);
        } else {
            GwtSettingsService.BACKDOOR()._put(key, value);
        }
        GwtSettingsService.BACKDOOR()._commit();
    }
    
    public void clear() {
        GwtSettingsService.BACKDOOR()._clear();
    }
}
