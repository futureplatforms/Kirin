rm -rf target
mkdir target
cd KirinKit/build/Release-iphoneos
zip --symlinks -r KirinKit.framework.zip KirinKit.framework
mv KirinKit.framework.zip ../../../target