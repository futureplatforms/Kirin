# Kirin on Windows 8 and Windows Phone 8

## Introduction

This library can be used to build XAML/C# based Windows Phone 8 and Windows 8 Store apps 
(Currently NOT HTML/Javascript).

The WP8 project should be considered the "reference" or "master" of these two.

The differences between the two are outlined in the interface `IKirinPlatform` -- 
it specifies a bunch of interfaces that abstract out the common elements of
components that actually differ slightly between W8 and WP8 -- this includes the 
Web view component, HTTP connections and settings.

This library is currently used to build the Domino's WP8 project.

## Prerequisites
You must have the following installed on your system:
* Java 1.6
* Maven 3.1.0 or above (with the bin folder on your path)
* MS Visual Studio Express for Windows Phone

And you must have the kirin project checked out.

## Getting started

### Build the Kirin core jars

In the folder `Kirin\src\common` execute `mvn clean install`.

### Build your project's GWT target

See the core Kirin/GWT howto for details.

### Build the Kirin W8 and WP8 library 

Execute `rebuild_kirinkit.bat` from within the `Kirin\src` folder.

### Create a new app in Visual Studio Express in the usual manner.

This should live in a `wp8` or `w8` folder at the same level as android, core, iPhone etc.

### Add the Kirin W8 or WP8 library to your app

<sub>
Unlike iOS, Windows and Windows Phone seem incapable of referencing libraries using a 
string containing a property.  In other words, it seems impossible to point your WP8 
project to `%KIRIN_HOME%\src\wp8\KirinWP8\Bin\Debug\KirinWP8.dll`.  I have tried manually
editing the `.csproj` file to no avail.  If you can discover a way of doing this then it 
should be preferred!
</sub>

Until then, copy the latest `KirinWP8.dll` into your project folder, and add a reference 
(right click References, choose Add Reference... then Browse).

<sub>
N.B. if you need to do any development to the Kirin core library, you need to build a new
DLL after each change and copy it in again.  It appears that you need to close your 
project in Visual Studio before you can do this.
</sub>

### Add the GWT target's `app` folder to the project

### Add Pre-build event command line:

`cd ..\..\..\..`
`wp8_prebuild.bat`

**Copy** `wp8_prebuild.bat` into the `wp8` folder and **edit** it to put your project's 
correct folder names in there.