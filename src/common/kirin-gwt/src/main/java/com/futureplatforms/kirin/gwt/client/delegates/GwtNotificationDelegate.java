package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.dependencies.NotificationDelegate;

public class GwtNotificationDelegate implements NotificationDelegate {

	private static native void callScheduleNotification(final int notificationId, final double timeMillisSince1970, final String title, final String text) /*-{
	    var notifications = $wnd.require("LocalNotifications");
	    
	    var config = {
	    	title: title,
	        text: text,
	        id: notificationId,
	        timeMillisSince1970: timeMillisSince1970
	    };
	    
	    notifications.scheduleNotification(config);
		
	}-*/;
	
	private static native void callCancelNotification(final int notificationId) /*-{
	    $wnd.require("LocalNotifications").cancelNotification(notificationId);
	}-*/;
	
	@Override
	public void scheduleNotification(final int notificationId, final long timeMillisSince1970, final String title, final String text) {
		callScheduleNotification(notificationId, timeMillisSince1970, title, text);
	}
	
	@Override
	public void cancelNotification(final int notificationId) {
		callCancelNotification(notificationId);
	}

}
