//
//  KirinExtensionStub.h
//  KirinKit
//
//  Created by James Hugman on 11/01/2012.
//  Copyright 2012 Future Platforms. All rights reserved.
//

#import "KirinExtensionProtocol.h"
#import "KirinExtensionHelper.h"


@interface KirinExtensionStub : NSObject <KirinExtensionProtocol> {
    
}

@property(retain, nonatomic) NSString* moduleName;
@property(retain, nonatomic) NSString* serviceName;
@property(retain, nonatomic) KirinExtensionHelper* kirinHelper;

- (id) initWithModuleName: (NSString*) moduleName;
- (id) initWithServiceName: (NSString*) serviceName;
@end
