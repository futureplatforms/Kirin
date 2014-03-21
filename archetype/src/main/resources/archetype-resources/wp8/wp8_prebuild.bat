REM This script should be invoked with a parameter of Debug or Release
REM (this is what %1 on the mvn invoke line refers to)
REM Windows uses backslash for path separators and they need escaping when they come
REM before a maven variable

cd ..

SET CONFIGURATION=%1

IF NOT %CONFIGURATION%=="Debug" (
	IF NOT %CONFIGURATION%=="Release" (
		ECHO "Usage: wp8_prebuild [Debug|Release] :: defaulting to Debug"
		SET CONFIGURATION="Debug"
	)
)

call mvn clean install ^
-pl gwt ^
-Dconfiguration=%1 ^
-DgwtModule=${package}.${rootArtifactId}_ie6_%CONFIGURATION% ^
-Dgwt.compiler.strict=true ^
-am



SET PROJECT_HOME="%cd%"
SET GWT_HOME="%PROJECT_HOME%\common\gwt"
SET WP8_HOME="%PROJECT_HOME%\wp8\\${rootArtifactId}"

rmdir /s /q "%WP8_HOME%\app"
mkdir "%WP8_HOME%\app"
REM  Everything needs COPYING into the project folder every time.  Visual Studio Express does not maintain soft links.
echo f | xcopy "%GWT_HOME%\target\\${rootArtifactId}-gwt-1.0-SNAPSHOT\app" "%WP8_HOME%\app" /e /i /y

cd wp8
CALL cscript include_bindings.js ${rootArtifactId} wp8 debug
