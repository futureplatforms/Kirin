//
//  UIViewController+Hybrid.m
//  Glastonbury
//
//  Created by Douglas Hoskins on 20/04/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "UIViewController+Hybrid.h"

@implementation UIViewController(Hybrid)

- (void) tellWebview: (NSString*) javascript {
    SEL getWebView = NSSelectorFromString(@"webView");
    SEL stringBEJSFString = NSSelectorFromString(@"stringByEvaluatingJavaScriptFromString:");
    
    if ([self respondsToSelector:getWebView]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
        id webView = [self performSelector:getWebView];
        [webView performSelector:stringBEJSFString withObject:javascript];
#pragma clang diagnostic pop
    }
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSURL *url = [request URL];
    NSString *scheme = [url scheme];
    
    if ([scheme isEqualToString:@"native"]) {
        [webView stringByEvaluatingJavaScriptFromString:@"window.api.setReady(true);"];
        NSString* method = [url host];
        NSString* params = [[url path] substringFromIndex:1];
        SEL getKirinModule = NSSelectorFromString(@"kirinModule");
        SEL webViewSaid = NSSelectorFromString(@"webViewSaid::");
        if([self respondsToSelector:getKirinModule]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
            id kirinModule = [self performSelector:getKirinModule];
            [kirinModule performSelector:webViewSaid withObject:method withObject:params];
#pragma clang diagnostic pop
        }
    }
    
    if ([scheme isEqualToString:@"file"]) {
        return YES;
    }
    return NO;
}

@end
