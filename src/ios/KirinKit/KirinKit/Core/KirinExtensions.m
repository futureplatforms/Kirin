//
//  KirinExtensions.m
//  KirinKit
//
//  Created by James Hugman on 11/01/2012.
//  Copyright 2012 Future Platforms. All rights reserved.
//

#import "KirinExtensions.h"

#import "SettingsBackend.h"
#import "FileSystemBackend.h"
#import "KirinImagePicker.h"
#import "LocalNotificationsBackend.h"
#import "KirinImageTransformer.h"
#import "NewNetworkingImpl.h"
#import "NewDatabaseAccessService.h"
#import "KirinGwtServiceProtocol.h"
#import "SymbolMapService.h"
#import "KirinFacebook.h"
#import "KirinGwtLocation.h"
#import "Crypto.h"
@interface KirinExtensions()

@property(retain) NSMutableArray* allExtensions;

@end

@implementation KirinExtensions

@synthesize isStarted;

@synthesize allExtensions;

+ (KirinExtensions*) empty {
    DLog(@"Empty KirinExtensions");
    return [[[KirinExtensions alloc] init] autorelease];
}

+ (KirinExtensions*) coreExtensions {
    KirinExtensions* services = [KirinExtensions empty];
    DLog(@"Core KirinExtensions");
    [services registerExtension:[[[SettingsBackend alloc] init] autorelease]];
    [services registerExtension:[[LocalNotificationsBackend alloc] init]];
    NewDatabaseAccessService *dbAccess = [[NewDatabaseAccessService alloc] init];
    [services registerGwtService:dbAccess];
    [services registerGwtService:dbAccess.NewTransactionService];
    [services registerGwtService:[[SymbolMapService alloc] init]];
    [services registerGwtService:[[KirinGwtLocation alloc] init]];
    Crypto *cr = [[Crypto alloc] init];
    [cr onRegister];

    
    if(NSClassFromString(@"FBSession")) {
        if ([[NSBundle mainBundle] objectForInfoDictionaryKey:@"FacebookAppID"]) {
            KirinFacebook* fb = [[KirinFacebook alloc] init];
            [fb onRegister];
        }
    } else {
    }
    
    NewNetworkingImpl * newNetworking = [[NewNetworkingImpl alloc] init];
    [newNetworking onRegister];
    
    return services;
}

- (id) init {
    self = [super init];
    if (self) {
        self.allExtensions = [NSMutableArray array];
    }
    return self;
}

- (void) registerGwtService: (id<KirinGwtServiceProtocol>) service {
    [self.allExtensions addObject:service];
    [service onRegister];
}

- (void) registerExtension: (id<KirinExtensionProtocol>) service {
    [self.allExtensions addObject:service];
    [service onLoad];
    if (self.isStarted && [service respondsToSelector:@selector(onStart)]) {
        [service onStart];
    }
}

- (void) ensureStarted {
    if (self.isStarted) {
        return;
    }
 
    self.isStarted = YES;   
    
    for (int i=0, max=[self.allExtensions count]; i<max; i++) {
        id<KirinExtensionProtocol> service = [self.allExtensions objectAtIndex:i];
        if ([service respondsToSelector:@selector(onStart)]) {
            [service onStart];
        }
    }
    
}

- (void) unloadServices {
    if (!self.isStarted) {
        return;
    }
    
    for (int i=0, max=[self.allExtensions count]; i<max; i++) {
        id<KirinExtensionProtocol> service = [self.allExtensions objectAtIndex:i];
        if ([service respondsToSelector:@selector(onStop)]) {
            [service onStop];
        }
    }
    
    for (int i=0, max=[self.allExtensions count]; i<max; i++) {
        id<KirinExtensionProtocol> service = [self.allExtensions objectAtIndex:i];
        if ([service respondsToSelector:@selector(onUnload)]) {
            [service onUnload];
        }
    }
    
    self.isStarted = NO;
}

- (void) dealloc {
    self.isStarted = NO;
    self.allExtensions = nil;
    [super dealloc];
}

@end
