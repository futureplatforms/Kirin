#import <Foundation/Foundation.h>

@protocol LocalNotifications <NSObject>

- (void) scheduleNotification: (NSString*) text withTitle: (NSString*) title atTime: (NSNumber*) millisSince1970 withId: (NSNumber*) notificationId withBadge: (NSNumber*) badge;

- (void) cancelNotification: (NSNumber*) notificationId;

@end
