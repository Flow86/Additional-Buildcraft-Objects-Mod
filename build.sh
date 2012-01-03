#!/bin/bash

MOD_DIR=$(pwd)
MCP_DIR=$MOD_DIR/..

[ -z "$BUILD_CLIENT" ] && [ -d $MCP_DIR/src/minecraft ] && BUILD_CLIENT=yes
[ -z "$BUILD_SERVER" ] && [ -d $MCP_DIR/src/minecraft_server ] && BUILD_SERVER=yes

if [ "$BUILD_CLIENT" = "yes" ] ; then

	if [ -d src ] ; then
		rsync --exclude .svn -va src/* $MCP_DIR/src/minecraft/net/minecraft/src

		mkdir -p $MCP_DIR/bin/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
		rsync -va src/AdditionalBuildcraftObjects/gui/*.png $MCP_DIR/bin/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
	else
		rsync --exclude .svn -va common/* $MCP_DIR/src/minecraft/net/minecraft/src
		rsync --exclude .svn -va client/* $MCP_DIR/src/minecraft/net/minecraft/src

		mkdir -p $MCP_DIR/bin/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
		rsync -va client/AdditionalBuildcraftObjects/gui/*.png $MCP_DIR/bin/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
	fi

	sed -i "s/return \"\([0-9.]*\) (MC 1.0.0, BC \([0-9.]*\))\";/return \"\\1-$BUILD_NUMBER (MC 1.0.0, BC $buildcraft_ver_dots)\";/g" $MCP_DIR/src/minecraft/net/minecraft/src/mod_AdditionalBuildcraftObjects.java
fi

if [ "$BUILD_SERVER" = "yes" ] ; then
	rsync --exclude .svn -va common/* $MCP_DIR/src/minecraft_server/net/minecraft/src
	rsync --exclude .svn -va server/* $MCP_DIR/src/minecraft_server/net/minecraft/src

	sed -i "s/return \"\([0-9.]*\) (MC 1.0.0, BC \([0-9.]*\))\";/return \"\\1-$BUILD_NUMBER (MC 1.0.0, BC $buildcraft_ver_dots)\";/g" $MCP_DIR/src/minecraft_server/net/minecraft/src/mod_AdditionalBuildcraftObjects.java
fi

pushd $MCP_DIR
	./recompile.sh || exit 1
popd

exit 0
