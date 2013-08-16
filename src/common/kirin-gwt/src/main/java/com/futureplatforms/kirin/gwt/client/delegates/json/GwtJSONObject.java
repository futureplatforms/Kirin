package com.futureplatforms.kirin.gwt.client.delegates.json;

import java.util.Iterator;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GwtJSONObject extends com.futureplatforms.kirin.dependencies.json.JSONObject {

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

    private boolean isNull(JSONValue value) {
        return value == null || value instanceof JSONNull;
    }
    
    @Override
    public boolean getBoolean(String key) {
        boolean has = jsonObj.containsKey(key);
        if (has) {
            JSONValue value = jsonObj.get(key);
            boolean isNull = isNull(value);
            if (!isNull) {
                JSONBoolean jb = value.isBoolean();
                if (jb != null) {
                    return jb.booleanValue();
                } else {
                    throw new IllegalStateException(key + " isn't boolean");
                }
            } else {
                throw new IllegalStateException(key + " is null");
            }
        } else {
            throw new IllegalStateException(key + " doesn't exist");
        }
    }

    @Override
    public double getDouble(String key) {
        boolean has = jsonObj.containsKey(key);
        if (has) {
            JSONValue value = jsonObj.get(key);
            boolean isNull = isNull(value);
            if (!isNull) {
                JSONNumber jn = value.isNumber();
                if (jn != null) {
                    return jn.doubleValue();
                } else {
                    throw new IllegalStateException(key + " isn't double");
                }
            } else {
                throw new IllegalStateException(key + " is null");
            }
        } else {
            throw new IllegalStateException(key + " doesn't exist");
        }
    }

    @Override
    public String getString(String key) {
        if (jsonObj.containsKey(key)) {
            JSONValue value = jsonObj.get(key);
            boolean isNull = isNull(value);
            if (!isNull) {
                JSONString js = value.isString();
                if (js != null) {
                    return js.stringValue();
                } else {
                    throw new IllegalStateException(key + " isn't String");
                }
            } else {
                return null;
            }
        } else {
            throw new IllegalStateException(key + " doesn't exist");
        }
    }

    @Override
    public boolean isNull(String key) {
        JSONValue value = jsonObj.get(key);
        boolean isNull = isNull(value);
        if (isNull) { return true; }
        return value.isNull() != null;
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONArray getJSONArray(String key) {
        JSONValue value = jsonObj.get(key);
        boolean isNull = isNull(value);
        if (!isNull) {
            com.google.gwt.json.client.JSONArray ja = value.isArray();
            if (ja != null) {
                return new GwtJSONArray(ja);
            } else {
                throw new IllegalStateException(key + " isn't Array");
            }
        } else {
            throw new IllegalStateException(key + " is null");
        }
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(String key) {
        JSONValue value = jsonObj.get(key);
        boolean isNull = isNull(value);
        if (!isNull) {
            com.google.gwt.json.client.JSONObject jo = value.isObject();
            if (jo != null) {
                return new GwtJSONObject(jo);
            } else {
                throw new IllegalStateException(key + " isn't Object");
            }
        } else {
            throw new IllegalStateException(key + " is null");
        }
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
            return this.put(key, ((Boolean)value).booleanValue());
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
        return jsonObj.equals(((GwtJSONObject)other).getNativeJSONObject());
    }
    
    @Override
    public int hashCode() {
        return jsonObj.hashCode();
    }
    
    protected JSONObject getNativeJSONObject() {
        return jsonObj;
    }
}
