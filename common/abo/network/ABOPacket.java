/** 
 * Copyright (C) 2011-2013 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import buildcraft.core.network.PacketCoordinates;
import buildcraft.transport.TileGenericPipe;

public class ABOPacket extends PacketCoordinates {
	public ABOPacket() {
		channel = "ABO";
	}

	public ABOPacket(int id, int xCoord, int yCoord, int zCoord) {
		super(id, xCoord, yCoord, zCoord);
		channel = "ABO";
	}

	protected TileGenericPipe getPipe(World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileGenericPipe))
			return null;

		return (TileGenericPipe) tile;
	}
}
