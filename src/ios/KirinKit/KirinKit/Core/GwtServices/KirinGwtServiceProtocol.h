//
//  KirinGwtServiceProtocol.h
//  KirinKit
//
//  Created by Douglas Hoskins on 04/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol KirinGwtServiceProtocol <NSObject>
- (void) onRegister;
- (NSString*) serviceName;
@end
