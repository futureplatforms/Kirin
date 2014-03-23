package com.futureplatforms.kirin.gwt.client.delegates.json;

import java.util.Iterator;

import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Replicates the 20080701 version of org.json API
 * 
 */

public class GwtJSONObject extends
		com.futureplatforms.kirin.dependencies.json.JSONObject {

	private JSONObject jsonObj;

	public GwtJSONObject(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	public GwtJSONObject(String jsonString) {
		jsonObj = JSONParser.parseStrict(jsonString).isObject();
	}

	public GwtJSONObject() {
		jsonObj = new JSONObject();
	}

	@Override
	public boolean has(String key) {
		return jsonObj.containsKey(key);
	}

	@Override
	public boolean isNull(String key) {
		JSONValue value = opt(key);
		return value == null || value instanceof JSONNull;
	}

	@Override
	public boolean getBoolean(String key) throws JSONException {
		JSONValue o = get(key);
		if (o instanceof JSONBoolean) {
			return ((JSONBoolean) o).booleanValue();
		}
		String val = o.toString();
        if (val.equalsIgnoreCase("false")) {
            return false;
        } else if (val.equalsIgnoreCase("true")) {
            return true;
        }
		throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a Boolean.");
	}

	@Override
	public double getDouble(String key) throws JSONException {
		JSONValue o = get(key);
		try {
			return ((JSONNumber) o).doubleValue();
		} catch (Exception e) {
			throw new JSONException("JSONObject[" + quote(key)
					+ "] is not a number.");
		}
	}

	@Override
	public int getInt(String key) throws JSONException {
		return (int) getDouble(key);
	}

	@Override
	public String getString(String key) throws JSONException {
		return get(key).toString();
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray getJSONArray(
			String key) throws JSONException {
		JSONValue o = get(key);
		if (o instanceof JSONArray) {
			return new GwtJSONArray((JSONArray) o);
		}
		throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a JSONArray.");
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(
			String key) throws JSONException {
		JSONValue o = get(key);
		if (o instanceof JSONObject) {
			return new GwtJSONObject((JSONObject) o);
		}
		throw new JSONException("JSONObject[" + quote(key)
				+ "] is not a JSONObject.");
	}

	@Override
	public Iterator<String> keys() {
		return jsonObj.keySet().iterator();
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, int value) {
		jsonObj.put(key, new JSONNumber(value));
		return this;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, long value) {
		jsonObj.put(key, new JSONNumber(value));
		return this;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, Object value) {
		if (value instanceof Integer) {
			return this.put(key, ((Integer) value).intValue());
		} else if (value instanceof Boolean) {
			return this.put(key, ((Boolean) value).booleanValue());
		} else if (value instanceof Long) {
			return this.put(key, ((Long) value).longValue());
		} else if (value instanceof Double) {
			return this.put(key, ((Double) value).doubleValue());
		} else if (value instanceof String) {
			jsonObj.put(key, new JSONString((String) value));
		} else if (value instanceof GwtJSONObject) {
			jsonObj.put(key, ((GwtJSONObject) value).getNativeJSONObject());
		} else if (value instanceof GwtJSONArray) {
			jsonObj.put(key, ((GwtJSONArray) value).getNativeJSONArray());
		} else if (value == null) {
			jsonObj.put(key, JSONNull.getInstance());
		}

		return this;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, boolean value) {
		jsonObj.put(key, JSONBoolean.getInstance(value));
		return this;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, double value) {
		jsonObj.put(key, new JSONNumber(value));
		return this;
	}

	@Override
	public int size() {
		return jsonObj.size();
	}

	@Override
	public String toString() {
		return jsonObj.toString();
	}

	@Override
	public boolean equals(
			com.futureplatforms.kirin.dependencies.json.JSONObject other) {
		return jsonObj.equals(((GwtJSONObject) other).getNativeJSONObject());
	}

	@Override
	public int hashCode() {
		return jsonObj.hashCode();
	}

	protected JSONObject getNativeJSONObject() {
		return jsonObj;
	}

	@Override
	public boolean optBoolean(String key) {
		return optBoolean(key, false);
	}

	@Override
	public boolean optBoolean(String key, boolean defaultValue) {
		try {
			return getBoolean(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public int optInt(String key) {
		return (int) optDouble(key, 0);
	}

	@Override
	public int optInt(String key, int defaultValue) {
		try {
			return (int) getDouble(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public double optDouble(String key) {
		return optDouble(key, Double.NaN);
	}

	@Override
	public double optDouble(String key, double defaultValue) {
		try {
			Object o = opt(key);
			return o instanceof JSONNumber ? ((JSONNumber) o).doubleValue()
					: new Double((String) o).doubleValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public String optString(String key) {
		return optString(key, "");
	}

	@Override
	public String optString(String key, String defaultValue) {
		Object o = opt(key);
		return o != null ? o.toString() : defaultValue;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray optJSONArray(
			String key) {
		Object o = opt(key);
		return o instanceof JSONArray ? new GwtJSONArray((JSONArray) o) : null;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject optJSONObject(
			String key) {
		Object o = opt(key);
		return o instanceof JSONObject ? new GwtJSONObject((JSONObject) o)
				: null;
	}

	/**
	 * Get the value object associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return The object associated with the key.
	 * @throws JSONException
	 *             if the key is not found.
	 */
	private JSONValue get(String key) throws JSONException {
		JSONValue o = opt(key);
		if (o == null) {
			throw new JSONException("JSONObject[" + quote(key) + "] not found.");
		}
		return o;
	}

	/**
	 * Get an optional value associated with a key.
	 * 
	 * @param key
	 *            A key string.
	 * @return An object which is the value, or null if there is no value.
	 */
	private JSONValue opt(String key) {
		return key == null ? null : this.jsonObj.get(key);
	}

	/**
	 * Produce a string in double quotes with backslash sequences in all the
	 * right places. A backslash will be inserted within </, allowing JSON text
	 * to be delivered in HTML. In JSON text, a string cannot contain a control
	 * character or an unescaped quote or backslash.
	 * 
	 * @param string
	 *            A String
	 * @return A String correctly formatted for insertion in a JSON text.
	 */
	public static String quote(String string) {
		if (string == null || string.length() == 0) {
			return "\"\"";
		}

		char b;
		char c = 0;
		int i;
		int len = string.length();
		StringBuffer sb = new StringBuffer(len + 4);
		String t;

		sb.append('"');
		for (i = 0; i < len; i += 1) {
			b = c;
			c = string.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
						|| (c >= '\u2000' && c < '\u2100')) {
					t = "000" + Integer.toHexString(c);
					sb.append("\\u" + t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
			}
		}
		sb.append('"');
		return sb.toString();
	}
}
