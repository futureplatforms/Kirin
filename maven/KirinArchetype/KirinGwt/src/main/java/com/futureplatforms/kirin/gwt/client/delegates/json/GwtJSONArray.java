package com.futureplatforms.kirin.gwt.client.delegates.json;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;


public class GwtJSONArray extends com.futureplatforms.kirin.dependencies.json.JSONArray {

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
    
    @Override
    public boolean getBoolean(int index) {
        return jsonArray.get(index).isBoolean().booleanValue();
    }

    @Override
    public double getDouble(int index) {
        return jsonArray.get(index).isNumber().doubleValue();
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONArray  getJSONArray(int index) {
        return new GwtJSONArray(jsonArray.get(index).isArray());
    }

    @Override
    public com.futureplatforms.kirin.dependencies.json.JSONObject getJSONObject(int index) {
        return new GwtJSONObject(jsonArray.get(index).isObject());
    }

    @Override
    public String getString(int index) {
        return jsonArray.get(index).isString().stringValue();
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
            return this.putBoolean(index, ((Boolean)value).booleanValue());
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
}
