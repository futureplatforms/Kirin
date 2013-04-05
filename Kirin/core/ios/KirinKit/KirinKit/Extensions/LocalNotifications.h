#import <Foundation/Foundation.h>

@protocol LocalNotifications <NSObject>

- (void) scheduleNotification: (NSString*) text atTime: (NSNumber*) secondsSince1970 withId: (NSString*) notificationId;
- (void) cancelNotification: (NSString*) notificationId;

@end
