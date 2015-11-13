//
//  KirinGwtLocation.h
//  KirinKit
//
//  Created by Douglas Hoskins on 01/05/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSObject+Kirin.h"
#import "KirinGwtServiceStub.h"
#import <toNative/GwtLocationServiceNative.h>
#import <CoreLocation/CoreLocation.h>

@interface KirinGwtLocation : KirinGwtServiceStub<GwtLocationServiceNative, CLLocationManagerDelegate, KirinExtensionOnMainThread>

@end
