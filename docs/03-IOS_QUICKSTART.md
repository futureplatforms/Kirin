# Kirin on iPhone and iPad

## This document
This document outlines the pattern for using Kirin within an iPhone or iPad app.

Refer also to the `kirin-hello-world` project in the `demos` folder to see this in action.

## Prerequisites
You must have the following installed on your system:

* Eclipse
* XCode

And you must have the kirin project checked out.

## Getting started

### Build your project's core

Follow the steps in [`01-CORE-QUICKSTART.md`](01-CORE-QUICKSTART.md).

### Build the Kirin iOS library 

Execute `./rebuild_kirinkit.sh` from within the `Kirin\src` folder.

### Create a new app in Xcode in the usual manner.

If you're building an iOS app which targets both iPhone and iPad then create your app in an `ios` folder at the same level as `android`, `common` etc.  If not then put your apps in separate `iPhone` and `iPad` folders.

### Define `KIRIN_HOME`
In xcode, type `cmd`-`,`, and select Source Trees.  Ensure `KIRIN_HOME` is defined and points to the folder where Kirin is checked out

### Add the Kirin iOS library to your app

Select your project in xcode, and choose the project name  in `Targets`, then choose the `Build Phases` tab.
In `Link Binary With Libraries`, click `+` then `Add Other…`.  Navigate to the folder where Kirin is checked out, then choose `src/ios/KirinKit/build/Debug-iphoneos/KirinKit.framework`.

#### Add linker flag

Select the project name in `PROJECT`, then choose `Build Settings`.  In `Other Linker Flags` add `-ObjC`.

#### Add Kirin to Framework search paths

Select the project name in `PROJECT`, then choose `Build Settings`.  In `Framework search paths` add `$(KIRIN_HOME)/core/ios/KirinKit/build/Release-iphoneos`.


### Add the GWT target's `app` folder to the project

Locate your project's `app` folder in `common/gwt/target/<app-name>-<version>` (in finder).  Drag and drop this onto your xcode project, and choose `Create folder references for any added folders`.

#### Add the `app` folder to the project's header search paths

Select the project name in `PROJECT`, then choose `Build Settings`.  In `Header Search Paths` add `"$(PROJECT_DIR)/../../common/gwt/target/<project-name>-<version>/app/bindings/ios`.

### Add Pre-build event command line:

 Select the project name  in `Targets`, then choose the `Build Phases` tab.  In `Run Script` add the lines:
 
    cd ../..
    mvn install


### Ensure you can access the generated object bindings

You should have added your GWT project's `app` folder to the xcode project, this 
should contain the folder `BINDINGS/ios`, this will contain another two folders 
`toNative` and `fromNative`.  

### `fromNative` and `toNative`

`fromNative` contains an Objective C protocol for each of your Kirin modules.
`toNative` contains an Objective C protocol for each of your Kirin natives.


To use a Kirin module you must create an implementation of its native interface (in 
`toNative`, and ought to end with `native`).

In 
Then, get the Kirin singleton like so:
`Kirin kirin = Kirin.getInstance();`
and bind the implementation to the module name:
`KirinAssistant ka = kirin.BindScreen(<MyModuleNativeImpl>, "MyModule");`
Finally, create an instance of the module class (located in `fromNative`) like so:
`Generated.MyModule myModule = new MyModule(ka);`

Now you can invoke methods on your Kirin module!

`myModule.helloWorld("amazing");`

and any asynchronous return methods will be invoked by Kirin on your native implementation
which you passed in to `BindScreen`.