//
//  NewSettingsImpl.m
//  KirinKit
//
//  Created by Douglas Hoskins on 27/07/2015.
//  Copyright (c) 2015 Future Platforms. All rights reserved.
//

#import "NewSettingsImpl.h"
#import "fromNative/GwtSettingsService.h"

@interface NewSettingsImpl()
@property(strong) id<GwtSettingsService> kirinModule;
@end

@implementation NewSettingsImpl

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"GwtSettingsService";
    self.kirinModuleProtocol = @protocol(GwtSettingsService);
    return [super initWithServiceName: self.serviceName];
}

- (void) onRegister {
    [super onRegister];
    NSString* json = [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:[self settingsAsDictionary] options:NSUTF8StringEncoding error:nil] encoding:NSUTF8StringEncoding];
    [self.kirinModule mergeOrOverwrite:json];
}

- (NSDictionary*) settingsAsDictionary {
    NSDictionary* settings = [[NSUserDefaults standardUserDefaults] dictionaryRepresentation];
    NSMutableDictionary* kirinSettings = [[NSMutableDictionary alloc] init];
    
    for (id key in settings) {
        if ([key hasPrefix:@"kirin-"]) {
            kirinSettings[[key substringFromIndex:6]] = settings[key];
        }
    }
    return kirinSettings;
}

- (void) updateContents: (NSArray*) addKeys : (NSArray*) addVals : (NSArray*) deletes {
    NSLog(@"updateContents :: %@, %@, %@", [addKeys debugDescription], [addVals debugDescription], [deletes debugDescription]);
    NSUserDefaults* userSettings = [NSUserDefaults standardUserDefaults];
    
    NSMutableDictionary* kirinAdds = [[NSMutableDictionary alloc] init];
    for (int i=0; i<[addKeys count]; i++) {
        NSString * key = addKeys[i];
        NSString * val = addVals[i];
        
        kirinAdds[[NSString stringWithFormat:@"kirin-%@", key]] = val;
    }
    [userSettings setValuesForKeysWithDictionary:kirinAdds];
    
    for (NSString * key in deletes) {
        [userSettings removeObjectForKey:[NSString stringWithFormat:@"kirin-%@", key]];
    }
    
    [userSettings synchronize];
}

- (void) clear {
    NSString *appDomain = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:appDomain];
}

@end
