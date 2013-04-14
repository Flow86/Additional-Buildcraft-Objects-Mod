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

package abo.pipes.power.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import abo.ABOPacketIds;
import buildcraft.core.network.PacketCoordinates;
import buildcraft.transport.TileGenericPipe;

public class PacketYesNoChange extends PacketCoordinates {

	private int slot;
	private boolean state;

	public PacketYesNoChange(int xCoord, int yCoord, int zCoord, int slot, boolean state) {
		super(ABOPacketIds.YesNoChange, xCoord, yCoord, zCoord);
		channel = "ABO";
		this.slot = slot;
		this.state = state;
	}

	public PacketYesNoChange(DataInputStream data) throws IOException {
		readData(data);
	}

	@Override
	public void writeData(DataOutputStream data) throws IOException {
		super.writeData(data);

		data.writeInt(slot);
		data.writeBoolean(state);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		super.readData(data);

		this.slot = data.readInt();
		this.state = data.readBoolean();
	}

	private TileGenericPipe getPipe(World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileGenericPipe))
			return null;

		return (TileGenericPipe) tile;
	}

	public void update(EntityPlayer player) {
		TileGenericPipe pipe = getPipe(player.worldObj, posX, posY, posZ);
		if (pipe == null || pipe.pipe == null)
			return;

		if (!(pipe.pipe.logic instanceof IYesNoChange))
			return;

		((IYesNoChange) pipe.pipe.logic).update(slot, state);
	}
}
