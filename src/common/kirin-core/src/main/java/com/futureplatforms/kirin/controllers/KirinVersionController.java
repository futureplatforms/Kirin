package com.futureplatforms.kirin.controllers;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;

public class KirinVersionController {

    private SettingsDelegate _Settings;
    private static final String VERSION_SETTING = "version_controller_VERSION";
    
    public KirinVersionController() {
        _Settings = StaticDependencies.getInstance().getSettingsDelegate();
    }
    
    public KirinVersionController(SettingsDelegate settings) {
        _Settings = settings;
    }
    
    public static interface KirinVersionDelegate {
        public void firstRun();
        public void upgraded(String lastVer);
    }
    
    public void currentVersion(String version, KirinVersionDelegate delegate) {
        String lastVersion = _Settings.get(VERSION_SETTING);
        _Settings.put(VERSION_SETTING, version);
        if (lastVersion == null) {
            delegate.firstRun();
        } else if (!lastVersion.equals(version)) {
            delegate.upgraded(lastVersion);
        }
    }
}
