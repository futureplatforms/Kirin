package com.google.gwt.core.client;

public class JavaScriptObject {
    @SuppressWarnings("rawtypes")
    public static JavaScriptObject createArray() {
        return new JsArray();
    }
    
    /**
     * Returns a new object.
     */
    public static JavaScriptObject createObject() {
        return new JavaScriptObject();
    }
    
    @SuppressWarnings("unchecked")
    public final <T extends JavaScriptObject> T cast() {
      return (T) this;
    }
}
