package com.futureplatforms.kirin.dependencies;

import java.util.HashMap;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.TimerTask.TimerDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.xml.parser.XMLParser;

public final class StaticDependencies {
    public enum Configuration { Debug, Release } ;
    
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
    public static abstract class NetworkDelegate {
        public static enum HttpVerb { GET, POST, PUT };
        public static interface NetworkResponse {
            public void onSuccess(int res, String result, Map<String, String> headers);
            public void onFail(String code);
        }
        
        public final void doHttp(HttpVerb verb, String url, NetworkResponse callback) {
            doHttp(verb, url, null, new HashMap<String, String>(), callback);
        }
        
        public final void doHttp(HttpVerb verb, String url, String payload, NetworkResponse callback) {
            doHttp(verb, url, payload, new HashMap<String, String>(), callback);
        }
        
        public abstract void doHttp(HttpVerb verb, String url, String payload, Map<String, String> headers, NetworkResponse callback);
    }

    private LogDelegate mLogDelegate;
    private SettingsDelegate mSettingsDelegate;
    private TimerDelegate mTimerDelegate;
    private LocationDelegate mLocationDelegate;
    private JSONDelegate mJsonDelegate;
    private NetworkDelegate mNetworkDelegate;
    private XMLParser mXmlParser;
    private Formatter mFormatter;
    private boolean mInitialised;
    private Configuration _Profile;
    
    public LogDelegate getLogDelegate() { return mLogDelegate; }
    public SettingsDelegate getSettingsDelegate() { return mSettingsDelegate; }
    protected TimerDelegate getTimerDelegate() { return mTimerDelegate; }
    public LocationDelegate getLocationDelegate() { return mLocationDelegate; }
    public NetworkDelegate getNetworkDelegate() { return mNetworkDelegate; }
    public JSONDelegate getJsonDelegate() { return mJsonDelegate; }
    public XMLParser getXmlParser() { return mXmlParser; }
    public Formatter getFormatter() { return mFormatter; }
    public boolean initialised() { return mInitialised; }
    public Configuration getProfile() { return _Profile; }
    
    public void setDependencies(    LogDelegate logDelegate,
                                    SettingsDelegate settingsDelegate,
                                    TimerDelegate timerDelegate,
                                    LocationDelegate locationDelegate, 
                                    NetworkDelegate networkDelegate,
                                    JSONDelegate jsonDelegate,
                                    XMLParser xmlParser, 
                                    Formatter formatter, 
                                    Configuration profile) {
        this.mLogDelegate = logDelegate;
        this.mSettingsDelegate = settingsDelegate;
        this.mTimerDelegate = timerDelegate;
        this.mLocationDelegate = locationDelegate;
        this.mNetworkDelegate = networkDelegate;
        this.mJsonDelegate = jsonDelegate;
        this.mXmlParser = xmlParser;
        this.mFormatter = formatter;
        this._Profile = profile;
        this.mInitialised = true;
    }
}
