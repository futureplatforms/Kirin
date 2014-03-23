package com.futureplatforms.kirin.dependencies.json;

import java.util.Iterator;

/**
 * 
 * Representation of a general JSON object listing name / value pairs and
 * written as { }. Replicates the 20080701 version of org.json API
 * 
 * @author daniel
 * 
 */

public abstract class JSONObject {
	public static final Object NULL = new Object();

	public abstract boolean has(String key);

	/**
	 * Get the boolean value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return The truth.
	 * @throws JSONException
	 *             if the value is not a Boolean or the String "true" or
	 *             "false".
	 */
	public abstract boolean getBoolean(String key) throws JSONException;

	/**
	 * Get the double value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return The numeric value.
	 * @throws JSONException
	 *             if the key is not found or if the value is not a Number
	 *             object and cannot be converted to a number.
	 */
	public abstract double getDouble(String key) throws JSONException;

	/**
	 * Get the int value associated with a key. If the number value is too large
	 * for an int, it will be clipped.
	 * 
	 * @param key
	 *            A key string.
	 * @return The integer value.
	 * @throws JSONException
	 *             if the key is not found or if the value cannot be converted
	 *             to an integer.
	 */
	public abstract int getInt(String key) throws JSONException;

	/**
	 * Get the string associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return A string which is the value.
	 * @throws JSONException
	 *             if the key is not found.
	 */
	public abstract String getString(String key) throws JSONException;

	/**
	 * Get the JSONArray value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return A JSONArray which is the value.
	 * @throws JSONException
	 *             if the key is not found or if the value is not a JSONArray.
	 */
	public abstract JSONArray getJSONArray(String key) throws JSONException;

	/**
	 * Get the JSONObject value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return A JSONObject which is the value.
	 * @throws JSONException
	 *             if the key is not found or if the value is not a JSONObject.
	 */
	public abstract JSONObject getJSONObject(String key) throws JSONException;

	/**
	 * Get an optional boolean associated with a key. It returns false if there
	 * is no such key, or if the value is not Boolean.TRUE or the String "true".
	 * 
	 * @param key
	 *            A key string.
	 * @return The truth.
	 */
	public abstract boolean optBoolean(String key);

	/**
	 * Get an optional boolean associated with a key. It returns the
	 * defaultValue if there is no such key, or if it is not a Boolean or the
	 * String "true" or "false" (case insensitive).
	 * 
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return The truth.
	 */
	public abstract boolean optBoolean(String key, boolean defVal);

	/**
	 * Get an optional int value associated with a key, or zero if there is no
	 * such key or if the value is not a number. If the value is a string, an
	 * attempt will be made to evaluate it as a number.
	 * 
	 * @param key
	 *            A key string.
	 * @return An object which is the value.
	 */
	public abstract int optInt(String key);

	/**
	 * Get an optional int value associated with a key, or the default if there
	 * is no such key or if the value is not a number. If the value is a string,
	 * an attempt will be made to evaluate it as a number.
	 * 
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return An object which is the value.
	 */
	public abstract int optInt(String key, int defVal);

	/**
	 * Get an optional double associated with a key, or NaN if there is no such
	 * key or if its value is not a number. If the value is a string, an attempt
	 * will be made to evaluate it as a number.
	 * 
	 * @param key
	 *            A string which is the key.
	 * @return An object which is the value.
	 */
	public abstract double optDouble(String key);

	/**
	 * Get an optional double associated with a key, or the defaultValue if
	 * there is no such key or if its value is not a number. If the value is a
	 * string, an attempt will be made to evaluate it as a number.
	 * 
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return An object which is the value.
	 */
	public abstract double optDouble(String key, double defVal);

	/**
	 * Get an optional string associated with a key. It returns an empty string
	 * if there is no such key. If the value is not a string and is not null,
	 * then it is coverted to a string.
	 * 
	 * @param key
	 *            A key string.
	 * @return A string which is the value.
	 */
	public abstract String optString(String key);

	/**
	 * Get an optional string associated with a key. It returns the defaultValue
	 * if there is no such key.
	 * 
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return A string which is the value.
	 */
	public abstract String optString(String key, String defVal);

	/**
	 * Get an optional JSONArray associated with a key. It returns null if there
	 * is no such key, or if its value is not a JSONArray.
	 * 
	 * @param key
	 *            A key string.
	 * @return A JSONArray which is the value.
	 */
	public abstract JSONArray optJSONArray(String key);

	/**
	 * Get an optional JSONObject associated with a key. It returns null if
	 * there is no such key, or if its value is not a JSONObject.
	 * 
	 * @param key
	 *            A key string.
	 * @return A JSONObject which is the value.
	 */
	public abstract JSONObject optJSONObject(String key);

	/**
	 * Determine if the value associated with the key is null or if there is no
	 * value.
	 * 
	 * @param key
	 *            A key string.
	 * @return true if there is no value associated with the key or if the value
	 *         is the JSONObject.NULL object.
	 */
	public abstract boolean isNull(String key);

	public boolean isNullOrDoesntExist(String key) {
		return has(key) && isNull(key);
	}

	public abstract Iterator<String> keys();

	public abstract JSONObject put(String key, int value) throws JSONException;

	public abstract JSONObject put(String key, long value) throws JSONException;

	public abstract JSONObject put(String key, Object value)
			throws JSONException;

	public abstract JSONObject put(String key, boolean value)
			throws JSONException;

	public abstract JSONObject put(String key, double value)
			throws JSONException;

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
