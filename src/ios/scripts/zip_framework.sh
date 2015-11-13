# Make a target folder in the root
rm -rf target
mkdir target

# This is where the builds are
cd KirinKit/build

# Make the folder that we're going to zip
mkdir artifacts
# Move the debug and release frameworks into the corresponding folders in artifacts dir
cd Release-iphoneos
cp -av KirinKit.framework ../artifacts

# Now make it into a jar
cd ../artifacts
zip --symlinks -r $1-$2.jar *

# Move it to the target folder
mv $1-$2.jar ../../../target

# And clean up
cd ..
rm -rf artifacts
