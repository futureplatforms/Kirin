//
//  Crypto.h
//  KirinKit
//
//  Created by Douglas Hoskins on 11/04/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NSObject+Kirin.h>
#import "KirinGwtServiceStub.h"
#import <toNative/CryptoServiceNative.h>

@interface Crypto : KirinGwtServiceStub<CryptoServiceNative>

@end
