@ECHO OFF

set CYGWIN=nontsec

rsync -arv --existing ../src/minecraft/net/minecraft/src/ client/
rsync -arv --existing ../src/minecraft/net/minecraft/src/ common/

rsync -arv --existing ../src/minecraft/net/minecraft_server/src/ server/
rsync -arv --existing ../src/minecraft/net/minecraft_server/src/ common/

PAUSE
