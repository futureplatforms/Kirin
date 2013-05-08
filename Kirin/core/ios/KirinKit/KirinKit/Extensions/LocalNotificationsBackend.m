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
                       withId: (NSNumber*) notificationId {
    NSDate *itemDate = [NSDate dateWithTimeIntervalSince1970:(millisSince1970.doubleValue / 1000)];
    
    NSLog(@"LocalNotificationsBackend.scheduleNotification %@ at time %@ where millisSince1970 is %@ with id %@", text, itemDate, millisSince1970, notificationId);

    UILocalNotification *localNotif = [[UILocalNotification alloc] init];

    if (localNotif == nil)
        return;

    localNotif.fireDate = itemDate;
    localNotif.timeZone = [NSTimeZone defaultTimeZone];

    localNotif.alertBody = [title stringByAppendingFormat:@": %@", text];
    localNotif.soundName = UILocalNotificationDefaultSoundName;

    localNotif.alertAction = @"OK";

    localNotif.userInfo = [NSDictionary dictionaryWithObject:notificationId forKey:@"id"];
    
    [[UIApplication sharedApplication] scheduleLocalNotification:localNotif];
    [localNotif release];
}

- (void) cancelNotification: (NSNumber*) notificationId {
    NSLog(@"LocalNotificationsBackend.cancelNotification %@", notificationId);
    
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
