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

## This document
This document outlines the pattern for using Kirin within a WP8 app.

The kirin archetype does NOT provide a skeleton Visual Studio project.

* The format of these project files is not well specified and could change in subsequent versions
* The project files contain lots of generated unique IDs which would clearly no longer be unique if you used a project we supplied.  Who knows what the consequences of that would be?  <sub>(Genuine question)</sub>
* It can be quite instructive to add all the components to your project yourself.

Refer to the `kirin-hello-world` project in the `demos` folder to see a working setup in action.

## Prerequisites
You must have the following installed on your system:

* Java 1.6
* Maven 3.1.0 or above (with the bin folder on your path)
* MS Visual Studio Express for Windows Phone

And you must have the kirin project checked out.

## Getting started

### Build your project's core

Follow the steps in [`01-CORE-QUICKSTART.md`](01-CORE-QUICKSTART.md).

### Build the Kirin W8 and WP8 library 

Execute `rebuild_kirinkit.bat` from within the `Kirin\src` folder.

### Create a new app in Visual Studio Express in the usual manner.

This should live in a `wp8` or `w8` folder at the same level as android, core, iPhone etc.  

#### >> Top tip! <<

Visual Studio likes to put this project in a separate folder within your `wp8`/`w8` subfolder.  This means your project path will look like: `ProjectName\wp8\ProjectName\ProjectName`!  Move the `.sln` and `.suo` into `wp8` folder alongside `wp8_prebuild.bat`.


### Add the Kirin W8 or WP8 library to your app

<sub>
Unlike iOS, Windows and Windows Phone seem incapable of referencing libraries using a 
string containing a property.  In other words, it seems impossible to point your WP8 
project to `%KIRIN_HOME%\src\wp8\KirinWP8\Bin\Debug\KirinWP8.dll`.  I have tried manually editing the `.csproj` file to no avail.  If you can discover a way of doing this then it should be preferred!
</sub>

Until then, copy the latest `KirinWP8.dll` into your project folder, and add a reference 
(right click References, choose Add Reference... then Browse).

<sub>
N.B. if you need to do any development to the Kirin core library, you need to build a new
DLL after each change and copy it in again.  It appears that you need to close your 
project in Visual Studio before you can do this.
</sub>

### Add the GWT target's `BINDINGS` folder to the project

### Add Pre-build event command line:

    cd ..\..\..
    wp8_prebuild.bat $(ConfigurationName)

`$(ConfigurationName)` will pass Debug or Release as appropriate into the script to build the correct variety of the project.  

**View** `wp8_prebuild.bat` and ensure your project's correct folder names are in there.

### Ensure the GWT generated files are copied in to the build

Edit your project's `.csproj` file in a text editor.  Near the bottom (but in the `<Project>` block), add the following: 

    <Target Name="AfterResolveReferences">
        <ItemGroup>
            <Content Include="**\*.js" />
            <Content Include="**\*.html" />
            <Content Include="**\*.symbolmap" />
        </ItemGroup>
    </Target>

This ensures that all the GWT generated files (some of whose names change on every build) are copied in to the build.

These files are copied in to the project in the prebuild batch file (specified in the previous step).  Hooking in to `AfterResolveReferences` means these files can be copied at the correct time.

See [this stackoverflow question](http://stackoverflow.com/questions/5926311/) for more information. 


### Add `Json.NET` to your project (Kirin needs this)
* Right-click "References"
* Click "Manage NuGet packages..."
* Select "Json.NET" from "NuGet official package source"

### Initialise Kirin

Ensure your project has **one** call to `Kirin.Initialize()`, passing in a new instance of
`W8KirinPlatform` or `WP8KirinPlatform` as appropriate.  A good place for this to go
might be in the `App()` constructor in `App.xaml.cs`.

### Ensure you can access the generated object bindings

You should have added your GWT project's `app` folder to the Visual Studio project, this 
should contain the folder `bindings\windows`, this will contain another two folders 
`toNative` and `fromNative`.  

### `fromNative` and `toNative`

`fromNative` contains a C# class for each of your Kirin modules.
`toNative` contains a C# interface for each of your Kirin natives.

These classes and interfaces all live under the `Generated` namespace.

To use a Kirin module you must create an implementation of its native interface (in 
`toNative`, and ought to end with `native`).
Then, get the Kirin singleton like so:

    Kirin kirin = Kirin.getInstance();

Bind the implementation to the module name and call `onLoad`:

    KirinAssistant ka = kirin.BindScreen(<MyModuleNativeImpl>, "MyModule");
    ka.onLoad();

Finally, create an instance of the module class (located in `fromNative`) like so:

    Generated.MyModule myModule = new MyModule(ka);

Now you can invoke methods on your Kirin module!

    myModule.helloWorld("amazing");

and any asynchronous return methods will be invoked by Kirin on your native implementation
which you passed in to `BindScreen`.