package com.futureplatforms.kirin.console;


import java.util.Map;

import com.futureplatforms.kirin.console.db.ConsoleDB;
import com.futureplatforms.kirin.console.json.ConsoleJson;
import com.futureplatforms.kirin.console.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback2;
import com.futureplatforms.kirin.dependencies.NotificationDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegateClient;
import com.futureplatforms.kirin.dependencies.fb.FacebookDelegate;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookRequestsCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookShareCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ReadPermission;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.ShareDialogParams;

public final class Kirin {
	public static void kickOffWithNetwork(NetworkDelegateClient net) {
		StaticDependencies.getInstance().setDependencies(
				new ConsoleLog(), 
				new ConsoleSettings(), 
				new ConsoleLocation(), 
				net, 
				new ConsoleJson(),
				new JaxpXmlParser(),
				new ConsoleFormatter(),
				Configuration.Debug,
				new ConsoleDB(),
				new ConsoleTimer(),
				new NotificationDelegate() {
					
					@Override
					public void scheduleNotification(int notificationId,
							long timeMillisSince1970, String title, String text, int badge) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void cancelNotification(int notificationId) {
						// TODO Auto-generated method stub
						
					}
				},
				new FacebookDelegate() {
					
					@Override
					public void signOut(AsyncCallback cb) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void _SignOut(AsyncCallback cb) {

					}

					@Override
					public void presentShareDialogWithParams(ShareDialogParams params,
							FacebookShareCallback cb) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void presentRequestsDialog(FacebookRequestsCallback cb) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void nativeRequestPublishPermissions(FacebookLoginCallback callback,
							PublishPermission... permissions) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void nativeOpenSessionWithReadPermissions(
							FacebookLoginCallback callback, boolean allowUI,
							ReadPermission... permissions) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void isLoggedIn(AsyncCallback1<Boolean> cb) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void getCurrentPermissions(AsyncCallback1<String[]> cb) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void getAppId(AsyncCallback1<String> cb) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void getAccessToken(AsyncCallback2<String, String> cb) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void logEvent(String eventName,
							Map<String, String> parameters) {
						// TODO Auto-generated method stub
						
					}
				});
		
	}
	
    public static void kickOff() {
        kickOffWithNetwork(new ConsoleNetwork());
    }
}
