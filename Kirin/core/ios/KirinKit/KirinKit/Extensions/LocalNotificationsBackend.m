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

- (void) scheduleNotification: (NSString*) text atTime: (NSNumber*) millisSince1970 withId: (NSString*) notificationId {
    NSDate *itemDate = [NSDate dateWithTimeIntervalSince1970:millisSince1970.longValue / 1000];
    
    NSLog(@"LocalNotificationsBackend.scheduleNotification %@ at time %@ with id %@", text, itemDate, notificationId);

    UILocalNotification *localNotif = [[UILocalNotification alloc] init];

    if (localNotif == nil)
        return;

    localNotif.fireDate = itemDate;
    localNotif.timeZone = [NSTimeZone defaultTimeZone];

    localNotif.alertBody = text;
    localNotif.soundName = UILocalNotificationDefaultSoundName;

    localNotif.alertAction = @"OK";

    [[UIApplication sharedApplication] scheduleLocalNotification:localNotif];
    [localNotif release];
}

- (void) cancelNotification: (NSString*) notificationId {
    NSLog(@"LocalNotificationsBackend.cancelNotification %@", notificationId);
    
    NSArray* allEvents = [[UIApplication sharedApplication] scheduledLocalNotifications];
    
    UILocalNotification* loc;
    NSEnumerator* e = [allEvents objectEnumerator];
    
    while((loc = [e nextObject])) {
        if([[[loc userInfo] objectForKey:@"id"] isEqualToString:notificationId]) {
            [[UIApplication sharedApplication] cancelLocalNotification: loc];
        }
    }
}

@end
