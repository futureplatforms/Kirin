package com.futureplatforms.kirin.dependencies;

public interface NotificationDelegate {
	/**
	 * @param notificationId An ID identifying this notification, and allowing you to identify it when cancelling the notification.
	 * @param timeMillisSince1970 The time at which the notification is to be displayed. If this is in the past or present then the notification is displayed immediately. If it is in the future, then the notification is scheduled to appear at that time.
	 * @param title
	 * @param text
	 */
	public void scheduleNotification(int notificationId, long timeMillisSince1970, String title, String text, int badge);
	
	/**
	 * If the notification hasn't already been shown, this will cancel the scheduled notification from appearing.
	 * @param notificationId
	 */
    public void cancelNotification(int notificationId);
}
