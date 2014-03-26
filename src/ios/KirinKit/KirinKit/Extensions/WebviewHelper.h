//
//  WebviewHelper.h
//  Glastonbury
//
//  Created by Douglas Hoskins on 21/03/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface WebviewHelper : NSObject
+ (NSString*) pathForResource:(NSString*)resourcepath;
+ (void) removeDropShadow:(UIWebView*)fromThisWebview;
@end
