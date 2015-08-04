//
//  NSObject+Kirin.h
//  KirinKit
//
//  Created by Douglas Hoskins on 12/03/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "KirinKit.h"

@interface NSObject (Kirin)
@property (nonatomic, copy) KirinScreenHelper* kirinHelper;
@property (nonatomic, copy) KirinExtensionHelper* kirinServiceHelper;
- (void) kirinStartModule: (NSString *) moduleName withProtocol: (Protocol *) protocol;
- (void) kirinStartService: (NSString *) moduleName withProtocol: (Protocol *) protocol;
@end
 