package com.futureplatforms.kirin;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.common.io.ByteStreams;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public class GwtCreateClientBundle {

	public <T> T instantiate(final Class<?> classLiteral) {
		@SuppressWarnings("unchecked")
		T instance = (T) Proxy.newProxyInstance(classLiteral.getClassLoader(), new Class[] { classLiteral }, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				Source source = method.getAnnotation(Source.class);
				final String sourceStr = source.value()[0];
				InputStream is = classLiteral.getResourceAsStream(sourceStr);
				final byte[] bytes = ByteStreams.toByteArray(is);
				return new TextResource() {
					
					public String getName() {
						return sourceStr;
					}
					
					public String getText() {
						return new String(bytes);
					}
				};
			}
		});
		return instance;
	}

}
