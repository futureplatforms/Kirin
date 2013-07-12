package com.futureplatforms.kirin.dependencies.json;


public interface JSONDelegate {
    public JSONObject getJSONObject();
    public JSONObject getJSONObject(String jsonText);
    public JSONArray getJSONArray(String jsonText);
    public JSONArray getJSONArray();
    public String quoteAndEscape(String javaStringToEscapeAsJSONString);
}
