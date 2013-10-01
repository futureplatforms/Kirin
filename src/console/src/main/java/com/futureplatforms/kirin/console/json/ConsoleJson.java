package com.futureplatforms.kirin.console.json;

import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public class ConsoleJson implements JSONDelegate {

    @Override
    public JSONObject getJSONObject(String jsonText)  {
        return new ConsoleJSONObject(jsonText);
    }

    @Override
    public JSONArray getJSONArray(String jsonText) {
        return new ConsoleJSONArray(jsonText);
    }

    @Override
    public JSONObject getJSONObject() {
        return new ConsoleJSONObject();
    }

    @Override
    public JSONArray getJSONArray() {
        return new ConsoleJSONArray();
    }
    
	@Override
	public String quoteAndEscape(final String javaStringToEscapeAsJSONString) {
		if(null == javaStringToEscapeAsJSONString) return null;
		else return org.json.JSONObject.quote(javaStringToEscapeAsJSONString);
	}
}
