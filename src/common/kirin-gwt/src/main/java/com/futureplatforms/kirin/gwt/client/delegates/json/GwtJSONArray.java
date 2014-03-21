package com.futureplatforms.kirin.gwt.client.delegates.json;

import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
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

	public GwtJSONArray(String jsonText) {
		this.jsonArray = JSONParser.parseStrict(jsonText).isArray();
	}

	public GwtJSONArray() {
		this.jsonArray = new JSONArray();
	}

	private boolean isNull(JSONValue value) {
		return value == null || value instanceof JSONNull;
	}

	@Override
	public boolean getBoolean(int index) {
		boolean has = jsonArray.size() > index;
		if (has) {
			JSONValue value = jsonArray.get(index);
			boolean isNull = isNull(value);
			if (!isNull) {
				JSONBoolean jb = value.isBoolean();
				if (jb != null) {
					return jb.booleanValue();
				} else {
					throw new IllegalStateException(index + " isn't boolean");
				}
			} else {
				throw new IllegalStateException(index + " is null");
			}
		} else {
			throw new IllegalStateException(index + " doesn't exist");
		}
	}

	@Override
	public int getInt(int index) {
		return (int) getDouble(index);
	}

	@Override
	public double getDouble(int index) {
		boolean has = jsonArray.size() > index;
		if (has) {
			JSONValue value = jsonArray.get(index);
			boolean isNull = isNull(value);
			if (!isNull) {
				JSONNumber jn = value.isNumber();
				if (jn != null) {
					return jn.doubleValue();
				} else {
					throw new IllegalStateException(index + " isn't double");
				}
			} else {
				throw new IllegalStateException(index + " is null");
			}
		} else {
			throw new IllegalStateException(index + " doesn't exist");
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray getJSONArray(
			int index) {
		JSONValue value = jsonArray.get(index);
		boolean isNull = isNull(value);
		if (!isNull) {
			com.google.gwt.json.client.JSONArray ja = value.isArray();
			if (ja != null) {
				return new GwtJSONArray(ja);
			} else {
				throw new IllegalStateException(index + " isn't Array");
			}
		} else {
			throw new IllegalStateException(index + " is null");
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(
			int index) {
		JSONValue value = jsonArray.get(index);
		boolean isNull = isNull(value);
		if (!isNull) {
			com.google.gwt.json.client.JSONObject jo = value.isObject();
			if (jo != null) {
				return new GwtJSONObject(jo);
			} else {
				throw new IllegalStateException(index + " isn't Object");
			}
		} else {
			throw new IllegalStateException(index + " is null");
		}
	}

	@Override
	public String getString(int index) {
		if (jsonArray.size() > index) {
			JSONValue value = jsonArray.get(index);
			boolean isNull = isNull(value);
			if (!isNull) {
				JSONString js = value.isString();
				if (js != null) {
					return js.stringValue();
				} else {
					throw new IllegalStateException(index + " isn't String");
				}
			} else {
				throw new IllegalStateException(index + " is null");
			}
		} else {
			throw new IllegalStateException(index + " doesn't exist");
		}
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
	public boolean optBoolean(int index, boolean defVal) {
		boolean has = jsonArray.size() > index;
		if (has) {
			JSONValue value = jsonArray.get(index);
			boolean isNull = isNull(value);
			if (!isNull) {
				JSONBoolean jb = value.isBoolean();
				if (jb != null) {
					return jb.booleanValue();
				} else {
					throw new IllegalStateException(index + " isn't boolean");
				}
			}
		}

		return defVal;
	}

	@Override
	public int optInt(int index, int defVal) {
		return (int) optDouble(index, defVal);
	}

	@Override
	public double optDouble(int index, double defVal) {
		boolean has = jsonArray.size() > index;
		if (has) {
			JSONValue value = jsonArray.get(index);
			boolean isNull = isNull(value);
			if (!isNull) {
				JSONNumber jn = value.isNumber();
				if (jn != null) {
					return jn.doubleValue();
				} else {
					throw new IllegalStateException(index + " isn't double");
				}
			}
		}
		return defVal;
	}

	@Override
	public String optString(int index) {
		if (jsonArray.size() > index) {
			JSONValue value = jsonArray.get(index);
			boolean isNull = isNull(value);
			if (!isNull) {
				JSONString js = value.isString();
				if (js != null) {
					return js.stringValue();
				} else {
					throw new IllegalStateException(index + " isn't String");
				}
			}
		}
		return null;
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONArray optJSONArray(
			int index) {
		JSONValue value = jsonArray.get(index);
		boolean isNull = isNull(value);
		if (!isNull) {
			com.google.gwt.json.client.JSONArray ja = value.isArray();
			if (ja != null) {
				return new GwtJSONArray(ja);
			}
		}

		return null;
	}

	@Override
	public JSONObject optJSONObject(int index) {
		JSONValue value = jsonArray.get(index);
		boolean isNull = isNull(value);
		if (!isNull) {
			com.google.gwt.json.client.JSONObject jo = value.isObject();
			if (jo != null) {
				return new GwtJSONObject(jo);
			} else {
				throw new IllegalStateException(index + " isn't Object");
			}
		}
		return null;
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
	public JSONValue opt(int index) {
		return (index < 0 || index >= length()) ? null : this.jsonArray
				.get(index);
	}

}
