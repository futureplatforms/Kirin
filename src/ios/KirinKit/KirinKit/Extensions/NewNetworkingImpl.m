//
//  NewNetworking.m
//  KirinKit
//
//  Created by Douglas Hoskins on 03/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewNetworkingImpl.h"
#import "fromNative/NetworkingService.h"

@interface NewNetworkingImpl()
@property(strong) id<NetworkingService> kirinModule;
@end

@implementation NewNetworkingImpl

@synthesize kirinModule = kirinModule_;

- (id) init {
    return [super initWithServiceName: @"NetworkingService"];
}

- (void) retrieve: (int) ref : (NSString*) method : (NSString*) url : (NSString*) postData : (NSArray*) headers {
    NSLog(@"Yep, that's us trying to retrieve %@, ref %d", url, ref);
}
@end
