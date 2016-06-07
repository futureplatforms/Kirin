//
//  KirinWebViewHolder.m
//  KirinKit
//
//  Created by James Hugman on 22/12/2011.
//  Copyright 2011 Future Platforms. All rights reserved.
//

#import "KirinWebViewHolder.h"

#import <UIKit/UIApplication.h>
#import <KirinKit/KirinPaths.h>
#import <KirinKit/KirinConstants.h>

@interface KirinWebViewHolder ()

@property(retain) UIWebView* webView;
@property(retain) id<NativeExecutor> nativeExecutor;

- (void) _initializeWebView: (UIWebView*) webView;

@end


@implementation KirinWebViewHolder

@synthesize webView = webView_;
@synthesize nativeExecutor = nativeExecutor_;


- (id) init {
	return [self initWithWebView:[[UIWebView alloc] init] andNativeContext:nil];
}

- (id) initWithWebView: (UIWebView*) aWebView andNativeContext: (id<NativeExecutor>) nativeExec {
    self = [super init];
	if (self) {
		self.webView = aWebView;
		[self _initializeWebView: aWebView];
		jsQueue = [[NSMutableArray alloc] init];
        self.nativeExecutor = nativeExec;
	}
	return self;
}

- (void) _initializeWebView: (UIWebView*) aWebView {
	aWebView.delegate = self;
	
    NSURL *appURL;
    if (KIRINCONSTANTS.superDevMode) {
        NSLog(@"Welcome to Super Dev Mode!");
        NSLog(@"Ensure your server is running, then use Safari > Develop");
        appURL = [NSURL URLWithString:@"http://127.0.0.1:8888/Kirin.html"];
    } else {
        NSString* startPage = [KirinPaths indexFilename];
        appURL = [NSURL URLWithString:startPage];
        if(![appURL scheme])
        {
            NSString* indexPath = [KirinPaths pathForResource:startPage];
            appURL = [NSURL fileURLWithPath: indexPath];
        }
        DLog(@"Loading %@", startPage);
    }
	
    NSURLRequest *appReq = [NSURLRequest requestWithURL:appURL cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:20.0];

	[aWebView loadRequest:appReq];
}

- (void) _execJSImmediately: (NSString*) js {
    if (KIRINCONSTANTS.logJS) {
        NSLog(@"Javascript: %@", js);
    }
    [self.webView stringByEvaluatingJavaScriptFromString:js];
}

- (void) execJS: (NSString*) js; {
	if (webViewIsReady) {
		[self _execJSImmediately: js];
	} else {
		[jsQueue addObject:js];
	}
    
}

- (BOOL)webView:(UIWebView *)theWebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    
	NSURL *url = [request URL];
//    DLog(@"shouldStartLoadWithRequest %@", [url debugDescription]);
    /*
     * Get Command and Options From URL
     * We are looking for URLS that match native://<Class>/<command>[?<arguments>]
     * We have to strip off the leading slash for the options.
     */
    if ([[url scheme] isEqualToString:@"ready"]) {
        DLog(@"WebView is reported finished. %lu commands to tell JS", (unsigned long)[jsQueue count]);
		[self _execJSImmediately:@"console.log('Webview is loaded')"];
		for (int i=0; i < [jsQueue count]; i++) {
			[self _execJSImmediately:[jsQueue objectAtIndex:i]];
		}
        webViewIsReady = YES;
		[jsQueue removeAllObjects];
        
        [theWebView stringByEvaluatingJavaScriptFromString:@"EXPOSED_TO_NATIVE.runNext();"];
        
        return NO;
    } else if ([[url scheme] isEqualToString:@"native"]) {
        if (KIRINCONSTANTS.logJS) {
            NSLog(@"Incoming: %@", [url debugDescription]);
        }
        
        // Tell the JS code that we've got this command, and we're ready for another
        [theWebView stringByEvaluatingJavaScriptFromString:@"EXPOSED_TO_NATIVE.runNext();"];
        
        NSArray* components = [[url host] componentsSeparatedByString:@"."];
        

        NSString* selectorName = nil;
        NSString* moduleName = nil;
        if (components.count == 2) {
            moduleName = [components objectAtIndex:0];
            selectorName = [components objectAtIndex:1];
        }               


        // Check to see if we are provided a class:method style command.
        [self.nativeExecutor executeCommandFromModule:moduleName
                                            andMethod:selectorName 
                                          andArgsList:[url query]];
		
		return NO;
    } else if (KIRINCONSTANTS.superDevMode &&
               [[url scheme] isEqualToString:@"http"] &&
               [[url host] isEqualToString:@"127.0.0.1"]) {
        return YES;
    } else if (![[url scheme] isEqualToString:@"file"]) {
        /*
         * We don't have a native request, load it in the main Safari browser.
         * XXX Could be security hole.
         */
        
        DLog(@"Kirin::shouldStartLoadWithRequest: Received Unhandled URL %@", url);
        [[UIApplication sharedApplication] openURL:url];
        return NO;
	}
	
	return YES;
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    DLog(@"[ERROR] %@", error);
    if (KIRINCONSTANTS.superDevMode) {
        NSLog(@"SuperDevMode error.  Please ensure you have run the dev mode server, e.g. superdev.sh");
    }
}

@end
