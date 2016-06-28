/* Generated from GwtNotificationServiceNative
 * Do not edit, as this WILL be overwritten
 */
namespace Generated {
public interface GwtNotificationServiceNative {
void scheduleNotification(string notificationId, string timeMillisSince1970, string title, string text, int badge);
void cancelNotification(string notificationId);
}}