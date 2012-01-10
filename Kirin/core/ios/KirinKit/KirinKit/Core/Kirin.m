/*
   Copyright 2011 Future Platforms

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/




#import "Kirin.h"

#import "KirinWebViewHolder.h"
#import "DebugConsole.h"

@interface Kirin (private)


@end



@implementation Kirin 

@synthesize dropbox;
@synthesize jsContext;
@synthesize nativeContext;

SYNTHESIZE_SINGLETON_FOR_CLASS(Kirin)

- (id) init {
    UIWebView* aWebView = [[[UIWebView alloc] init] autorelease];
	return [self initWithWebView:aWebView];
}

- (id) initWithWebView: (UIWebView*) aWebView {
    self = [super init];
	if (self) {

        self.nativeContext = [[[NativeContext alloc] init] autorelease];

        [self.nativeContext registerNativeObject:[[[DebugConsole alloc] init] autorelease] asName:@"DebugConsole"];
        
        dropbox = [[[KirinDropbox alloc] init] autorelease];
        // the webview needs to be able to call out to native using the nativeContext.
        KirinWebViewHolder* webViewHolder = [[[KirinWebViewHolder alloc] initWithWebView:aWebView andNativeContext: self.nativeContext] autorelease];
        
        self.jsContext = [[[JSContext alloc] initWithJSExecutor: webViewHolder] autorelease];
        
	}
	return self;
}   

- (KirinHelper*) bindObject: (id) nativeObject toModule:(NSString*) moduleName {
    return [[[KirinHelper alloc] initWithModuleName:moduleName 
                                   andNativeObject:nativeObject 
                                      andJsContext:self.jsContext 
                                  andNativeContext:self.nativeContext
                                        andDropbox:dropbox] autorelease];
}

#pragma mark -
#pragma mark Memory managment
- (void)dealloc
{
    self.jsContext = nil;
    self.nativeContext = nil;
    [dropbox release];
	[super dealloc];
}

	

@end
