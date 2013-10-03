//
//  NSObject+Kirin.m
//  KirinKit
//
//  Created by Douglas Hoskins on 12/03/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NSObject+Kirin.h"
#import <objc/runtime.h>

static char kirinModuleKey;
static char kirinServiceKey;

@implementation NSObject (Kirin)

- (void) kirinStartModule: (NSString *) moduleName withProtocol: (Protocol *) protocol {
    if(nil != moduleName) {
        self.kirinHelper = [KIRIN bindScreen:self toModule:moduleName];
    }
    
    if(nil != protocol) {
        [self callSubclassSetterForKirinModule: [self.kirinHelper proxyForJavascriptObject:protocol]];
    }
    
    NSLog(@"calling kirinhelper %@ onLoad", moduleName);
    [self.kirinHelper onLoad];
}

- (void) kirinStartService: (NSString *) moduleName withProtocol: (Protocol *) protocol {
    if(nil != moduleName) {
        self.kirinServiceHelper = [KIRIN bindService:self toModule:moduleName];
    }
    
    if(nil != protocol) {
        [self callSubclassSetterForKirinModule: [self.kirinHelper proxyForJavascriptObject:protocol]];
    }
    
    NSLog(@"calling kirinhelper %@ onLoad", moduleName);
    [self.kirinHelper onLoad];
}

- (void) callSubclassSetterForKirinModule: (id) withThisArgument {
    SEL selector = NSSelectorFromString(@"setKirinModule:");
    if([self respondsToSelector:selector]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
        [self performSelector:selector withObject:withThisArgument];
#pragma clang diagnostic pop
    }
}

- (void) setKirinServiceHelper:(KirinExtensionHelper *)kirinService
{
    objc_setAssociatedObject(self, &kirinServiceKey, kirinService, OBJC_ASSOCIATION_RETAIN);
}

- (KirinScreenHelper *) kirinServiceHelper
{
    KirinScreenHelper *ref= objc_getAssociatedObject(self, &kirinServiceKey);
    NSAssert(ref, @"Object was nil");
    return ref;
}

- (void) setKirinHelper:(KirinHelper *)kirinHelper
{
    objc_setAssociatedObject(self, &kirinModuleKey, kirinHelper, OBJC_ASSOCIATION_RETAIN);
}

- (KirinScreenHelper *) kirinHelper
{
    KirinScreenHelper *ref= objc_getAssociatedObject(self, &kirinModuleKey);
    NSAssert(ref, @"Object was nil");
    return ref;
}

@end
