package com.futureplatforms.kirin.console.json;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.futureplatforms.kirin.dependencies.json.JSONArray;

public class ConsoleJSONObject extends com.futureplatforms.kirin.dependencies.json.JSONObject {
    private JSONObject jsonObject;
    
    public ConsoleJSONObject() {
        this.jsonObject = new JSONObject();
    }
    
    public ConsoleJSONObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    
    public ConsoleJSONObject(String jsonString) {
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't init object");
        }
    }
    
    @Override
    public boolean has(String key) {
        return jsonObject.has(key);
    }

    @Override
    public boolean getBoolean(String key) {
        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't get boolean");
        }
    }

    @Override
    public double getDouble(String key) {
        try {
            return jsonObject.getDouble(key);
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't get double");
        }
    }

    @Override
    public String getString(String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't get string");
        }
    }

    @Override
    public boolean isNull(String key) {
        return jsonObject.isNull(key);
    }

    @Override
    public JSONArray getJSONArray(String key) {
        try {
            return new ConsoleJSONArray(jsonObject.getJSONArray(key));
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't get array");
        }
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(String key) {
        try {
            return new ConsoleJSONObject(jsonObject.getJSONObject(key));
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't get object");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<String> keys() {
        return jsonObject.keys();
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject put(String key, int value) {
        try {
            jsonObject.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't put " + value);
        }
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject put(String key, long value) {
        try {
            jsonObject.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't put " + value);
        }
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject put(String key, Object value) {
        try {
            if (value instanceof Integer) {
                return this.put(key, ((Integer) value).intValue());
            } else if (value instanceof Boolean) {
                return this.put(key, ((Boolean)value).booleanValue());
            } else if (value instanceof Long) {
                return this.put(key, ((Long) value).longValue());
            } else if (value instanceof Double) {
                return this.put(key, ((Double) value).doubleValue());
            } else if (value instanceof String) {
                jsonObject.put(key, value);
            } else if (value instanceof ConsoleJSONObject) {
                jsonObject.put(key, ((ConsoleJSONObject) value).getNativeJSONObject());
            } else if (value instanceof ConsoleJSONArray) {
                jsonObject.put(key, ((ConsoleJSONArray) value).getNativeJSONArray());
            } else if (null == value) {
                jsonObject.put(key, (Object) null);
            }
            return this;
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't put " + value);
        }
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject put(String key, boolean value) {
        try {
            jsonObject.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't put " + value);
        }
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject put(String key, double value) {
        try {
            jsonObject.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't put " + value);
        }
    }

    @Override
    public int size() { return jsonObject.length(); }

    @Override
    public String toString() { return jsonObject.toString(); }

    @Override
    public boolean equals(
            com.futureplatforms.kirin.dependencies.json.JSONObject other) {
        return jsonObject.equals(((ConsoleJSONObject)other).getNativeJSONObject());
    }

    protected JSONObject getNativeJSONObject() { return jsonObject; }

	@Override
	public int getInt(String key) {
		try {
			return jsonObject.getInt(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException("couldn't put " + key);
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
		return jsonObject.optString(key, null);
	}

	@Override
	public JSONArray optJSONArray(String key) {
		try {
			return new ConsoleJSONArray(jsonObject.getJSONArray(key));
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public com.futureplatforms.kirin.dependencies.json.JSONObject optJSONObject(
			String key) {
		try {
			return new ConsoleJSONObject(jsonObject.getJSONObject(key));
		} catch (JSONException e) {
			return null;
		}
	}
}
