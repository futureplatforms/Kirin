package com.futureplatforms.kirin.dependencies.json;

import java.util.Iterator;

/**
 * 
 * Representation of a general JSON object listing name / value pairs and
 * written as { }.
 * Replicates the 20080701 version of org.json API
 * 
 * @author daniel
 * 
 */

public abstract class JSONObject {
	public static final Object NULL = new Object();

	public abstract boolean has(String key);

	public abstract boolean getBoolean(String key) throws JSONException;

	public abstract int getInt(String key) throws JSONException;

	public abstract double getDouble(String key) throws JSONException;

	public abstract String getString(String key) throws JSONException;

	public abstract JSONArray getJSONArray(String key) throws JSONException;

	public abstract JSONObject getJSONObject(String key) throws JSONException;

	public abstract boolean optBoolean(String key);
	public abstract boolean optBoolean(String key, boolean defVal);

	public abstract int optInt(String key);
	public abstract int optInt(String key, int defVal);

	public abstract double optDouble(String key);
	public abstract double optDouble(String key, double defVal);

	public abstract String optString(String key);
	public abstract String optString(String key, String defVal);

	public abstract JSONArray optJSONArray(String key);

	public abstract JSONObject optJSONObject(String key);

	public abstract boolean isNull(String key);

	public boolean isNullOrDoesntExist(String key) {
		return has(key) && isNull(key);
	}

	public abstract Iterator<String> keys();

	public abstract JSONObject put(String key, int value) throws JSONException;

	public abstract JSONObject put(String key, long value) throws JSONException;

	public abstract JSONObject put(String key, Object value) throws JSONException;

	public abstract JSONObject put(String key, boolean value) throws JSONException;

	public abstract JSONObject put(String key, double value) throws JSONException;

	public abstract int size();

	public abstract String toString();

	public abstract boolean equals(JSONObject other);

	public boolean safeGetBoolean(String key) {
		try {
			return Boolean.valueOf(getBoolean(key));
		} catch (Throwable t) {
			return false;
		}
	}

	public double safeGetDouble(String key) {
		try {
			return Double.valueOf(getDouble(key));
		} catch (Throwable t) {
			return 0d;
		}
	}

	public String safeGetString(String key) {
		try {
			return getString(key);
		} catch (Throwable t) {
			return null;
		}
	}

	public JSONObject safeGetObject(String key) {
		try {
			return getJSONObject(key);
		} catch (Throwable t) {
			return null;
		}
	}

	public JSONArray safeGetArray(String key) {
		try {
			return getJSONArray(key);
		} catch (Throwable t) {
			return null;
		}
	}

}
