//
//  NativeObjectHolder.h
//  KirinKit
//
//  Created by James Hugman on 29/01/2012.
//  Copyright (c) 2012 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NativeObjectHolder : NSObject

+ (NativeObjectHolder*) holderForObject: (NSObject*) object withName:(NSString*) name;

+ (dispatch_queue_t) dispatchQueue;

@property(weak) NSObject* nativeObject;
@property(nonatomic) dispatch_queue_t dispatchQueue;

- (SEL) findSelectorFromString: methodName;
- (NSArray*) methodNames;

@end
