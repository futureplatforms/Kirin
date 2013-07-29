# Developing a W8 or WP8 Kirin app

This document outlines the pattern for using Kirin within a WP8 app.

Refer also to the `kirin-hello-world` project in the `demos` folder to see this in action.

## Setup
Follow the instructions in `WINDOWS_SETUP.md` to set up your system and a new W8/WP8
project.

## Initialise Kirin

Ensure your project has **one** call to `Kirin.Initialize()`, passing in a new instance of
`W8KirinPlatform` or `WP8KirinPlatform` as appropriate.  A good place for this to go
might be in the `App()` constructor in `App.xaml.cs`.

## Ensure you can access the generated object bindings

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
`Kirin kirin = Kirin.getInstance();`
and bind the implementation to the module name:
`KirinAssistant ka = kirin.BindScreen(<MyModuleNativeImpl>, "MyModule");`
Finally, create an instance of the module class (located in `fromNative`) like so:
`Generated.MyModule myModule = new MyModule(ka);`

Now you can invoke methods on your Kirin module!

`myModule.helloWorld("amazing");`

and any asynchronous return methods will be invoked by Kirin on your native implementation
which you passed in to `BindScreen`.