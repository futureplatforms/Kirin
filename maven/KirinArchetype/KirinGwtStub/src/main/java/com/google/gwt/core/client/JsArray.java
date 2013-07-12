package com.google.gwt.core.client;

import java.util.ArrayList;
import java.util.List;

public class JsArray<T extends JavaScriptObject> extends JavaScriptObject {
    private List<T> mList = new ArrayList<T>();
    
    public final void push(T value) {
        mList.add(value);
    }
    
    public final T get(int index) {
        return mList.get(index);
    }
}
