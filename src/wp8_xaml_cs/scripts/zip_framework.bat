:: Make a target folder in the root
rmdir target
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
zip --symlinks -r $1-$2.jar Debug Release

:: Move it to the target folder
mv $1-$2.jar ../../../target

:: And clean up
cd ..
rm -rf artifacts
