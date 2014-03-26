//
//  KirinUpload.h
//  RaceForLife
//
//  Created by Douglas Hoskins on 04/02/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//


#import <Foundation/Foundation.h>
#import <KirinKit/NSObject+Kirin.h>
#import <KirinKit/KirinGwtServiceStub.h>
#import <toNative/GwtFileUploadServiceNative.h>

@interface KirinUpload : KirinGwtServiceStub<GwtFileUploadServiceNative, KirinExtensionOnMainThread>
+ (void)imageToUpload:(UIImage *)image;
@end
