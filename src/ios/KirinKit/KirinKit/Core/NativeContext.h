//
//  NativeContext.h
//  KirinKit
//
//  Created by James Hugman on 22/12/2011.
//  Copyright 2011 Future Platforms. All rights reserved.
//


#import "NativeExecutor.h"

@interface NativeContext : NSObject<NativeExecutor>  {
}

- (id) init;

- (id) initWithDictionary: (NSMutableDictionary*) nativeObjs;

- (void) registerNativeObject: (id) object asName: (NSString*) name;

- (void) unregisterNativeObject: (NSString*) name;

@end
