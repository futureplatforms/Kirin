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



#import "KirinConstants.h"
#import "KirinHelper.h"
#import "KirinUiFragmentHelper.h"
#import "KirinScreenHelper.h"
#import "KirinExtensionHelper.h"
#import "KirinAppDelegateHelper.h"
#import "KirinExtensions.h"

#import "KirinDropbox.h"

#import "SynthesizeSingleton.h"
#import <UIKit/UIWebView.h>

#ifdef __APPLE__
#include "TargetConditionals.h"
#endif




@interface Kirin : NSObject {
}

// publicly available objects.
@property(retain) KirinDropbox* dropbox;

@property(nonatomic, retain) KirinExtensions* kirinExtensions;

@property(nonatomic, retain) NSDictionary* notificationUserData;

- (id) initWithWebView: (UIWebView*) aWebView;

- (KirinHelper*) bindObject: (id) nativeObject toModule:(NSString*) moduleName;

- (KirinUiFragmentHelper*) bindUiFragment: (id) nativeObject toModule:(NSString*) moduleName;

- (KirinScreenHelper*) bindScreen: (id) nativeObject toModule:(NSString*) moduleName;

- (KirinExtensionHelper*) bindService: (id) nativeObject toModule:(NSString*) moduleName;

- (KirinAppDelegateHelper*) bindAppDelegate: (id) nativeObject toModule: (NSString*) moduleName;

- (void) unloadKirin;

SYNTHESIZE_SINGLETON_HEADER_FOR_CLASS(Kirin)

#define KIRIN [Kirin sharedKirin]

@end
