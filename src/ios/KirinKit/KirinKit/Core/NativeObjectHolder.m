//
//  NativeObjectHolder.m
//  KirinKit
//
//  Created by James Hugman on 29/01/2012.
//  Copyright (c) 2012 Future Platforms. All rights reserved.
//

#import "NativeObjectHolder.h"

#import "KirinExtensionOnMainThread.h"
#import "KirinSerialExecute.h"
#import <UIKit/UIViewController.h>

#import <objc/runtime.h>

@interface NativeObjectHolder ()

@property(nonatomic, retain) NSDictionary* methodsMap;
@property(nonatomic, copy) NSString* name;

- (void) initializeMethodsMap;
- (NSString*) camelCasedMethodName: (NSString*) methodName;
- (NSString*) underscoredMethodName: (NSString*) methodName;

@end

@implementation NativeObjectHolder

static dispatch_queue_t serialDispatchQueue = nil;

@synthesize nativeObject = nativeObject_;
@synthesize dispatchQueue = dispatchQueue_;
@synthesize methodsMap = methodsMap_;

+ (NativeObjectHolder*) holderForObject: (NSObject*) object withName:(NSString *)name {
    NativeObjectHolder* holder = [[NativeObjectHolder alloc] init];
    holder.name = name;
    holder.nativeObject = object;
    return holder;
}

+ (dispatch_queue_t) globalSerialQueue {
    if (!serialDispatchQueue) {
        serialDispatchQueue = dispatch_queue_create("com.futureplatforms.kirin", DISPATCH_QUEUE_SERIAL);
    }
    NSLog(@"SERIAL DISPATCH QUEUE");
    return serialDispatchQueue;
}

- (void) setNativeObject:(NSObject*)nativeObject {
    nativeObject_ = nativeObject;
    if (!nativeObject_) {
        self.dispatchQueue = nil;
        return;
    }
    
    if (
            [nativeObject isKindOfClass:[UIViewController class]] ||
            [nativeObject conformsToProtocol:@protocol(KirinExtensionOnMainThread)]
        ) {
        // Dispatch to main/UI thread
        self.dispatchQueue = nil;
    } else if ([nativeObject conformsToProtocol:@protocol(KirinSerialExecute)]) {
        // Dispatch to custom serial queue
        NSString * queueName = [NSString stringWithFormat:@"com.futureplatforms.kirin.%@", self.name];
        self.dispatchQueue = dispatch_queue_create([queueName UTF8String], DISPATCH_QUEUE_SERIAL);
    } else {
        SEL getter = @selector(dispatchQueue);
        
        // See if this object provides its own dispatch queue
        if ([nativeObject_ respondsToSelector:getter]) {
            if (!nativeObject) {
                return;
            }
            
            IMP imp = [nativeObject methodForSelector:getter];
            dispatch_queue_t (*func)(id, SEL) = (void *) imp;
            self.dispatchQueue = (dispatch_queue_t) func(nativeObject, getter);
            NSLog(@"setNativeObject responds to dispatch queue getter: %@, %p", self.name, self.dispatchQueue);
            
        }
        
        if (!self.dispatchQueue) {
            // This object does not provide its own dispatch queue, so use a default global concurrent queue
            self.dispatchQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
        }
        
        
    }
    
    // generate the method names will use to call selectors on this object.
    self.methodsMap = nil;
    [self initializeMethodsMap];
}


- (NSString*) underscoredMethodName: (NSString*) methodName {
    return [[methodName componentsSeparatedByString:@":"] componentsJoinedByString:@"_"];
}


- (NSString*) camelCasedMethodName: (NSString*) methodName {
    NSMutableString *output = [NSMutableString string];
    BOOL makeNextCharacterUpperCase = NO;
    for (NSInteger idx = 0; idx < [methodName length]; idx += 1) {
        unichar c = [methodName characterAtIndex:idx];
        if (c == ':') {
            makeNextCharacterUpperCase = YES;
        } else if (makeNextCharacterUpperCase) {
            [output appendString:[[NSString stringWithCharacters:&c length:1] uppercaseString]];
            makeNextCharacterUpperCase = NO;
        } else {
            [output appendFormat:@"%C", c];
        }
    }
    return output;
}

- (void) initializeMethodsMap {
    if (self.methodsMap) {
        return;
    }
    NSMutableDictionary* methods = [NSMutableDictionary dictionary];
    
    self.methodsMap = methods;
    
    Class class = object_getClass(self.nativeObject);
    while (class) {
        int i=0;
        unsigned int mc = 0;
        Method * mlist = class_copyMethodList(class, &mc);
        
        for(i=0;i<mc;i++) {
            Method method = mlist[i];
            SEL selector = method_getName(method);
            NSString* realMethodName = NSStringFromSelector(selector);

            methods[[self underscoredMethodName:realMethodName]] = realMethodName;
            methods[[self camelCasedMethodName: realMethodName]] = realMethodName;
        }
        
        /* note mlist needs to be freed */
        free(mlist);
        
        class = class_getSuperclass(class);
    }
}

- (NSArray*) methodNames {
    NSMutableArray* methodNames = [NSMutableArray array];
    for (NSString* methodName in [self.methodsMap keyEnumerator]) {
        [methodNames addObject:methodName];
    }
    return methodNames;
    
}


- (SEL) findSelectorFromString: methodName {
    return NSSelectorFromString(self.methodsMap[methodName]);
}

@end
