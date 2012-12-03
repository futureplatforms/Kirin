WINDOWS_README

Yo.

WP8 and W8 folders contain Kirin implementations for Windows Phone 8 and Windows 8 (XAML/C#) respectively.

Both projects consist of the main library and a test application.

The WP8 project is the "master".  The files in the W8 project are in fact links to the files residing in the WP8 project.

Both projects have their own "native" components: both providing their own implementations of IKirinPlatform.  This provides wrappers around components that are similar but different across the two platforms, currently the web browser itself (in which Kirin runs), HTTP and key/value settings.  As more native services become required for Kirin in future, native differences should be factored out and made available by IKirinPlatform.

The initial commit has all services required to build Domino's.

Douglas Hoskins 2012-12-03