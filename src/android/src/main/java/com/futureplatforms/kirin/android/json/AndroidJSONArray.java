package com.futureplatforms.kirin.android.json;

import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public class AndroidJSONArray extends JSONArray {

	private org.json.JSONArray jsonArray;

	public AndroidJSONArray(org.json.JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public AndroidJSONArray() {
		jsonArray = new org.json.JSONArray();
	}

	public AndroidJSONArray(String jsonText) throws JSONException {
		try {
			jsonArray = new org.json.JSONArray(jsonText);
		} catch (Exception e) {
			throw new JSONException(e);
		}
	}

	@Override
	public boolean getBoolean(int index) throws JSONException {
		try {
			return jsonArray.getBoolean(index);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public double getDouble(int index) throws JSONException {
		try {
			return jsonArray.getDouble(index);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public JSONArray getJSONArray(int index) throws JSONException {
		try {
			return new AndroidJSONArray(jsonArray.getJSONArray(index));
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public JSONObject getJSONObject(int index) throws JSONException {
		try {
			return new AndroidJSONObject(jsonArray.getJSONObject(index));
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public String getString(int index) throws JSONException {
		try {
			return jsonArray.getString(index);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public int length() {
		return jsonArray.length();
	}

	@Override
	public String toString() {
		return jsonArray.toString();
	}

	protected org.json.JSONArray getNativeJSONArray() {
		return jsonArray;
	}

	@Override
	public JSONArray putBoolean(boolean b) {
		jsonArray.put(b);
		return this;
	}

	@Override
	public JSONArray putDouble(double d) throws JSONException {
		try {
			jsonArray.put(d);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
		return this;
	}

	@Override
	public JSONArray putObject(Object value) throws JSONException {
		if (value instanceof Integer) {
			return this.putDouble(((Integer) value).intValue());
		} else if (value instanceof Boolean) {
			return this.putBoolean(((Boolean) value).booleanValue());
		} else if (value instanceof Long) {
			return this.putDouble(((Long) value).longValue());
		} else if (value instanceof Double) {
			return this.putDouble(((Double) value).doubleValue());
		} else if (value instanceof String) {
			jsonArray.put(value);
		} else if (value instanceof AndroidJSONObject) {
			jsonArray.put(((AndroidJSONObject) value).getNativeJSONObject());
		} else if (value instanceof AndroidJSONArray) {
			jsonArray.put(((AndroidJSONArray) value).getNativeJSONArray());
		}
		return this;
	}

	@Override
	public JSONArray putBoolean(int index, boolean b) throws JSONException {
		try {
			jsonArray.put(index, b);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
		return this;
	}

	@Override
	public JSONArray putDouble(int index, double d) throws JSONException {
		try {
			jsonArray.put(index, d);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
		return this;
	}

	@Override
	public JSONArray putObject(int index, Object value) throws JSONException {
		try {
			if (value instanceof Integer) {
				return this.putDouble(index, ((Integer) value).intValue());
			} else if (value instanceof Boolean) {
				return this.putBoolean(index, ((Boolean) value).booleanValue());
			} else if (value instanceof Long) {
				return this.putDouble(index, ((Long) value).longValue());
			} else if (value instanceof Double) {
				return this.putDouble(index, ((Double) value).doubleValue());
			} else if (value instanceof String) {
				jsonArray.put(index, value);
			} else if (value instanceof AndroidJSONObject) {
				jsonArray.put(index,
						((AndroidJSONObject) value).getNativeJSONObject());
			} else if (value instanceof AndroidJSONArray) {
				jsonArray.put(index,
						((AndroidJSONArray) value).getNativeJSONArray());
			}
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
		return this;
	}

	@Override
	public int getInt(int index) throws JSONException {
		try {
			return jsonArray.getInt(index);
		} catch (org.json.JSONException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public boolean optBoolean(int index, boolean defVal) {
		return jsonArray.optBoolean(index, defVal);
	}

	@Override
	public int optInt(int index, int defVal) {
		return jsonArray.optInt(index, defVal);
	}

	@Override
	public double optDouble(int index, double defVal) {
		return jsonArray.optDouble(index, defVal);
	}

	@Override
	public String optString(int index) {
		return jsonArray.optString(index);
	}

	@Override
	public JSONArray optJSONArray(int index) {
		try {
			return new AndroidJSONArray(jsonArray.getJSONArray(index));
		} catch (org.json.JSONException e) {
			return null;
		}
	}

	@Override
	public JSONObject optJSONObject(int index) {
		try {
			return new AndroidJSONObject(jsonArray.getJSONObject(index));
		} catch (org.json.JSONException e) {
			return null;
		}
	}

	@Override
	public boolean isNull(int index) {
		return jsonArray.isNull(index);
	}

	@Override
	public boolean optBoolean(int index) {
		return jsonArray.optBoolean(index);
	}

	@Override
	public double optDouble(int index) {
		return jsonArray.optDouble(index);
	}

	@Override
	public int optInt(int index) {
		return jsonArray.optInt(index);
	}

	@Override
	public String optString(int index, String defVal) {
		return jsonArray.optString(index,defVal);
	}

}
