package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

/**
 * Created by douglashoskins on 27/07/2015.
 */
public interface GwtNotificationServiceNative extends IKirinNativeService {
    void scheduleNotification(final String notificationId, final String timeMillisSince1970, final String title, final String text, int badge);
    void cancelNotification(final String notificationId);
}
