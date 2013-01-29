@ECHO OFF

set CYGWIN=nontsec

IF EXIST ..\build\forge\mcp\src\minecraft (
	echo Syncing Client
	rsync -arv --existing ../build/forge/mcp/src/minecraft/ client/
	rsync -arv --existing ../build/forge/mcp/src/minecraft/ common/
	rsync -arv --existing ../build/forge/mcp/src/minecraft/lang/ resources/lang/
)

IF EXIST ..\build\forge\mcp\src\minecraft_server (
	PAUSE

	echo Syncing Server
	rsync -arv --existing ../build/forge/mcp/src/minecraft_server/ server/
	rsync -arv --existing ../build/forge/mcp/src/minecraft_server/ common/
)

PAUSE
