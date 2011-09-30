@ECHO OFF

set ABPDIR=%~dp0
set MCPDIR=%ABPDIR%=%\..

CALL :APPLYPATCH
CALL :COPYCLIENT
IF EXIST "%MCPDIR%\src\minecraft_server" CALL :COPYSERVER

CALL :COMPILE
CALL :COPYTEXTURES

pause

GOTO :EOF

REM ---------------------------------------------------------------------------

:APPLYPATCH
	%MCPDIR%\runtime\bin\applydiff.exe -N -t --binary -p2 -u -i %ABPDIR%\mlprop.patch -d %MCPDIR%
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

GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYSERVER

GOTO :EOF

REM ---------------------------------------------------------------------------

:COPYTEXTURES

GOTO :EOF
