package com.futureplatforms.kirin.console.json;

import org.json.JSONException;

import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public class ConsoleJSONArray extends JSONArray {

    private org.json.JSONArray jsonArray;
    
    public ConsoleJSONArray(org.json.JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
    
    public ConsoleJSONArray() {
        jsonArray = new org.json.JSONArray();
    }
    
    public ConsoleJSONArray(String jsonText) {
        try {
            jsonArray = new org.json.JSONArray(jsonText);
        } catch (JSONException e) {
            throw new IllegalArgumentException("invalid json: " + e.getMessage());
        }
    }

    @Override
    public boolean getBoolean(int index) {
        try {
            return jsonArray.getBoolean(index);
        } catch (JSONException e) {
            throw new IllegalArgumentException("it's not a boolean");
        }
    }

    @Override
    public double getDouble(int index) {
        try {
            return jsonArray.getDouble(index);
        } catch (JSONException e) {
            throw new IllegalArgumentException("it's not a double");
        }
    }

    @Override
    public JSONArray getJSONArray(int index) {
        try {
            return new ConsoleJSONArray(jsonArray.getJSONArray(index));
        } catch (JSONException e) {
            throw new IllegalArgumentException("it's not an array");
        }
    }

    @Override
    public JSONObject getJSONObject(int index) {
        try {
            return new ConsoleJSONObject(jsonArray.getJSONObject(index));
        } catch (JSONException e) {
            throw new IllegalArgumentException("couldn't get object");
        }
    }

    @Override
    public String getString(int index){
        try {
            return jsonArray.getString(index);
        } catch (JSONException e) {
            throw new IllegalArgumentException("it's not a string");
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
    public JSONArray putDouble(double d) {
        try {
            jsonArray.put(d);
        } catch (JSONException e) {
            throw new IllegalArgumentException("error adding " + d + " to array");
        }
        return this;
    }

    @Override
    public JSONArray putObject(Object value) {
        if (value instanceof Integer) {
            return this.putDouble(((Integer) value).intValue());
        } else if (value instanceof Boolean) {
            return this.putBoolean(((Boolean)value).booleanValue());
        } else if (value instanceof Long) {
            return this.putDouble(((Long) value).longValue());
        } else if (value instanceof Double) {
            return this.putDouble(((Double) value).doubleValue());
        } else if (value instanceof String) {
            jsonArray.put(value);
        } else if (value instanceof ConsoleJSONObject) {
            jsonArray.put(((ConsoleJSONObject) value).getNativeJSONObject());
        } else if (value instanceof ConsoleJSONArray) {
            jsonArray.put(((ConsoleJSONArray) value).getNativeJSONArray());
        }
        return this;
    }

    @Override
    public JSONArray putBoolean(int index, boolean b) {
        try {
            jsonArray.put(index, b);
        } catch (JSONException e) {
            throw new IllegalArgumentException("error adding " + b + " to array");
        }
        return this;
    }

    @Override
    public JSONArray putDouble(int index, double d) {
        try {
            jsonArray.put(index, d);
        } catch (JSONException e) {
            throw new IllegalArgumentException("error adding " + d + " to array");
        }
        return this;
    }

    @Override
    public JSONArray putObject(int index, Object value) {
        try {
            if (value instanceof Integer) {
                return this.putDouble(index, ((Integer) value).intValue());
            } else if (value instanceof Boolean) {
                return this.putBoolean(index, ((Boolean)value).booleanValue());
            } else if (value instanceof Long) {
                return this.putDouble(index, ((Long) value).longValue());
            } else if (value instanceof Double) {
                return this.putDouble(index, ((Double) value).doubleValue());
            } else if (value instanceof String) {
                jsonArray.put(index, value);
            } else if (value instanceof ConsoleJSONObject) {
                jsonArray.put(index, ((ConsoleJSONObject) value).getNativeJSONObject());
            } else if (value instanceof ConsoleJSONArray) {
                jsonArray.put(index, ((ConsoleJSONArray) value).getNativeJSONArray());
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("error adding " + value + " to array");
        }
        return this;
    }
}

