#import <Foundation/Foundation.h>

@protocol LocalNotifications <NSObject>

- (void) scheduleNotification: (NSString*) text atTime: (NSNumber*) secondsSince1970 withId: (NSNumber*) notificationId;
- (void) cancelNotification: (NSNumber*) notificationId;

@end
