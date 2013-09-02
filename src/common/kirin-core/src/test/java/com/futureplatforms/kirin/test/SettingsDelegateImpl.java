package com.futureplatforms.kirin.test;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.google.common.collect.Maps;

public class SettingsDelegateImpl implements SettingsDelegate {
    private Map<String, String> _Map;
    public SettingsDelegateImpl() {
        _Map = Maps.newHashMap();
    }
    public String get(String key) {
        try {
            return _Map.get(key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void put(String key, String value) {
        _Map.put(key, value);
    }

}
