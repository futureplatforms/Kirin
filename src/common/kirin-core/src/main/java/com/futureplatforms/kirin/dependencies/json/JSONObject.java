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

	public int getInt(String key) {
		try {
			return (int)getDouble(key);
		} catch (Throwable t) {
			return -1;
		}
	}
}
