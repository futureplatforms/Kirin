package com.futureplatforms.kirin.console;


import com.futureplatforms.kirin.console.db.ConsoleDB;
import com.futureplatforms.kirin.console.json.ConsoleJson;
import com.futureplatforms.kirin.console.xml.JaxpXmlParser;
import com.futureplatforms.kirin.dependencies.NotificationDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;

public final class Kirin {
    public static void kickOff() {
        
        StaticDependencies.getInstance().setDependencies(
                new ConsoleLog(), 
                new ConsoleSettings(), 
                new ConsoleLocation(), 
                new ConsoleNetwork(), 
                new ConsoleJson(),
                new JaxpXmlParser(),
                new ConsoleFormatter(),
                Configuration.Debug,
                new ConsoleDB(),
                new ConsoleTimer(),
                new NotificationDelegate() {
					
					@Override
					public void scheduleNotification(int notificationId,
							long timeMillisSince1970, String title, String text) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void cancelNotification(int notificationId) {
						// TODO Auto-generated method stub
						
					}
				});
    }
}
