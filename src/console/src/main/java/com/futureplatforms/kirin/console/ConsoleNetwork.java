package com.futureplatforms.kirin.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    @Override
    public void doHttp(final HttpVerb verb, final String url, final String payload,
            final Map<String, String> headers, final NetworkResponse callback) {
        new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   URL theURL = new URL(url);
                   HttpURLConnection conn = (HttpURLConnection) theURL.openConnection();
                   conn.setRequestMethod(verb.toString());
                   Set<String> keys = headers.keySet();
                   for (String key : keys) {
                       conn.setRequestProperty(key, headers.get(key));
                   }
                   
                   if (verb._HasPayload) {
                       OutputStream os = conn.getOutputStream();
                       os.write(payload.getBytes());
                       os.close();
                   } 
                   
                   InputStream is = conn.getInputStream();
                   int i;
                   String str = "";
                   while ((i=is.read()) != -1) {
                       str += (byte)i;
                   }
                   Map<String, String> retHeaders = Maps.newHashMap();
                   Map<String, List<String>> reqProps = conn.getRequestProperties();
                   Set<String> reqKeys = reqProps.keySet();
                   for (String reqKey : reqKeys) {
                       retHeaders.put(reqKey, reqProps.get(reqKey).get(0));
                   }
                   
                   callback.onSuccess(conn.getResponseCode(), str, retHeaders);
               } catch (IOException e) {
                   callback.onFail("err");
               }
           }
        }).start();
    }
}
