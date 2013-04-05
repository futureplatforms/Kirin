#import "LocalNotificationsBackend.h"
#import "Kirin.h"
#import "JSON.h"

@interface LocalNotificationsBackend ()
@end

@implementation LocalNotificationsBackend

- (id) init {
    return [super initWithModuleName:@"LocalNotifications"];
}

#pragma mark -
#pragma mark Interal Methods



#pragma mark -
#pragma mark External Interface Methods

- (void) scheduleNotifications: (NSDictionary *) config {
    NSLog(@"TODO: ios LocalNotificationsBackend.scheduleNotification");
}

- (void) cancelNotifications: (int) id {
    NSLog(@"TODO: ios LocalNotificationsBackend.cancelNotification");
}

@end
