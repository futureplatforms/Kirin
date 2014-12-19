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

- (void) scheduleNotification: (NSString*) text
                    withTitle: (NSString*) title
                       atTime: (NSNumber*) millisSince1970
                       withId: (NSNumber*) notificationId
                    withBadge: (NSNumber*) badge {
    NSDate *itemDate = [NSDate dateWithTimeIntervalSince1970:(millisSince1970.doubleValue / 1000)];
    
    if ([KIRIN loggingEnabled]) {
        NSLog(@"LocalNotificationsBackend.scheduleNotification %@ at time %@ where millisSince1970 is %@ with id %@", text, itemDate, millisSince1970, notificationId);
    }
    UILocalNotification *localNotif = [[UILocalNotification alloc] init];

    if (localNotif == nil)
        return;

    localNotif.fireDate = itemDate;
    localNotif.timeZone = [NSTimeZone defaultTimeZone];

    localNotif.alertBody = text;
    localNotif.soundName = UILocalNotificationDefaultSoundName;

    localNotif.alertAction = @"OK";

    if ([badge intValue] != -1) {
        localNotif.applicationIconBadgeNumber = [badge intValue];
    }
    NSDictionary * dic = [KIRIN notificationUserData];
    if (dic) {
        localNotif.userInfo = dic;
    } else {
        localNotif.userInfo = [NSDictionary dictionaryWithObject:notificationId forKey:@"id"];
    }
    
    [[UIApplication sharedApplication] scheduleLocalNotification:localNotif];
    [localNotif release];
}

- (void) cancelNotification: (NSNumber*) notificationId {
    if ([KIRIN loggingEnabled]) {
        NSLog(@"LocalNotificationsBackend.cancelNotification %@", notificationId);
    }
    
    NSArray* allEvents = [[UIApplication sharedApplication] scheduledLocalNotifications];
    
    UILocalNotification* loc;
    NSEnumerator* e = [allEvents objectEnumerator];
    
    while((loc = [e nextObject])) {
        if([[[loc userInfo] objectForKey:@"id"] isEqualToNumber:notificationId]) {
            [[UIApplication sharedApplication] cancelLocalNotification: loc];
        }
    }
}

@end
