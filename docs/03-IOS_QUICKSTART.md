# Kirin on iPhone and iPad

## This document
This document outlines the pattern for using Kirin within an iPhone or iPad app.

Refer also to the `kirin-hello-world` project in the `demos` folder to see this in action.

## Prerequisites
You must have the following installed on your system:

* Eclipse
* Xcode

And you must have the kirin project checked out.

##Getting started

###Build your project's core

Follow the steps in [`CORE-QUICKSTART`](01-CORE-QUICKSTART.md).

###Build the Kirin iOS library 

    cd "Kirin/src/ios/KirinKit"
    
Build this in both `Debug` and `Release` configurations
    
    xcodebuild -sdk iphoneos6.1 -project KirinKit.xcodeproj -target KirinKit -configuration Debug -verbose clean build
    xcodebuild -sdk iphoneos6.1 -project KirinKit.xcodeproj -target KirinKit -configuration Release -verbose clean build

### Create a new app in Xcode in the usual manner.

If you're building an iOS app which targets both iPhone and iPad then create your app in an `ios` folder at the same level as `android`, `common` etc.  If not then put your apps in separate `iPhone` and `iPad` folders.

### Define `KIRIN_HOME` in Xcode
In xcode, type `cmd`-`,`, and select Source Trees.  Ensure `KIRIN_HOME` is defined and points to the folder where Kirin is checked out

### Add the Kirin iOS library to your app

Select your project in xcode, and choose the project name  in `Targets`, then choose the `Build Phases` tab.
In `Link Binary With Libraries`, click `+` then `Add Other…`.  Navigate to the folder where Kirin is checked out, then choose `src/ios/KirinKit/build/Debug-iphoneos/KirinKit.framework`.

#### Add linker flag

Select the project name in `PROJECT`, then choose `Build Settings`.  In `Other Linker Flags` add `-ObjC`.

#### Add Kirin to Framework search paths

Select the project name in `PROJECT`, then choose `Build Settings`.  In `Framework search paths` add `$(KIRIN_HOME)/core/ios/KirinKit/build/Release-iphoneos`.

#### Add other required libraries
In `Targets` -> `Build Phases` -> `Link Binary With Libraries` ensure all these libraries are present:

* KirinKit.framework (added above)
* libsqlite3.dylib
* UIKit.framework
* CoreLocation.framework
* MobileCoreServices.framework
* Foundation.framework
* CoreGraphics.framework

### Add the GWT target's `app` folder to the project

Locate your project's `app` folder in `common/gwt/target/<app-name>-<version>` (in finder).  Drag and drop this onto your xcode project, and choose `Create folder references for any added folders`.

#### Add the bindings to the project's header search paths

Select the project name in `PROJECT`, then choose `Build Settings`.  In `Header Search Paths` add `"$(PROJECT_DIR)/../../common/gwt/target/<project-name>-<version>/app/bindings/ios`.

### Add Pre-build event command line:

 Select the project name  in `Targets`, then choose the `Build Phases` tab.  Choose `Add Build Phase` -> `Add Run Script`, then in `Run Script` add the lines:
 
    cd ../..
    mvn clean install -pl common/gwt
    
(This line will change once Hoskins has figured out how to invoke Maven so that GWT 
only builds when needed.)

This ensures only the `gwt` project and its dependencies are built (and not the android project).

Place this `Run Script` above `Compile Sources` (drag and drop).

You can rename this `Run Script` phase to `Kirin Build` or something similar.

### Ensure you can access the generated object bindings

You should have added your GWT project's `app` folder to the xcode project, this 
should contain the folder `BINDINGS/ios`, this will contain another two folders 
`toNative` and `fromNative`.  

### `fromNative` and `toNative`

`fromNative` contains an Objective C protocol for each of your Kirin modules.
`toNative` contains an Objective C protocol for each of your Kirin natives.

You must provide an implementation of each module's native component (in 
`toNative`, and ought to end with `native`).

### Implement `TestModule`

Your iPhone project should provide a `ViewController` object.  This will be our `TestModule` implementation.

In `ViewController.h` import `TestModuleNative` like so:

    #import "toNative/TestModuleNative.h"

And specify `TestModuleNative` on the class like so:

    @interface ViewController : UIViewController <TestModuleNative>

In `ViewController.m` we must provide an implementation of `TestModuleNative`'s `testyNativeMethod` method:

    - (void) testyNativeMethod: (NSString*) str {
        NSLog(@"testyNativeMethod: %@", str);
    }

In `ViewController.m`, import `TestModule`, and also the Kirin `NSObject` category:

    #import <KirinKit/NSObject+Kirin.h>
    #import "fromNative/TestModule.h"

Declare a `kirinModule` property in the `@interface` part:

    @interface ViewController ()
    @property (strong) id <TestModule> kirinModule;
    @end

In `viewDidLoad`, bind `ViewController` to the `TestModule` class like so:

    [self kirinStartModule:@"TestModule" withProtocol:@protocol(TestModule)];
    

Now, we can access our module through `self.kirinModule`:

    [self.kirinModule testyMethod:@"Hey hey hey!" :1337];

Building and running this app should demonstrate end-to-end native to Kirin method calling.