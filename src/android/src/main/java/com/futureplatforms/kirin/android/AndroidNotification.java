package com.futureplatforms.kirin.android;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.futureplatforms.kirin.dependencies.NotificationDelegate;

public class AndroidNotification implements NotificationDelegate {

	private final Context ctx;
	private final AlarmManager alarmMgr;
	private final NotificationManager notificationMgr;
	
	public AndroidNotification(final Context context) {
		this.ctx = context.getApplicationContext();
		this.alarmMgr = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		this.notificationMgr = (NotificationManager) this.ctx.getSystemService(Service.NOTIFICATION_SERVICE);
	}
	
	@Override
	public void scheduleNotification(final int notificationId, final long timeMillisSince1970, final String title, final String text) {
		Log.i("Kirin", "scheduleNotification for " + notificationId + " at " + new Date(timeMillisSince1970) + " this="+this);

		final PersistingAlarmIntent alarmIntent = new PersistingAlarmIntent(ctx, notificationId, timeMillisSince1970, title, text);
		scheduleNotification(alarmIntent);
	}
	
	private void scheduleNotification(final PersistingAlarmIntent alarmIntent) {
		if(alarmIntent.getTimeMillisSince1970() <= System.currentTimeMillis()) {
			// Show the notification immediately, don't bother scheduling an alarm
			Log.i("Kirin", "Scheduling notification immediately");
			displayNotification(ctx, alarmIntent);
		} else {
			Log.i("Kirin", "Scheduling alarm for the future");
			// Schedule an alarm to trigger in the future, at which point we will then show the notification
			alarmIntent.save();
			
			final PendingIntent pendingIntent = PendingIntent.getBroadcast(this.ctx, alarmIntent.getNotificationId(), alarmIntent, 0);
			
			this.alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmIntent.getTimeMillisSince1970(), pendingIntent);
		}
	}

	@Override
	public void cancelNotification(final int notificationId) {
		Log.i("Kirin", "cancelNotification for " + notificationId);

		// If an alarm is scheduled, cancel it.
		if(PersistingAlarmIntent.hasSaved(this.ctx, notificationId)) {
			final PersistingAlarmIntent alarmIntent = PersistingAlarmIntent.load(this.ctx, notificationId);
			
			this.alarmMgr.cancel(PendingIntent.getBroadcast(this.ctx, notificationId, alarmIntent, 0));
			
			alarmIntent.delete();
		}
		
		// If the notification is already showing, cancel it.
		this.notificationMgr.cancel(notificationId);
	}
	
	private static class RandomActivity extends Activity {
		
	}
	
	public static KirinNotificationActions KIRIN_NOTIFICATIONS_DROPBOX;
	
	public static class KirinNotificationActions {
		public final Class<?> _Class;
		public final Bundle _Extras;
		public KirinNotificationActions(Class<?> clz, Bundle extras) {
			_Class = clz; _Extras = extras;
		}
	}
	
	private static void displayNotification(final Context ctx, final PersistingAlarmIntent alarmIntent) {
		Log.d("Kirin", "displayNotification");
		
		try {
			final NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(ctx);
			
			Intent intent;
			if (KIRIN_NOTIFICATIONS_DROPBOX != null) {
				intent = new Intent(ctx, KIRIN_NOTIFICATIONS_DROPBOX._Class);
				intent.putExtras(KIRIN_NOTIFICATIONS_DROPBOX._Extras);
				KIRIN_NOTIFICATIONS_DROPBOX = null;
			} else {
				intent = new Intent(ctx, RandomActivity.class);
			}
			
			Log.d("Kirin", "Creating notification with code :: " + alarmIntent.getNotificationId());
			
			final PendingIntent pendingIntent = PendingIntent.getActivity(ctx, alarmIntent.getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			notifBuilder.setContentIntent(pendingIntent);
			notifBuilder.setContentTitle(alarmIntent.getTitle());
			notifBuilder.setContentText(alarmIntent.getText());
			notifBuilder.setSmallIcon(ctx.getApplicationInfo().icon);
			notifBuilder.setTicker(alarmIntent.getTitle() + ": " + alarmIntent.getText());
			notifBuilder.setAutoCancel(true);
			notifBuilder.setWhen(alarmIntent.getTimeMillisSince1970());
			notifBuilder.setDefaults(Notification.DEFAULT_ALL);
			
			final Notification notice = notifBuilder.build();
			
			final NotificationManager notificationMgr = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);
			
			notificationMgr.notify(alarmIntent.getNotificationId(), notice);
		} catch(Throwable t) {
			Log.i("Kirin", "AndroidNotification.displayNotification()", t);
		}
	}
	
	/**
	 * This displays notifications in response to alarms triggered by the AlarmManager.
	 */
	public static class NotificationAlarmReceiver extends BroadcastReceiver {
		protected static final String EXPECTED_ACTION = "com.futureplatforms.glasto13.client.dependencies.AndroidNotification.NotificationAlarmReceiver.KIRIN_NOTIFICATION";
		protected static final String EXPECTED_SCHEME = "kirin.notification.alarm";
		
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final Context appCtx = context.getApplicationContext();
			Log.i("Kirin", "AndroidNotification.AlarmReceiver.onReceive(): Received alarm for notification with data " + intent.getData());
			
			try {
				final String action = intent.getAction();
				
				if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
					// Device has rebooted, so the AlarmManager has gone and forgotten all our scheduled alarms.
					// Schedule all of the alarms again now.
					final List<PersistingAlarmIntent> allAlarmsIntents = PersistingAlarmIntent.loadAll(appCtx);
					
					// Delete all of the saved alarms before rescheduling
					for(final PersistingAlarmIntent alarmIntent : allAlarmsIntents) {
						alarmIntent.delete();
					}
					
					final AndroidNotification notifications = new AndroidNotification(appCtx);
					
					for(final PersistingAlarmIntent alarmIntent : allAlarmsIntents) {
						notifications.scheduleNotification(alarmIntent);
					}
				} else if(EXPECTED_ACTION.equals(action)) {
					// That's one of our scheduled alarms coming through right now! 
					
					final Uri data = intent.getData();
					
					if(data == null || !EXPECTED_SCHEME.equals(data.getScheme())) {
						// Well, that was unexpected! Don't handle this, whatever it is.
						return;
					}
		
					final PersistingAlarmIntent alarmIntent = new PersistingAlarmIntent(appCtx, data);
					
					// Remove the saved intent details now that we've actually received it
					alarmIntent.delete();
					
					displayNotification(appCtx, alarmIntent);
				} else {
					// Some other action that we shouldn't touch..
				}
			} catch(Throwable t) {
				Log.e("Kirin", "AndroidNotification.AlarmReceiver.onReceive()", t);
			}
		}
	}
	
	/**
	 * A PendingIntent that can be saved/loaded from the shared preferences
	 */
	public static class PersistingAlarmIntent extends Intent {
		/** 
		 * A prefix for the key used when storing alarm details in the shared application preferences.
		 * Added to this prefix is the notification ID of the scheduled alarm.
		 */
		private final static String PREFS_KEY_PREFIX = "AndroidNotification.ScheduledAlarmsPersistence."; 
		
		private final Context ctx;
		
		public PersistingAlarmIntent(final Context ctx, final int notificationId, final long timeMillisSince1970, final String title, final String text) {
			this(ctx, createIntentData(notificationId, timeMillisSince1970, title, text));
		}
		
		public PersistingAlarmIntent(final Context context, final Uri data) {
			super(NotificationAlarmReceiver.EXPECTED_ACTION, data, context, new NotificationAlarmReceiver().getClass());
			
			this.ctx = context;
		}

		private static Uri createIntentData(final int notificationId, final long timeMillisSince1970, final String title, final String text) {
			return Uri.parse(NotificationAlarmReceiver.EXPECTED_SCHEME + "://" + notificationId + "/" + timeMillisSince1970 + "/" + Uri.encode(title) + "/" + Uri.encode(text));
		}
		
		public int getNotificationId() {
			return Integer.parseInt(getData().getHost());
		}
		
		public long getTimeMillisSince1970() {
			return Long.parseLong(getData().getPathSegments().get(0));
		}
		
		public String getTitle() {
			return getData().getPathSegments().get(1);
		}
		
		public String getText() {
			return getData().getPathSegments().get(2);
		}
		
		private String getKey(final String forThisAttribute) {
			return PREFS_KEY_PREFIX + getNotificationId() + "." + forThisAttribute;
		}
		
		public void save() {
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
			
			final Editor prefsEditor = prefs.edit();
			
			prefsEditor.putLong(getKey("timeMillisSince1970"), getTimeMillisSince1970());
			prefsEditor.putString(getKey("title"), getTitle());
			prefsEditor.putString(getKey("text"), getText());
			
			prefsEditor.commit();
		}
		
		public void delete() {
			if(!hasSaved(this.ctx, this.getNotificationId())) {
				return; // Hasn't been saved, so there's nothing there to delete
			}

			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
			
			final Editor prefsEditor = prefs.edit();
			
			prefsEditor.remove(getKey("timeMillisSince1970"));
			prefsEditor.remove(getKey("title"));
			prefsEditor.remove(getKey("text"));
			
			prefsEditor.commit();
		}
		
		public static PersistingAlarmIntent load(final Context ctx, final int forThisNotificationId) {
			if(!hasSaved(ctx, forThisNotificationId)) {
				return null; // Nothing there to load
			}
			
			final String keyPrefix = PREFS_KEY_PREFIX + forThisNotificationId + ".";
			
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
			
			final long timeMillisSince1970 = prefs.getLong(keyPrefix + "timeMillisSince1970", 0);
			final String title = prefs.getString(keyPrefix + "title", "");
			final String text = prefs.getString(keyPrefix + "text", "");

			return new PersistingAlarmIntent(ctx, forThisNotificationId, timeMillisSince1970, title, text);
		}
		
		public static boolean hasSaved(final Context ctx, final int notificationId) {
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
			
			return prefs.contains(PREFS_KEY_PREFIX + notificationId + ".timeMillisSince1970");
		}
		
		public static List<PersistingAlarmIntent> loadAll(final Context ctx) {
			final List<PersistingAlarmIntent> alarmIntents = new LinkedList<PersistingAlarmIntent>();
			
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
			
			for(final String key : prefs.getAll().keySet()) {
				if(key.startsWith(PREFS_KEY_PREFIX)) {
					final int notificationId = Integer.parseInt(key.split("\\.")[2]);
					
					alarmIntents.add(load(ctx, notificationId));
				}
			}
			
			return alarmIntents;
		}
	}

}
