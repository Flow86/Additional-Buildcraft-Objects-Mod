@ECHO OFF

set CYGWIN=nontsec

rsync -arv --existing ../build/mcp/src/minecraft/ client/
rsync -arv --existing ../build/mcp/src/minecraft/ common/

PAUSE

rsync -arv --existing ../build/mcp/src/minecraft_server/ server/
rsync -arv --existing ../build/mcp/src/minecraft_server/ common/

PAUSE
