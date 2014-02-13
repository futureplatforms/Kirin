package com.futureplatforms.kirin.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;
import com.google.common.collect.Maps;

public class ConsoleNetwork implements NetworkDelegateClient {

	public static String slurp(final InputStream is, final int bufferSize)
	{
	  final char[] buffer = new char[bufferSize];
	  final StringBuilder out = new StringBuilder();
	  try {
	    final Reader in = new InputStreamReader(is, "UTF-8");
	    try {
	      for (;;) {
	        int rsz = in.read(buffer, 0, buffer.length);
	        if (rsz < 0)
	          break;
	        out.append(buffer, 0, rsz);
	      }
	    }
	    finally {
	      in.close();
	    }
	  }
	  catch (UnsupportedEncodingException ex) {
	    /* ... */
	  }
	  catch (IOException ex) {
	      /* ... */
	  }
	  return out.toString();
	}
	
    @Override
    public void doHttp(final HttpVerb verb, final String url, final String payload,
            final Map<String, String> headers, final NetworkResponse callback) {
        new Thread(new Runnable() {
           @Override
           public void run() {
        	   int code=0;
               try {
            	   System.out.println("URL is " + url);
                   URL theURL = new URL(url);
                   HttpURLConnection conn = (HttpURLConnection) theURL.openConnection();
                   conn.setRequestMethod(verb.toString());
                   Set<String> keys = headers.keySet();
                   for (String key : keys) {
                       conn.setRequestProperty(key, headers.get(key));
                   }
                   
                   if (verb._HasPayload) {
                	   conn.setDoOutput(true);
                       OutputStream os = conn.getOutputStream();
                       os.write(payload.getBytes());
                       os.close();
                   } 
                   
                   code = conn.getResponseCode();
                   String str = "";
                   Map<String, List<String>> reqProps = conn.getHeaderFields();
                   Map<String, String> retHeaders = Maps.newHashMap();
                   Set<String> reqKeys = reqProps.keySet();
                   for (String reqKey : reqKeys) {
                	   retHeaders.put(reqKey, reqProps.get(reqKey).get(0));
                   }
                   try {
	                   InputStream is = conn.getInputStream();
	                   str = slurp(is, 1024);
                   } catch (Throwable t) {
                	   t.printStackTrace();
                   }
                   
                   callback.onSuccess(code, str, retHeaders);
               } catch (Throwable e) {
                   callback.onFail(""+code);
               }
           }
        }).start();
    }
}
