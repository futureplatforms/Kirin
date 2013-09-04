//
//  KirinGwtServiceStub.m
//  KirinKit
//
//  Created by Douglas Hoskins on 04/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "KirinGwtServiceStub.h"
#import <KirinKit/Kirin.h>

@implementation KirinGwtServiceStub

@synthesize kirinHelper;
@synthesize serviceName;

- (id) initWitServiceName: (NSString*) serviceName_ {
    self = [super init];
    if (self) {
        self.serviceName = serviceName_;
    }
    return self;
}

- (void) onRegister {
    self.kirinHelper = [KIRIN bindService:self toModule:self.serviceName];
}

@end
