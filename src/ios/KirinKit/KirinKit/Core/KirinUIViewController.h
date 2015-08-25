//
//  KirinUIViewControllerViewController.h
//  KirinKit
//
//  Created by James Hugman on 26/04/2012.
//  Copyright (c) 2012 Future Platforms. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "Kirin.h"
#import "KirinScreenHelper.h"

@interface KirinUIViewController : UIViewController

@property(retain, nonatomic) KirinScreenHelper* kirinHelper;

- (void) bindScreenWithoutLoading: (NSString*) moduleName;

- (void) bindScreen: (NSString*) moduleName;

- (id) bindScreen:(NSString *)moduleName withProtocol: (Protocol*) protocol;

@end
