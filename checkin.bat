@ECHO OFF

set CYGWIN=nontsec

rsync -arv --existing ../src/minecraft/net/minecraft/src/ src/

PAUSE
