#import <Foundation/Foundation.h>

@protocol LocalNotifications <NSObject>

- (void) scheduleNotifications: (NSDictionary *) config;
- (void) cancelNotifications: (int) id;

@end
