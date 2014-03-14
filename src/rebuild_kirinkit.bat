REM build the windows stuffs
<<<<<<< HEAD
cd wp8
call wp8_prebuild.bat Release

c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild /t:Clean,Build /property:Platform=ARM KirinWP8\KirinWP8.csproj 
c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild /t:Clean,Build /property:Platform=x86 KirinWP8\KirinWP8.csproj 
REM c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild w8\w8\KirinW8\KirinW8.csproj /t:Clean,Build
cd ..
=======
CALL mvn clean install -pl common/kirin-gwt -am
CALL c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild wp8\wp8\KirinWP8\KirinWP8.csproj /t:Clean,Build
REM CALL c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild w8\w8\KirinW8\KirinW8.csproj /t:Clean,Build
>>>>>>> 8e633382ececaa3468651ecd94ead7dd0902857e
