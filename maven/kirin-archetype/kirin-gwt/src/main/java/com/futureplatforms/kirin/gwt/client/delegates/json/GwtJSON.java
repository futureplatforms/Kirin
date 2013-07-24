package com.futureplatforms.kirin.gwt.client.delegates.json;

import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.google.gwt.core.client.JsonUtils;

public class GwtJSON implements JSONDelegate {

    @Override
    public JSONObject getJSONObject(String jsonText) {
        return new GwtJSONObject(jsonText);
    }

    @Override
    public JSONArray getJSONArray(String jsonText) {
        return new GwtJSONArray(jsonText);
    }

    @Override
    public JSONObject getJSONObject() {
        return new GwtJSONObject();
    }

    @Override
    public JSONArray getJSONArray() {
        return new GwtJSONArray();
    }

    @Override
    public String quoteAndEscape(String javaStringToEscapeAsJSONString) {
        if(null == javaStringToEscapeAsJSONString) return null;
        return JsonUtils.escapeValue(javaStringToEscapeAsJSONString);
    }
}
