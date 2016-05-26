UPGRADING iOS APP TO MAVEN CENTRAL KIRIN
========================================

1.  Create new Other > Aggregate target named Kirin
2.  Add new Run Script build phase
3.  Copy the Kirin run script phase code from the main target
4.  Remove Kirin Run Script phase from the main target
5.  Manage schemes.  Remove schemes that have been auto created for new targets
6.  Add the new Kirin target to the Build phase of the main app scheme, ensure it's the FIRST in the list
7.  IMPORTANT: Uncheck Parallelize Build
8.  Remove existing KirinKit.framework from Frameworks
9.  Run mvn clean install -pl ios -am
10. Copy lib/Kirin/KirinKit.framework into Frameworks
11. Add "$(SRCROOT)/../../lib/Kirin" to Framework Search Paths
12. Change the Header Search Paths, the name of the app will be added in
13. In main project > Info, create new Configuration called "Debug: SDM", duplicated from Debug
14. In main project, build settings, preprocessor macros, Debug: SDM, add SDM=1 
15. If using CocoaPods, add a Debug: SDM target there too.
16. Create new scheme, "App name: SDM". 
17. Add the following to the start of AppDelegate:didFinishLaunchingWithOptions:
    
        #if DEBUG
            KIRINCONSTANTS.loggingEnabled = YES;
        #endif
    
        #if SDM
        #if TARGET_IPHONE_SIMULATOR
            KIRINCONSTANTS.superDevMode = YES;
        #else
            NSLog(@"SuperDevMode only works on simulator!!");
            return NO;
        #endif
        #endif