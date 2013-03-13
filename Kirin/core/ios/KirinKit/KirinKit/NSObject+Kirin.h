//
//  NSObject+Kirin.h
//  KirinKit
//
//  Created by Douglas Hoskins on 12/03/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <KirinKit/KirinKit.h>

@interface NSObject (Kirin)
@property (nonatomic, copy) KirinScreenHelper* kirinHelper;
- (void) kirinStartModule: (NSString *) moduleName withProtocol: (Protocol *) protocol;
@end
