//
//  KirinGwtServiceStub.m
//  KirinKit
//
//  Created by Douglas Hoskins on 04/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "KirinGwtServiceStub.h"
#import <KirinKit/Kirin.h>
#import "NSObject+Kirin.h"

@implementation KirinGwtServiceStub

@synthesize kirinHelper;
@synthesize serviceName;

- (id) initWithServiceName: (NSString*) serviceName_ {
    self = [super init];
    if (self) {
        self.serviceName = serviceName_;
    }
    return self;
}

- (void) onRegister {
    self.kirinHelper = [KIRIN bindService:self toModule:self.serviceName];
    
    if(nil != self.kirinModuleName && nil != self.kirinModuleProtocol) {
        [self kirinStartModule:self.kirinModuleName withProtocol:self.kirinModuleProtocol];
    }
}

@end
