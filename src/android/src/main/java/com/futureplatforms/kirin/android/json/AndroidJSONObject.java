package com.futureplatforms.kirin.android.json;

import java.util.Iterator;

import org.json.JSONObject;

import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONException;

public class AndroidJSONObject extends
		com.futureplatforms.kirin.dependencies.json.JSONObject {
	private JSONObject jsonObject;

	public AndroidJSONObject() {
		this.jsonObject = new JSONObject();
	}

	public AndroidJSONObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public AndroidJSONObject(String jsonString) throws JSONException {
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public boolean has(String key) {
		return jsonObject.has(key);
	}

	@Override
	public boolean getBoolean(String key) throws JSONException {
		try {
			return jsonObject.getBoolean(key);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public double getDouble(String key) throws JSONException {
		try {
			return jsonObject.getDouble(key);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public String getString(String key) throws JSONException {
		try {
			return jsonObject.getString(key);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public boolean isNull(String key) {
		return jsonObject.isNull(key);
	}

	@Override
	public JSONArray getJSONArray(String key) throws JSONException {
		try {
			return new AndroidJSONArray(jsonObject.getJSONArray(key));
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(
			String key) throws JSONException {
		try {
			return new AndroidJSONObject(jsonObject.getJSONObject(key));
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<String> keys() {
		return jsonObject.keys();
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, int value) throws JSONException {
		try {
			jsonObject.put(key, value);
			return this;
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, long value) throws JSONException {
		try {
			jsonObject.put(key, value);
			return this;
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, Object value) throws JSONException {
		try {
			if (value instanceof Integer) {
				return this.put(key, ((Integer) value).intValue());
			} else if (value instanceof Boolean) {
				return this.put(key, ((Boolean) value).booleanValue());
			} else if (value instanceof Long) {
				return this.put(key, ((Long) value).longValue());
			} else if (value instanceof Double) {
				return this.put(key, ((Double) value).doubleValue());
			} else if (value instanceof String) {
				jsonObject.put(key, value);
			} else if (value instanceof AndroidJSONObject) {
				jsonObject.put(key,
						((AndroidJSONObject) value).getNativeJSONObject());
			} else if (value instanceof AndroidJSONArray) {
				jsonObject.put(key,
						((AndroidJSONArray) value).getNativeJSONArray());
			} else if (null == value) {
				jsonObject.put(key, (Object) null);
			}
			return this;
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, boolean value) throws JSONException {
		try {
			jsonObject.put(key, value);
			return this;
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject put(
			String key, double value) throws JSONException {
		try {
			jsonObject.put(key, value);
			return this;
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public int size() {
		return jsonObject.length();
	}

	@Override
	public String toString() {
		return jsonObject.toString();
	}

	@Override
	public boolean equals(
			com.futureplatforms.kirin.dependencies.json.JSONObject other) {
		return jsonObject.equals(((AndroidJSONObject) other)
				.getNativeJSONObject());
	}

	protected JSONObject getNativeJSONObject() {
		return jsonObject;
	}

	@Override
	public int getInt(String key) throws JSONException {
		try {
			return jsonObject.getInt(key);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public boolean optBoolean(String key, boolean defVal) {
		return jsonObject.optBoolean(key, defVal);
	}

	@Override
	public int optInt(String key, int defVal) {
		return jsonObject.optInt(key, defVal);
	}

	@Override
	public double optDouble(String key, double defVal) {
		return jsonObject.optDouble(key, defVal);
	}

	@Override
	public String optString(String key) {
		return jsonObject.optString(key);
	}

	@Override
	public JSONArray optJSONArray(String key) {
		try {
			return new AndroidJSONArray(jsonObject.getJSONArray(key));
		} catch (org.json.JSONException e) {
			return null;
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject optJSONObject(
			String key) {
		try {
			return new AndroidJSONObject(jsonObject.getJSONObject(key));
		} catch (org.json.JSONException e) {
			return null;
		}
	}

	@Override
	public boolean optBoolean(String key) {
		return jsonObject.optBoolean(key);
	}

	@Override
	public int optInt(String key) {
		return jsonObject.optInt(key);
	}

	@Override
	public double optDouble(String key) {
		return jsonObject.optDouble(key);
	}

	@Override
	public String optString(String key, String defVal) {
		return jsonObject.optString(key, defVal);
	}
}
