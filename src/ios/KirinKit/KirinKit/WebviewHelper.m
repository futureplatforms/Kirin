//
//  WebviewHelper.m
//  Glastonbury
//
//  Created by Douglas Hoskins on 21/03/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "WebviewHelper.h"

@implementation WebviewHelper

+ (NSString*) join: (NSString*) areaPath andFilePath: (NSString*) filePath {
    NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[areaPath componentsSeparatedByString:@"/"]];
    [directoryParts addObjectsFromArray:[filePath componentsSeparatedByString:@"/"]];
    
    return [directoryParts componentsJoinedByString:@"/"];
}

+ (NSString*) pathForResource:(NSString*)resourcepath {
    NSBundle * mainBundle = [NSBundle mainBundle];
    NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[resourcepath componentsSeparatedByString:@"/"]];
    NSString       *filename       = [directoryParts lastObject];
    [directoryParts removeLastObject];
    NSString *directoryStr = [WebviewHelper join: @"components" andFilePath:[directoryParts componentsJoinedByString:@"/"]];
    
    return [mainBundle pathForResource:filename ofType:@"" inDirectory:directoryStr];
}

+ (void) removeDropShadow:(UIWebView*)fromThisWebview {
    for(UIView *view in [[[fromThisWebview subviews] objectAtIndex:0] subviews]) {
        if([view isKindOfClass:[UIImageView class]]) {
            view.hidden = YES;
        }
    }
}
	

@end

