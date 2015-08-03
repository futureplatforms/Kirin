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

#import "KirinKit/JSContext.h"
#import "KirinKit/NativeContext.h"

#import "KirinState.h"

@interface Kirin ()

- (void) ensureStarted;

@property(nonatomic, retain) JSContext* jsContext;
@property(nonatomic, retain) NativeContext* nativeContext;

@property(nonatomic, retain) KirinState* state;

@end



@implementation Kirin

SYNTHESIZE_SINGLETON_FOR_CLASS(Kirin)

- (id) init {
    UIWebView* aWebView = [[UIWebView alloc] init];
	return [self initWithWebView:aWebView];
}

- (id) initWithWebView: (UIWebView*) aWebView {
    self = [super init];
	if (self) {

        self.nativeContext = [[NativeContext alloc] init];

        [self.nativeContext registerNativeObject:[[DebugConsole alloc] init] asName:@"DebugConsole"];
        
        self.state = [KirinState initialState];
        self.state.dropbox = [[KirinDropbox alloc] init];

        // TODO deprecate the use of self.dropbox, and pass around KirinState instead.
        self.dropbox = self.state.dropbox;
        
        // the webview needs to be able to call out to native using the nativeContext.
        KirinWebViewHolder* webViewHolder = [[KirinWebViewHolder alloc] initWithWebView:aWebView andNativeContext: self.nativeContext];
        
        self.jsContext = [[JSContext alloc] initWithJSExecutor: webViewHolder];
        // Debug on port 9999
        // http://atnan.com/blog/2011/11/17/enabling-remote-debugging-via-private-apis-in-mobile-safari/
#if defined(__APPLE__) && TARGET_IPHONE_SIMULATOR
        if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 5.0) {
            // turn on Safari debugging, in iOS 5.0+ on the simulator.
            [NSClassFromString(@"WebView") performSelector:@selector(_enableRemoteInspector)];
        }

#endif
	}
	return self;
}   

- (KirinHelper*) bindObject: (id) nativeObject toModule:(NSString*) moduleName {
    [self ensureStarted];
    return [[KirinHelper alloc] initWithModuleName:moduleName
                                   andNativeObject:nativeObject 
                                      andJsContext:self.jsContext 
                                  andNativeContext:self.nativeContext
                                        andState:self.state];
}

- (KirinUiFragmentHelper*) bindUiFragment: (id) nativeObject toModule:(NSString*) moduleName {
    [self ensureStarted];
    return [[KirinUiFragmentHelper alloc] initWithModuleName:moduleName
                                              andNativeObject:nativeObject 
                                                 andJsContext:self.jsContext 
                                             andNativeContext:self.nativeContext
                                                   andState:self.state];
    
}

- (KirinScreenHelper*) bindScreen: (id) nativeObject toModule:(NSString*) moduleName {
    [self ensureStarted];

    return [[KirinScreenHelper alloc] initWithModuleName:moduleName
                                              andNativeObject:nativeObject 
                                                 andJsContext:self.jsContext 
                                             andNativeContext:self.nativeContext
                                                   andState:self.state];
}

- (KirinExtensionHelper*) bindService: (id) nativeObject toModule:(NSString*) moduleName {
    // we don't want to ensureStarted here, because this will be adding services, 
    // and services is what we're starting.
    return [[KirinExtensionHelper alloc] initWithModuleName:moduleName
                                          andNativeObject:nativeObject 
                                             andJsContext:self.jsContext 
                                         andNativeContext:self.nativeContext
                                               andState:self.state];
}

- (KirinAppDelegateHelper*) bindAppDelegate: (id) nativeObject toModule: (NSString*) moduleName {
    [self ensureStarted];
    
    return [[KirinAppDelegateHelper alloc] initWithModuleName:moduleName
                                          andNativeObject:nativeObject 
                                             andJsContext:self.jsContext 
                                         andNativeContext:self.nativeContext
                                                 andState:self.state];
}

#pragma mark -
#pragma mark Managing Services

- (void) ensureStarted {
    // implicitly calls the getter, ensuring a KirinExtensions object exists.
    self.kirinExtensions = [KirinExtensions coreExtensions];
    [self.kirinExtensions ensureStarted];
}

- (void) unloadKirin {
    [self.kirinExtensions unloadServices];
}

@end
