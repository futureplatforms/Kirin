//
//  KirinFacebook.h
//  RaceForLife
//
//  Created by Douglas Hoskins on 24/01/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <KirinKit/NSObject+Kirin.h>
#import <KirinKit/KirinGwtServiceStub.h>
#import <toNative/GwtFacebookServiceNative.h>

@interface KirinFacebook : KirinGwtServiceStub<GwtFacebookServiceNative, KirinExtensionOnMainThread>

+ (void)imageToUpload:(UIImage *)image;
+ (UIImage *)uploadImage;

@end
