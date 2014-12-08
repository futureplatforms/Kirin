cd ..

REM  The module itself is specified in the pom.xml for the Kirin-GWT build
call mvn clean install ^
-pl common/kirin-gwt ^
-Dconfiguration=%1 ^
-am


set PROJECT_HOME="%cd%"
set GWT_HOME="%PROJECT_HOME%\common\kirin-gwt"
set WP8_HOME="%PROJECT_HOME%\wp8\KirinWP8"

rmdir /s /q "%WP8_HOME%\app"
mkdir "%WP8_HOME%\app"
REM  Everything needs COPYING into the project folder every time.  Visual Studio Express does not maintain soft links.
echo f | xcopy "%GWT_HOME%\target\bindings" "%WP8_HOME%\app" /e /i /y

cd wp8
CALL cscript include_bindings.js "KirinWP8"
