/* Generated from GwtNotificationServiceNative
 * Do not edit, as this WILL be overwritten
 */
@protocol GwtNotificationServiceNative <NSObject>

- (void) scheduleNotification: (NSString*) notificationId : (NSString*) timeMillisSince1970 : (NSString*) title : (NSString*) text : (int) badge;

- (void) cancelNotification: (NSString*) notificationId;

@end