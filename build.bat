@ECHO OFF

set ABPDIR=%~dp0
set MCPDIR=%ABPDIR%\..

CALL :APPLYPATCH
CALL :COPYCLIENT
IF EXIST "%MCPDIR%\src\minecraft_server" CALL :COPYSERVER

CALL :COPYTEXTURES
CALL :COMPILE

pause

GOTO :EOF

REM ---------------------------------------------------------------------------

:APPLYPATCH
	%MCPDIR%\runtime\bin\applydiff.exe -N -t --binary -p1 -u -i %ABPDIR%\patches\mlprop.patch -d %MCPDIR%
GOTO :EOF

REM ---------------------------------------------------------------------------

:COMPILE
	set OLDCD=%CD%
	cd %MCPDIR%
	cmd /C recompile.bat
	cd %OLDCD%
	set OLDCD=
GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYCLIENT
	xcopy /Y /E %ABPDIR%\common\*  %MCPDIR%\src\minecraft\net\minecraft\src
	xcopy /Y /E %ABPDIR%\client\*  %MCPDIR%\src\minecraft\net\minecraft\src
	del %MCPDIR%\src\minecraft\net\minecraft\src\AdditionalBuildcraftObjects\gui\*.psd
GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYSERVER
	xcopy /Y /E %ABPDIR%\common\*  %MCPDIR%\src\minecraft_server\net\minecraft\src
	xcopy /Y /E %ABPDIR%\server\*  %MCPDIR%\src\minecraft_server\net\minecraft\src
GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYTEXTURES
	mkdir %MCPDIR%\bin\minecraft\net\minecraft\src\AdditionalBuildcraftObjects\gui
	xcopy /Y /E %ABPDIR%\client\AdditionalBuildcraftObjects\gui\*.png  %MCPDIR%\bin\minecraft\net\minecraft\src\AdditionalBuildcraftObjects\gui
GOTO :EOF
