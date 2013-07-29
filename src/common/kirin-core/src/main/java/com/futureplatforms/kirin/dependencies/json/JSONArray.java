package com.futureplatforms.kirin.dependencies.json;

public abstract class JSONArray {
    public abstract boolean getBoolean(int index);
    public abstract double getDouble(int index);
    public abstract JSONArray getJSONArray(int index);
    public abstract JSONObject getJSONObject(int index);
    public abstract String getString(int index);
    public abstract int length();
    
    public abstract JSONArray putBoolean(boolean b);
    public abstract JSONArray putDouble(double d);
    public abstract JSONArray putObject(Object o);
    
    public abstract JSONArray putBoolean(int index, boolean b);
    public abstract JSONArray putDouble(int index, double d);
    public abstract JSONArray putObject(int index, Object o);
    
    public abstract String toString();
    
    public interface ForEach {
        public void process(JSONObject obj);
    }
    
    public void forEach(ForEach each) {
        int len = length();
        for (int i=0; i<len; i++) {
            each.process(getJSONObject(i));
        }
    }
}
