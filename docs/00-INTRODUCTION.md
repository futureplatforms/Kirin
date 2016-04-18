# Kirin introduction

If you're building a mobile app of any complexity which you want to run on more than one
platform, you should consider Kirin.

## What is it?

Kirin allows you to create code libraries in Java and use them on any supported platform.
Currently the supported targets are iOS, Android, and the Windows ecosystem including Windows Phone 8, Windows 8 and Xbox One.

Kirin uses Google Web Toolkit to compile Java into Javascript, and provides a structured means of invoking
methods on your library from iOS and Windows platforms.  Your code actually runs in an
invisible webview.  Kirin does its best to abstract this from you, so your library 
can be integrated into your app as seamlessly as possible. 

##Features
Kirin provides abstractions over native device APIs so they can be accessed in a platform-independent way.  

It's also very easy to add support for new services which may become available, or in fact any code library you may wish to use on your native platform.

Currently the following APIs are provided: 

| | iOS | Android | Windows XAML/C# | Windows HTML/JS |
|---|---|---|---|---|
|Networking |✓|✓|✓|✓|
|SQLite Database |✓|✓|✓||
|Key/value setting persistence |✓|✓|✓|✓|
|Local Notifications |✓|✓|||
|GPS |✓|✓|✓||
|JSON parsing |✓|✓|✓|✓|
|XML parsing |✓|✓|✓|✓|
|Calendar event creation |✓|✓|||


