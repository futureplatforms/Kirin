package com.futureplatforms.kirin.dependencies;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.TimerTask.TimerDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;

public final class StaticDependencies {
    private static StaticDependencies instance;
    public static StaticDependencies getInstance() {
        if (instance == null) { instance = new StaticDependencies(); }
        return instance;
    }
    public static interface LogDelegate {
        public void log(String s);
    }
    public static interface SettingsDelegate {
        public String get(String key);
        public void put(String key, String value);
    }
    public static interface LocationResponse {
        public void onSuccess(double lat, double lng);
        public void onFail(String errDesc);
    }
    public static interface NetworkDelegate {
        public static enum HttpVerb { GET, POST, PUT };
        public static interface NetworkResponse {
            public void onSuccess(int res, String result, Map<String, String> headers);
            public void onFail(String code);
        }
        public void doHttp(HttpVerb verb, String url, String payload, Map<String, String> headers, NetworkResponse callback);
    }

    private LogDelegate mLogDelegate;
    private SettingsDelegate mSettingsDelegate;
    private TimerDelegate mTimerDelegate;
    private LocationDelegate mLocationDelegate;
    private JSONDelegate mJsonDelegate;
    private NetworkDelegate mNetworkDelegate;
    private boolean mInitialised;
    
    public LogDelegate getLogDelegate() { return mLogDelegate; }
    public SettingsDelegate getSettingsDelegate() { return mSettingsDelegate; }
    protected TimerDelegate getTimerDelegate() { return mTimerDelegate; }
    public LocationDelegate getLocationDelegate() { return mLocationDelegate; }
    public NetworkDelegate getNetworkDelegate() { return mNetworkDelegate; }
    public JSONDelegate getJsonDelegate() { return mJsonDelegate; }
    
    public boolean initialised() { return mInitialised; }
    
    public void setDependencies(    LogDelegate logDelegate,
                                    SettingsDelegate settingsDelegate,
                                    TimerDelegate timerDelegate,
                                    LocationDelegate locationDelegate, 
                                    NetworkDelegate networkDelegate,
                                    JSONDelegate jsonDelegate) {
        this.mLogDelegate = logDelegate;
        this.mSettingsDelegate = settingsDelegate;
        this.mTimerDelegate = timerDelegate;
        this.mLocationDelegate = locationDelegate;
        this.mNetworkDelegate = networkDelegate;
        this.mJsonDelegate = jsonDelegate;
        this.mInitialised = true;
    }
}
