#!/bin/bash

VERSION=$*
MOD_DIR=$(pwd)
MCP_DIR=$MOD_DIR/..

if [ -z "$VERSION" ] ; then
	echo "usage: $0 [version]" >&2
	exit 1
fi

pushd $MCP_DIR
	./recompile.sh || exit 1
	./reobfuscate.sh || exit 1
popd

cp -va README.txt $MCP_DIR/reobf/minecraft
rm -rf $MCP_DIR/reobf/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
mkdir -p $MCP_DIR/reobf/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui
cp -va src/AdditionalBuildcraftObjects/gui/*.png $MCP_DIR/reobf/minecraft/net/minecraft/src/AdditionalBuildcraftObjects/gui

list="README.txt net/minecraft/src/AdditionalBuildcraftObjects/gui/*.png"

pushd src
	for I in $(find -type f -name \*.java)
	do
		list="$list $(echo $I | sed "s/.java/*.class/g")"
	done
popd

pushd $MCP_DIR/reobf/minecraft
	zip $MCP_DIR/../additionalbuildcraftobjects-$VERSION.zip $list || exit 1
popd

exit 0
