//
//  KirinShare.h
//  RaceForLife
//
//  Created by Douglas Hoskins on 29/01/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import <KirinKit/NSObject+Kirin.h>
#import <KirinKit/KirinGwtServiceStub.h>
#import <toNative/GwtShareServiceNative.h>

#import <Foundation/Foundation.h>
#import <MessageUI/MessageUI.h>
#import <Social/Social.h>


@interface KirinShare : KirinGwtServiceStub <GwtShareServiceNative, KirinExtensionOnMainThread, UIActionSheetDelegate, MFMessageComposeViewControllerDelegate, MFMailComposeViewControllerDelegate>

@end
