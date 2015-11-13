//
//  KirinGwtServiceStub.m
//  KirinKit
//
//  Created by Douglas Hoskins on 04/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "KirinGwtServiceStub.h"
#import "Kirin.h"
#import "NSObject+Kirin.h"

@implementation KirinGwtServiceStub

- (id) initWithServiceName: (NSString*) inServiceName {
    self = [super init];
    if (self) {
        self.serviceName = inServiceName;
    }
    return self;
}

- (void) onRegister {
    if (nil != self.serviceName && nil != self.kirinModuleProtocol) {
        [self kirinStartService:self.serviceName withProtocol:self.kirinModuleProtocol];
    }
}

@end
