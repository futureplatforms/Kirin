mvn clean install -pl common/kirin-gwt -am
cd "ios/KirinKit"
xcodebuild -sdk iphoneos -project KirinKit.xcodeproj -target KirinKit -configuration Debug -verbose clean build
xcodebuild -sdk iphoneos -project KirinKit.xcodeproj -target KirinKit -configuration Release -verbose clean build