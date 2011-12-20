@ECHO OFF

set ABPDIR=%~dp0
set MCPDIR=%ABPDIR%\..

CALL :APPLYPATCH
CALL :COPYCLIENT
IF EXIST "%MCPDIR%\src\minecraft_server" CALL :COPYSERVER

CALL :COMPILE
CALL :COPYTEXTURES

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
	xcopy /Y /E %ABPDIR%\src\*  %MCPDIR%\src\minecraft\net\minecraft\src
GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYSERVER

GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYTEXTURES
	mkdir %MCPDIR%\bin\minecraft\net\minecraft\src\AdditionalBuildcraftObjects\gui
	xcopy /Y /E %ABPDIR%\src\AdditionalBuildcraftObjects\gui\*.png  %MCPDIR%\bin\minecraft\net\minecraft\src\AdditionalBuildcraftObjects\gui
GOTO :EOF
