//
//  NewSettingsImpl.h
//  KirinKit
//
//  Created by Douglas Hoskins on 27/07/2015.
//  Copyright (c) 2015 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <KirinKit/KirinKit.h>
#import <NSObject+Kirin.h>
#import "KirinGwtServiceStub.h"
#import "toNative/GwtSettingsServiceNative.h"

@interface NewSettingsImpl : KirinGwtServiceStub<GwtSettingsServiceNative>

@end
