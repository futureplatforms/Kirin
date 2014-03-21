package com.futureplatforms.kirin.android.json;

import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public class AndroidJson implements JSONDelegate {

    @Override
    public JSONObject getJSONObject(String jsonText) throws JSONException {
        return new AndroidJSONObject(jsonText);
    }

    @Override
    public JSONArray getJSONArray(String jsonText) throws JSONException {
        return new AndroidJSONArray(jsonText);
    }

    @Override
    public JSONObject getJSONObject() {
        return new AndroidJSONObject();
    }

    @Override
    public JSONArray getJSONArray() {
        return new AndroidJSONArray();
    }
    
	@Override
	public String quoteAndEscape(final String javaStringToEscapeAsJSONString) {
		if(null == javaStringToEscapeAsJSONString) return null;
		else return org.json.JSONObject.quote(javaStringToEscapeAsJSONString);
	}
}
