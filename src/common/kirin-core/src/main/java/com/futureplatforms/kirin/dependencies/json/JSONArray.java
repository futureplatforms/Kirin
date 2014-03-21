package com.futureplatforms.kirin.dependencies.json;

/**
 * Replicates the 20080701 version of org.json API
 *
 */
public abstract class JSONArray {
    public abstract boolean getBoolean(int index) throws JSONException;
    public abstract int getInt(int index)throws JSONException;;
    public abstract double getDouble(int index)throws JSONException;;
    public abstract JSONArray getJSONArray(int index)throws JSONException;;
    public abstract JSONObject getJSONObject(int index)throws JSONException;;
    public abstract String getString(int index)throws JSONException;;
	public abstract boolean optBoolean(int index, boolean defVal);
	public abstract int optInt(int index, int defVal);
	public abstract double optDouble(int index, double defVal);
	public abstract String optString(int index);
	public abstract JSONArray optJSONArray(int index);
	public abstract JSONObject optJSONObject(int index);
    public abstract int length();
    
    public abstract JSONArray putBoolean(boolean b) throws JSONException;
    public abstract JSONArray putDouble(double d) throws JSONException;
    public abstract JSONArray putObject(Object o) throws JSONException;
    
    public abstract JSONArray putBoolean(int index, boolean b) throws JSONException;
    public abstract JSONArray putDouble(int index, double d) throws JSONException;
    public abstract JSONArray putObject(int index, Object o) throws JSONException;
    
    public abstract String toString();
    
    public interface ForEach {
        public void process(JSONObject obj);
    }
    
    public void forEach(ForEach each) throws JSONException {
        int len = length();
        for (int i=0; i<len; i++) {
            each.process(getJSONObject(i));
        }
    }
}
