REM build the windows stuffs
CALL mvn clean install -pl common/kirin-gwt -am
CALL c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild wp8\wp8\KirinWP8\KirinWP8.csproj /t:Clean,Build
REM CALL c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild w8\w8\KirinW8\KirinW8.csproj /t:Clean,Build