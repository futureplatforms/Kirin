package com.futureplatforms.kirin.dependencies.json;

public interface JSONDelegate {
	public JSONObject getJSONObject();

	public JSONObject getJSONObject(String jsonText) throws JSONException;

	public JSONArray getJSONArray(String jsonText) throws JSONException;

	public JSONArray getJSONArray();

	public String quoteAndEscape(String javaStringToEscapeAsJSONString);

}
