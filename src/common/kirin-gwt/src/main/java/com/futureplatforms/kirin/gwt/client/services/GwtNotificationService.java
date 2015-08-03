package com.futureplatforms.kirin.gwt.client.services;

import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.GwtNotificationServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.gwt.core.client.GWT;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Created by douglashoskins on 27/07/2015.
 */
@Export(value = "GwtNotificationService", all = true)
@ExportPackage("screens")
public class GwtNotificationService extends KirinService<GwtNotificationServiceNative> {
    public GwtNotificationService() {
        super(GWT.<GwtNotificationServiceNative>create(GwtNotificationServiceNative.class));
    }

    @NoBind
    public void _scheduleNotification(
            final int notificationId, final long timeMillisSince1970, final String title, final String text, int badge) {
        getNativeObject().scheduleNotification(""+notificationId, Long.toString(timeMillisSince1970), title, text, badge);
    }

    @NoBind
    public void _cancelNotification(final int notificationId) {
        getNativeObject().cancelNotification(""+notificationId);
    }
}
