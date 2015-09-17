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
				});
		
	}
	
    public static void kickOff() {
        kickOffWithNetwork(new ConsoleNetwork());
    }
}
