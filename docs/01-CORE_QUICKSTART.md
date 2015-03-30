# Kirin development quick start

Get a new Kirin project with Android, iPhone and WP8 targets.

## Prerequisites

* Check out the kirin-maven branch
* Ensure you have Java installed (`java -version` must work on command line)
* Ensure you have maven installed (`mvn -version` must work on command line)
* For eclipse development, install the [M2E plugin](http://eclipse.org/m2e/)
* For android development, ensure you have the Android SDK installed, and the `ANDROID_HOME` environment variable is set -- make sure this is up-to-date by running `android update sdk`
* Ensure you have your platform's development tools installed (xcode for iOS, MS Visual Studio Express for Windows Phone)

## Correct version of Maven
Homebrew is your friend to get the correct version of Maven installed.  You need `3.1.x` for now.

    brew install homebrew/versions/maven31
    brew link homebrew/versions/maven31

## Build the core kirin libraries

In the project's `src` folder execute `mvn install`.

You should see a bunch of compile logs, followed by a message similar to:

    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO] 
    [INFO] kirin ............................................. SUCCESS [0.376s]
    [INFO] kirin-core ........................................ SUCCESS [3.825s]
    [INFO] kirin-gwt-stub .................................... SUCCESS [0.878s]
    [INFO] kirin-android ..................................... SUCCESS [3.615s]
    [INFO] kirin-gwt ......................................... SUCCESS [1.559s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------ 
    
We have now built the `kirin-core` library (needed by all projects), the `kirin-gwt-stub` and `kirin-android` libraries (needed by Android), and the `kirin-gwt` project (needed by iOS and Windows).

## Install the Kirin template archetype

In the future the Kirin template archetype will hopefully live somewhere like maven central.  Until then you need to install it locally yourself.

In the project's `archetype` folder execute `mvn install`.

You should see compile logs ending with
    
    [INFO] BUILD SUCCESS

## Create a new Kirin project
Create a new folder for your Kirin project.  Enter that folder, then execute `mvn archetype:generate`.  You should see a list of possible archetypes, the last of which should be 

    <number>: local -> com.futureplatforms:kirin-template (kirin-template)

Enter the number on the left.  You'll then be prompted for your project's parameters.  

`groupId` should be an all-lowercase base package name identifying your new project, e.g. `com.futureplatforms.myfirstproj`.  

`artifactId` should be an identifier describing the project, beginning with an uppercase letter, e.g. `MyFirstProj`.

You can select the defaults for the other parameters.

Now, `cd` into the folder that was created with the same name as your `artifactId`, then try building it with `mvn install`.

All being well your new projects should build, ending with:

    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO] 
    [INFO] TestyProj ........................................... SUCCESS [0.165s]
    [INFO] TestyProj-core ...................................... SUCCESS [2.245s]
    [INFO] TestyProj-android ................................... SUCCESS [4.137s]
    [INFO] TestyProj-gwt ....................................... SUCCESS [23.362s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------

In `android/target` you should find an `.apk` for installing on an Android device.  Yes, this means you've created a Kirin app!

You can install this on a device by executing `mvn android:deploy` if you like.

## Create a new project for your native platform

See [`WINDOWS_QUICKSTART`](02-WINDOWS_QUICKSTART.md) or [`IOS_QUICKSTART`](03-IOS_QUICKSTART.md).

