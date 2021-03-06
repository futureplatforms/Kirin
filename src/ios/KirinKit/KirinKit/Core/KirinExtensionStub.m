//
//  KirinExtensionStub.m
//  KirinKit
//
//  Created by James Hugman on 11/01/2012.
//  Copyright 2012 Future Platforms. All rights reserved.
//

#import "KirinExtensionStub.h"
#import "Kirin.h"

@implementation KirinExtensionStub

@synthesize kirinHelper;
@synthesize moduleName;

- (id) initWithModuleName: (NSString*) moduleName_ {
    self = [super init];
    if (self) {
        self.moduleName = moduleName_;
    }
    return self;
}

- (id) initWithServiceName:(NSString *)inServiceName {
    self = [super init];
    if (self) {
        self.serviceName = inServiceName;
    }
    return self;
}

- (void) onLoad {
    self.kirinHelper = [KIRIN bindService:self toModule:self.moduleName];
    [self.kirinHelper onLoad];
}

- (void) onStart {
    [self.kirinHelper onStart];
}

- (void) onStop {
    [self.kirinHelper onStop];
}

- (void) onUnload {
    [self.kirinHelper onUnload];
}

@end
