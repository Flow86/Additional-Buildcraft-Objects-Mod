@ECHO OFF

set CYGWIN=nontsec

rsync -arv --existing ../../src/minecraft/net/ src/

PAUSE
