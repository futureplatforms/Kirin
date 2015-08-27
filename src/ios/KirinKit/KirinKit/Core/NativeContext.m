//
//  NativeContext.m
//  KirinKit
//
//  Created by James Hugman on 22/12/2011.
//  Copyright 2011 Future Platforms. All rights reserved.
//

#import "NativeContext.h"
#import "NativeObjectHolder.h"
#import <UIKit/UIApplication.h>
#import "KirinSynchronousExecute.h"

@interface NativeContext ()

@property(nonatomic, retain) NSMutableDictionary* nativeObjects;
    
@end


@implementation NativeContext

@synthesize nativeObjects = nativeObjects_;

- (id) init {
    return [self initWithDictionary: [NSMutableDictionary dictionary]]; 
}

- (id) initWithDictionary: (NSMutableDictionary*) nativeObjs {
    self = [super init];
    if (self) {
        self.nativeObjects = nativeObjs;
    }
    return self;
}

- (NSArray*) methodNamesFor: (NSString*) moduleName {
    NativeObjectHolder* holder = self.nativeObjects[moduleName];
    if (!holder) {
        [NSException raise:@"KirinNoSuchObjectException" format:@"There is no object registered called %@", moduleName];
    }
    return [holder methodNames];
}

- (void) registerNativeObject: (id) object asName: (NSString*) name {
    self.nativeObjects[name] = [NativeObjectHolder holderForObject:object];
}

- (void) unregisterNativeObject: (NSString*) name {
    if (name) {
        [self.nativeObjects removeObjectForKey:name];
    }
}

- (void) executeCommandFromModule: (NSString*) host andMethod: (NSString*) fullMethodName andArgsList: (NSString*) query {
    NativeObjectHolder* holder = self.nativeObjects[host];
    id obj = holder ? holder.nativeObject : nil;
    
	SEL selector = [holder findSelectorFromString:fullMethodName];
    
    BOOL isBackgroundThread = (holder.dispatchQueue != nil);
	if (obj && [obj respondsToSelector:selector]) {
        void (^block)(void) = ^{
            __block UIBackgroundTaskIdentifier taskId = UIBackgroundTaskInvalid;
            
            if (isBackgroundThread) {
                taskId = [[UIApplication sharedApplication] beginBackgroundTaskWithExpirationHandler:^{
                    [[UIApplication sharedApplication] endBackgroundTask:taskId];
                    taskId = UIBackgroundTaskInvalid;
                }];
            }
            @try {
                NSString* argsJSON = [query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                
                NSMutableArray* arguments = [NSJSONSerialization JSONObjectWithData:[argsJSON dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil];
                
                NSMethodSignature* sig = [[obj class] instanceMethodSignatureForSelector:selector];
                
                NSInvocation* invocation = [NSInvocation invocationWithMethodSignature:sig];
                [invocation retainArguments];
                invocation.selector = selector;
                invocation.target = obj;
                for (NSUInteger i=0, max=[arguments count]; i<max; i++) {
                    // http://stackoverflow.com/q/16928299/64505
                    __unsafe_unretained NSObject* arg = [arguments objectAtIndex:i];
                    
                    char argType = [sig getArgumentTypeAtIndex:i + 2][0];
                    BOOL handled = YES;
                    if (argType == @encode(id)[0]) {
                        if ([arg isKindOfClass:[NSNull class]]) {
                            arg = nil;
                        }
                        [invocation setArgument:&arg atIndex:i + 2];
                    } else if ([arg isKindOfClass: [NSNumber class]]) {
                        NSNumber* num = (NSNumber*) arg;
                        if (argType == @encode(int)[0]) {
                            int value = [num intValue];
                            [invocation setArgument:&value atIndex:i + 2];
                        } else if (argType == @encode(BOOL)[0]) {
                            BOOL value = [num boolValue];
                            [invocation setArgument:&value atIndex:i + 2];
                        } else if (argType == @encode(float)[0]) {
                            float value = [num floatValue];
                            [invocation setArgument:&value atIndex:i + 2];
                        } else if (argType == @encode(double)[0]) {
                            double value = [num doubleValue];
                            [invocation setArgument:&value atIndex:i + 2];
                        } else if (argType == @encode(long)[0]) {
                            long value = [num longValue];
                            [invocation setArgument:&value atIndex:i + 2];
                        } else if (argType == @encode(short)[0]) {
                            short value = [num shortValue];
                            [invocation setArgument:&value atIndex:i + 2];
                        } else {
                            handled = NO;
                        }
                    } else {
                        handled = NO;
                    }
                    
                    if (!handled) {
                        [NSException raise:@"KirinInvocationException"
                                    format:@"Cannot call selector %@ with argument %lu value=%@",
                         fullMethodName, (unsigned long)i, arg];
                    }
                }
                [invocation invoke];
                [invocation self];
            } @catch (NSException* exception) {
                NSLog(@"Exception while executing %@.%@", host, fullMethodName);
                
                // Create a string based on the exception
                NSString *exceptionMessage = [NSString stringWithFormat:@"%@\nReason: %@\nUser Info: %@", [exception name], [exception reason], [exception userInfo]];
                
                // Always log to console for history
                NSLog(@"Exception raised:\n%@", exceptionMessage);
                NSLog(@"Backtrace: %@", [exception callStackSymbols]);
            } @finally {
                if (isBackgroundThread) {
                    [[UIApplication sharedApplication] endBackgroundTask:taskId];
                    taskId = UIBackgroundTaskInvalid;
                }
            }
        };
        
        dispatch_queue_t queue = holder.dispatchQueue;
        if (queue) {
            if ([obj conformsToProtocol:@protocol(KirinSynchronousExecute)]) {
                dispatch_sync(queue, block);
            } else {
                dispatch_async(queue, block);
            }
        } else {
            block();
        }
	} else {                
        // There's no method to call, so throw an error.

         NSString* className = NSStringFromClass([obj class]);

        if (!className) {
            className = host;
        }
        NSLog(@"Class method '%@' not defined in class %@, called from module %@.js", fullMethodName, className, host);
        
        //[NSException raise:NSInternalInconsistencyException format:@"Class method '%@' not defined against class '%@'.", fullMethodName, className];
        
	}
}

@end
