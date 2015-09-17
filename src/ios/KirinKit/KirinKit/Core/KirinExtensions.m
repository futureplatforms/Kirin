//
//  KirinExtensions.m
//  KirinKit
//
//  Created by James Hugman on 11/01/2012.
//  Copyright 2012 Future Platforms. All rights reserved.
//

#import "KirinExtensions.h"

#import "NewSettingsImpl.h"
#import "NewNetworkingImpl.h"
#import "NewDatabaseAccessService.h"
#import "KirinGwtServiceProtocol.h"
#import "SymbolMapService.h"
#import "KirinGwtLocation.h"
#import "NewNotificationsImpl.h"
#import "Crypto.h"
@interface KirinExtensions()

@property(retain) NSMutableArray* allExtensions;

@end

@implementation KirinExtensions

@synthesize isStarted;

@synthesize allExtensions;

+ (KirinExtensions*) empty {
    return [[KirinExtensions alloc] init];
}

+ (KirinExtensions*) coreExtensions {
    KirinExtensions* services = [KirinExtensions empty];
    NewDatabaseAccessService *dbAccess = [[NewDatabaseAccessService alloc] init];
    [services registerGwtService:dbAccess];
    [services registerGwtService:dbAccess.NewTransactionService];
    [services registerGwtService:[[SymbolMapService alloc] init]];
    [services registerGwtService:[[KirinGwtLocation alloc] init]];
    [services registerGwtService:[[Crypto alloc] init]];
    [services registerGwtService:[[NewSettingsImpl alloc] init]];
    [services registerGwtService:[[NewNotificationsImpl alloc] init]];
    [services registerGwtService:[[NewNetworkingImpl alloc] init]];
    
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
    
    for (NSUInteger i=0, max=[self.allExtensions count]; i<max; i++) {
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
    
    for (NSUInteger i=0, max=[self.allExtensions count]; i<max; i++) {
        id<KirinExtensionProtocol> service = [self.allExtensions objectAtIndex:i];
        if ([service respondsToSelector:@selector(onStop)]) {
            [service onStop];
        }
    }
    
    for (NSUInteger i=0, max=[self.allExtensions count]; i<max; i++) {
        id<KirinExtensionProtocol> service = [self.allExtensions objectAtIndex:i];
        if ([service respondsToSelector:@selector(onUnload)]) {
            [service onUnload];
        }
    }
    
    self.isStarted = NO;
}

@end
