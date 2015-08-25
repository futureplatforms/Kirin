//
//  NativeObjectHolder.m
//  KirinKit
//
//  Created by James Hugman on 29/01/2012.
//  Copyright (c) 2012 Future Platforms. All rights reserved.
//

#import "NativeObjectHolder.h"

#import <KirinKit/KirinExtensionOnMainThread.h>
#import <UIKit/UIViewController.h>

#import <objc/runtime.h>

@interface NativeObjectHolder ()

@end

@implementation NativeObjectHolder

@synthesize nativeObject = nativeObject_;
@synthesize dispatchQueue = dispatchQueue_;

+ (NativeObjectHolder*) holderForObject: (NSObject*) object {
    NativeObjectHolder* holder = [[[NativeObjectHolder alloc] init] autorelease];
    holder.nativeObject = object;
    return holder;
}

- (void) setNativeObject:(NSObject*)nativeObject {
    
    [nativeObject retain];
    [nativeObject_ release];
    nativeObject_ = nativeObject;
    if (!nativeObject_) {
        self.dispatchQueue = nil;
        return;
    }
    
    if ([nativeObject isKindOfClass:[UIViewController class]]) {
        //DLog(@"Will dispatch to UIViewController %@ on the main thread", [nativeObject class]);
        self.dispatchQueue = nil;//dispatch_get_main_queue();
    } else if ([nativeObject conformsToProtocol:@protocol(KirinExtensionOnMainThread)]) {
       // DLog(@"Will dispatch to KirinExtensionWithUI %@ on the main thread", [nativeObject class]);
        self.dispatchQueue = nil;//dispatch_get_main_queue();
    } else {
        
        SEL getter = @selector(dispatchQueue);
        
        if ([nativeObject_ respondsToSelector:getter]) {
            self.dispatchQueue = (dispatch_queue_t) [nativeObject performSelector:getter];
        }
        
        if (self.dispatchQueue) {
            //DLog(@"Will dispatch to KirinExtension %@ on a custom dispatch queue", [nativeObject class]);
        } else {
            //DLog(@"Will dispatch to KirinExtension %@ on a global dispatch queue", [nativeObject class]);
            self.dispatchQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
        }
    }
}

- (SEL) findSelectorFromString: methodName {
    NSString * realMethodName = [[methodName componentsSeparatedByString:@"_"] componentsJoinedByString:@":"];
    
    // unsure as to allow non-string munged methods.
    return NSSelectorFromString(realMethodName);
}

- (void) dealloc {
    self.dispatchQueue = nil;
    self.nativeObject = nil;
    [super dealloc];
}

@end
