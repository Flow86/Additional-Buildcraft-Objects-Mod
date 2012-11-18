@ECHO OFF

set CYGWIN=nontsec

IF EXIST ..\build\mcp\src\minecraft (
	echo Syncing Client
	rsync -arv --existing ../build/mcp/src/minecraft/ client/
	rsync -arv --existing ../build/mcp/src/minecraft/ common/
	rsync -arv --existing ../build/mcp/src/minecraft/lang/ resources/lang/
)

IF EXIST ..\build\mcp\src\minecraft_server (
	PAUSE

	echo Syncing Server
	rsync -arv --existing ../build/mcp/src/minecraft_server/ server/
	rsync -arv --existing ../build/mcp/src/minecraft_server/ common/
)

PAUSE
