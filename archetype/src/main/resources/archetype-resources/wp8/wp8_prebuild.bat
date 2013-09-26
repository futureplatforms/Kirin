cd ..

call mvn -pl common/gwt -DgwtModule=${package}.${rootArtifactId}_ie6_%1  -am clean install


set PROJECT_HOME="%cd%"
set GWT_HOME="%PROJECT_HOME%\common\gwt"
set WP8_HOME="%PROJECT_HOME%\wp8\${artifactId}"

rmdir /s /q "%WP8_HOME%\app"
mkdir "%WP8_HOME%\app"
REM  Everything needs COPYING into the project folder every time.  Visual Studio Express does not maintain soft links.
echo f | xcopy "%GWT_HOME%\target\${artifactId}-gwt-1.0-SNAPSHOT\app" "%WP8_HOME%\app" /e /i /y
