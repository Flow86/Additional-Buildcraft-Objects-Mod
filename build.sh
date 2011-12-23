#!/bin/bash

MOD_DIR=$(pwd)
MCP_DIR=$MOD_DIR/..

cp -va src/* $MCP_DIR/src/minecraft/net/minecraft/src

mkdir -p $MCP_DIR/bin/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
cp -va src/AdditionalBuildcraftObjects/gui/*.png $MCP_DIR/bin/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui

pushd $MCP_DIR
	./recompile.sh || exit 1
popd

exit 0
