# Kirin technical overview

This document lists some technical considerations the developer must be aware of before using Kirin.

## Delegates

Delegates encapsulate functionality that is implemented differently on each target platform.  This mainly includes physical features such as network and GPS access.  

However it can in fact include *anything* you would prefer to run natively -- such as a native implementation of a complex library function.

Kirin provides the following delegates by default

* Networking
* Location
* Persisted settings (key, value pairs)
* Deferred operations (replacement for threading)
* Native timed notifications
* JSON parsing
* XML parsing
* Facebook operations
* Console logging

## GWT

**Absolutely all common code in a Kirin project must be GWT compatible.**

This has various implications for the app code you write.  Information about this can be found online <link to follow>, however here are the most important differences to regular Java development.

### GWT operates on source code only

GWT compiles Java **source code** to Javascript.  It **does not** operate on class files.

Therefore any external Java libraries you want to use must themselves be GWT compatible, and be available for inclusion in source form.  Compatible libraries will usually advertise a GWT version with a JAR that includes sources as well as binaries.

### GWT code cannot use reflection

As part of conversion to Javascript, GWT does not retain Java symbol information.  
Therefore, Java reflection classes are not available.  

GWT's substitute for reflection is *deferred binding*.  This is a process by which code
is generated at compile time.  Kirin uses this to generate the iOS and Windows bindings
amongst other things.

However, beware that code generated in this way **will not be available on Android**.

### Other GWT-bits

Web browsers have a limited knowledge of **time and date**, so standard date-manipulation 
libraries are not available.  In the past we have usually used `java.util.Date` and all 
its deprecated methods.  If this is not adequate for your needs, perhaps a Kirin date 
library is required!

**Threading** works differently to usual.  Long-running threads **cannot** be used, you
must use the Kirin threading delegate!

And if you need `com.google.gwt.*` classes you must define a delegate, and provide an
Android implementation also. 

## Android

**Absolutely all common code in a Kirin project must be Android compatible.**

The main consequence of this is that you can't use any GWT-specific code or concepts
in common code.  

### If you use deferred binding you must provide an Android equivalent

<sub>(If you don't know what this means it probably doesn't affect you!)</sub>

Android's version of `GWT.create()` lives in the `kirin-core-gwt-stub` project.  You
must enhance this class to provide an Android implementation of any class you create by
deferred binding.

## TODO
A good task for someone to undertake on this project would be to create a way for Eclipse to prevent you using any classes you're not supposed to.