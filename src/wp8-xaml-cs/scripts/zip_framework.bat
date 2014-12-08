:: Make a target folder in the root
rmdir /s /q target
md target

:: This is where the builds are
cd KirinWP8/bin

:: Make the folder that we're going to zip
md artifacts

:: Move the debug and release frameworks into the corresponding folders in artifacts dir
move ARM artifacts
move x86 artifacts

:: Now make it into a jar
cd artifacts
:: Need to find some way of zipping these folders
"%JAVA_HOME%\bin\jar" cf %1-%2.jar ARM x86

:: Move it to the target folder
move %1-%2.jar ../../../target

:: And clean up
cd ..
rmdir /s /q artifacts
