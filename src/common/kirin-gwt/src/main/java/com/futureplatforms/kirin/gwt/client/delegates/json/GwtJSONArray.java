package com.futureplatforms.kirin.gwt.client.delegates.json;

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
public class GwtJSONArray extends
		com.futureplatforms.kirin.dependencies.json.JSONArray {

	private JSONArray jsonArray;

	public GwtJSONArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public GwtJSONArray(String jsonText) throws JSONException {
		try{
			this.jsonArray = JSONParser.parseStrict(jsonText).isArray();
		} catch (Exception e) {
			throw new JSONException(e);
		}
	}

	public GwtJSONArray() {
		this.jsonArray = new JSONArray();
	}


	@Override
	public boolean isNull(int index) {
		JSONValue value = opt(index);
		return value == null || value instanceof JSONNull;
	}

	@Override
	public boolean getBoolean(int index) throws JSONException {
		Object o = get(index);
		if (o instanceof JSONBoolean) {
			return ((JSONBoolean) o).booleanValue();
		}
		String val = o.toString();
        if (val.equalsIgnoreCase("false")) {
            return false;
        } else if (val.equalsIgnoreCase("true")) {
            return true;
        }
		throw new JSONException("JSONArray[" + index + "] is not a Boolean.");
	}

	@Override
	public int getInt(int index) throws JSONException {
		return (int) getDouble(index);
	}

	@Override
	public double getDouble(int index) throws JSONException {
		Object o = get(index);
		try {
			return o instanceof JSONNumber ? ((JSONNumber) o).doubleValue()
					: Double.valueOf((String) o).doubleValue();
		} catch (Exception e) {
			throw new JSONException("JSONArray[" + index + "] is not a number.");
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray getJSONArray(
			int index) throws JSONException {
		Object o = get(index);
		if (o instanceof JSONArray) {
			return new GwtJSONArray((JSONArray) o);
		}
		throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
	}

    public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(int index) throws JSONException {
        Object o = get(index);
        if (o instanceof JSONObject) {
			return new GwtJSONObject((JSONObject) o);
        }
        throw new JSONException("JSONArray[" + index +
            "] is not a JSONObject.");
    }
	@Override
	public String getString(int index) throws JSONException {
		JSONValue o = get(index);
		if(o instanceof JSONString) return ((JSONString) o).stringValue();
		else return o.toString();
	}

	@Override
	public int length() {
		return jsonArray.size();
	}

	@Override
	public String toString() {
		return jsonArray.toString();
	}

	protected JSONArray getNativeJSONArray() {
		return jsonArray;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray putBoolean(
			boolean b) {
		return putBoolean(length(), b);
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray putDouble(
			double d) {
		return putDouble(length(), d);
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray putObject(
			Object o) {
		return putObject(length(), o);
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray putBoolean(
			int index, boolean b) {
		jsonArray.set(index, JSONBoolean.getInstance(b));
		return this;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray putDouble(
			int index, double d) {
		jsonArray.set(index, new JSONNumber(d));
		return this;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray putObject(
			int index, Object value) {
		if (value instanceof Integer) {
			return this.putDouble(index, ((Integer) value).intValue());
		} else if (value instanceof Boolean) {
			return this.putBoolean(index, ((Boolean) value).booleanValue());
		} else if (value instanceof Long) {
			return this.putDouble(index, ((Long) value).longValue());
		} else if (value instanceof Double) {
			return this.putDouble(index, ((Double) value).doubleValue());
		} else if (value instanceof String) {
			jsonArray.set(index, new JSONString((String) value));
		} else if (value instanceof GwtJSONObject) {
			jsonArray.set(index, ((GwtJSONObject) value).getNativeJSONObject());
		} else if (value instanceof GwtJSONArray) {
			jsonArray.set(index, ((GwtJSONArray) value).getNativeJSONArray());
		}
		return this;
	}

	@Override
	public boolean optBoolean(int index) {
		return optBoolean(index, false);
	}

	@Override
	public boolean optBoolean(int index, boolean defaultValue) {
		try {
			return getBoolean(index);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public int optInt(int index) {
		return (int) optDouble(index, 0);
	}

	@Override
	public int optInt(int index, int defaultValue) {
		try {
			return (int) getDouble(index);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public double optDouble(int index) {
		return optDouble(index, Double.NaN);
	}

	@Override
	public double optDouble(int index, double defaultValue) {
		try {
			return getDouble(index);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public String optString(int index) {
		return optString(index, "");
	}

	@Override
	public String optString(int index, String defaultValue) {
		Object o = opt(index);
		return o != null ? o.toString() : defaultValue;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray optJSONArray(
			int index) {
		Object o = opt(index);
		return o instanceof JSONArray ? new GwtJSONArray((JSONArray) o) : null;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject optJSONObject(
			int index) {
		Object o = opt(index);
		return o instanceof JSONObject ? new GwtJSONObject((JSONObject) o)
				: null;
	}

	/**
	 * Get the object value associated with an index.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return An object value.
	 * @throws JSONException
	 *             If there is no value for the index.
	 */
	private JSONValue get(int index) throws JSONException {
		JSONValue o = opt(index);
		if (o == null) {
			throw new JSONException("JSONArray[" + index + "] not found.");
		}
		return o;
	}

	/**
	 * Get the optional object value associated with an index.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return An object value, or null if there is no object at that index.
	 */
	private JSONValue opt(int index) {
		return (index < 0 || index >= length()) ? null : this.jsonArray
				.get(index);
	}

}
