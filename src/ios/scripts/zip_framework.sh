rm -rf target
mkdir target
cd KirinKit/build/Release-iphoneos
zip --symlinks -r $1-$2.zip KirinKit.framework
mv $1-$2.zip ../../../target