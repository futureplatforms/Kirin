:: build the windows stuffs
cd wp8-xaml-cs
call wp8_prebuild.bat Release

c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild /t:Clean,Build /property:Platform=ARM KirinWP8\KirinWP8.csproj 
c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild /t:Clean,Build /property:Platform=x86 KirinWP8\KirinWP8.csproj 
:: c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild w8\w8\KirinW8\KirinW8.csproj /t:Clean,Build
cd ..
