package com.google.gwt.core.client;

import com.futureplatforms.kirin.GwtCreateClientBundle;
import com.google.gwt.resources.client.ClientBundle;

public class GWT {
    @SuppressWarnings("unchecked")
	public static <T> T create(Class<?> classLiteral) { 
    	if ((ClientBundle.class).isAssignableFrom(classLiteral)) {
    		return (T) new GwtCreateClientBundle().instantiate(classLiteral);
    	} else {
    		return null;
    	}
    }
}
