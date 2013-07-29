# Kirin introduction

If you're building a mobile app of any complexity which you want to run on more than one
platform, you should consider Kirin.

## What is it?

Kirin allows you to create code libraries in Java and use them on any supported platform.
Currently the intended targets are iOS, Android, Windows Phone 8 and Windows 8, however
**any** platform which runs Java or Javascript can be targetted.  This could include:

* web browsers
* blackberry
* firefox OS
* games consoles
* connected TV...

## Javascript?  But the code is written in Java :-S

Indeed.  Kirin uses Google Web Toolkit and provides a structured means of invoking
methods on your library from iOS and Windows platforms.  Your code actually runs in an
invisible webview.  But we go out of our way to abstract this from you, so your library 
can be integrated into your app as seamlessly as possible. 
