//
//  KirinWebViewHolder.h
//  KirinKit
//
//  Created by James Hugman on 22/12/2011.
//  Copyright 2011 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIWebView.h>
#import "JSExecutor.h"
#import "NativeExecutor.h"

@interface KirinWebViewHolder : NSObject <UIWebViewDelegate, JSExecutor> {

    @private
	NSMutableArray* jsQueue;

    BOOL webViewIsReady;
}



- (id) initWithWebView: (UIWebView*) aWebView andNativeContext: (id<NativeExecutor>) nativeExec;

@end
