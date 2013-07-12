package com.futureplatforms.kirin.dependencies.json;

import java.util.Iterator;


/**
 * 
 * Representation of a general JSON object listing name / value pairs and
 * written as { }.
 * 
 * @author daniel
 *
 */

public abstract class JSONObject {
    public static final Object NULL = new Object();
    public abstract boolean has(String key);
    public abstract boolean getBoolean(String key);
    public abstract double getDouble(String key);
    public abstract String getString(String key);
    public abstract boolean isNull(String key);
    public boolean isNullOrDoesntExist(String key) {
        return has(key) && isNull(key);
    }
    public abstract JSONArray getJSONArray(String key);
    public abstract JSONObject getJSONObject(String key);
    public abstract Iterator<String> keys();
    public abstract JSONObject put(String key, int value);
    public abstract JSONObject put(String key, long value);
    public abstract JSONObject put(String key, Object value);
    public abstract JSONObject put(String key, boolean value);
    public abstract JSONObject put(String key, double value);
    public abstract int size();
    public abstract String toString();
    public abstract boolean equals(JSONObject other);
    
    
    public Boolean safeGetBoolean(String key) {
        return Boolean.valueOf(getBoolean(key));
    }
    
    public Double safeGetDouble(String key) {
        return Double.valueOf(getDouble(key));
    }
    
    public String safeGetString(String key) {
        return getString(key);
    }
    
    public JSONObject safeGetObject(String key) {
        return getJSONObject(key);
    }
    
    public JSONArray safeGetArray(String key) {
        return getJSONArray(key);
    }
}
