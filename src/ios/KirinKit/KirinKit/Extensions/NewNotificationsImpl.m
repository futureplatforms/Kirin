//
//  NewNotificationsImpl.m
//  KirinKit
//
//  Created by Douglas Hoskins on 27/07/2015.
//  Copyright (c) 2015 Future Platforms. All rights reserved.
//

#import "NewNotificationsImpl.h"
#import "fromNative/GwtNotificationService.h"

@interface NewNotificationsImpl()
@property(strong) id<GwtNotificationService> kirinModule;
@end

@implementation NewNotificationsImpl

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"GwtNotificationService";
    self.kirinModuleProtocol = @protocol(GwtNotificationService);
    return [super initWithServiceName: self.serviceName];
}

- (void) scheduleNotification: (NSString *) notificationId : (NSString*) timeMillisSince1970 : (NSString*) title : (NSString*) text : (int) badge {
    NSDate *itemDate = [NSDate dateWithTimeIntervalSince1970:([timeMillisSince1970 doubleValue] / 1000)];
    
    if ([KIRINCONSTANTS loggingEnabled]) {
        NSLog(@"LocalNotificationsBackend.scheduleNotification %@ at time %@ where millisSince1970 is %@ with id %@", text, itemDate, timeMillisSince1970, notificationId);
    }

    UILocalNotification *localNotif = [[UILocalNotification alloc] init];
    
    if (localNotif == nil)
        return;
    
    localNotif.fireDate = itemDate;
    localNotif.timeZone = [NSTimeZone defaultTimeZone];
    
    localNotif.alertBody = text;
    localNotif.soundName = UILocalNotificationDefaultSoundName;
    
    localNotif.alertAction = @"OK";
    
    if (badge != -1) {
        localNotif.applicationIconBadgeNumber = badge;
    }
    NSDictionary * dic = [KIRIN notificationUserData];
    if (dic) {
        localNotif.userInfo = dic;
    } else {
        localNotif.userInfo = [NSDictionary dictionaryWithObject:notificationId forKey:@"id"];
    }
    
    [[UIApplication sharedApplication] scheduleLocalNotification:localNotif];
}

- (void) cancelNotification: (NSString *) notificationId {
    if ([KIRINCONSTANTS loggingEnabled]) {
        NSLog(@"LocalNotificationsBackend.cancelNotification %@", notificationId);
    }
     
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
