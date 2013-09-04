//
//  KirinGwtServiceStub.h
//  KirinKit
//
//  Created by Douglas Hoskins on 04/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "KirinGwtServiceProtocol.h"
#import "KirinHelper.h"

@interface KirinGwtServiceStub : NSObject<KirinGwtServiceProtocol> {
    
}

@property(retain, nonatomic) NSString* serviceName;
@property(retain, nonatomic) KirinHelper* kirinHelper;

- (id) initWithServiceName: (NSString*) serviceName;
@end
