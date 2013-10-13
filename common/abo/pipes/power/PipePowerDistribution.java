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

package abo.pipes.power;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.PipeIconProvider;
import abo.gui.ABOGuiIds;
import abo.network.IYesNoChange;
import abo.pipes.ABOPipe;
import buildcraft.core.network.IClientState;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeTransportPower;

public class PipePowerDistribution extends ABOPipe<PipeTransportPower> implements IClientState, IYesNoChange {
	public final boolean[] connectionMatrix = { true, true, true, true, true, true };

	public boolean isDirty = true;

	public PipePowerDistribution(int itemID) {
		super(new PipeTransportPower(), itemID);

		transport.powerCapacities.put(PipePowerDistribution.class, 1024);
		transport.initFromPipe(getClass());
	}

	@Override
	public boolean blockActivated(EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID < Block.blocksList.length) {
			if (Block.blocksList[entityplayer.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe)
				return false;
		}

		if (super.blockActivated(entityplayer))
			return true;

		if (!container.worldObj.isRemote)
			entityplayer.openGui(ABO.instance, ABOGuiIds.PIPE_DIAMOND_POWER, container.worldObj, container.xCoord, container.yCoord, container.zCoord);

		return true;
	}

	@Override
	public void update(int slot, boolean state) {
		if (connectionMatrix[slot] != state) {
			connectionMatrix[slot] = state;
			isDirty = true;
			updateEntity();
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (isDirty) {
			// System.out.println("updateEntity: " + worldObj.isRemote + ": " +
			// isDirty);
			container.scheduleNeighborChange();
			updateNeighbors(true);
			isDirty = false;
		}
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		switch (direction) {
		case UNKNOWN:
			return PipeIconProvider.PipePowerDiamondCenter;
		case DOWN:
			return PipeIconProvider.PipePowerDiamondDown;
		case UP:
			return PipeIconProvider.PipePowerDiamondUp;
		case NORTH:
			return PipeIconProvider.PipePowerDiamondNorth;
		case SOUTH:
			return PipeIconProvider.PipePowerDiamondSouth;
		case WEST:
			return PipeIconProvider.PipePowerDiamondWest;
		case EAST:
			return PipeIconProvider.PipePowerDiamondEast;
		default:
			throw new IllegalArgumentException("direction out of bounds");
		}
	}

	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side) {
		return connectionMatrix[side.ordinal()] && super.canPipeConnect(tile, side);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		for (int i = 0; i < 6; ++i)
			nbt.setBoolean("connectionMatrix[" + i + "]", connectionMatrix[i]);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		for (int i = 0; i < 6; ++i) {
			if (nbt.hasKey("connectionMatrix[" + i + "]"))
				connectionMatrix[i] = nbt.getBoolean("connectionMatrix[" + i + "]");
		}
		isDirty = true;
	}

	// ICLIENTSTATE
	@Override
	public void writeData(DataOutputStream data) throws IOException {
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < 6; ++i)
			nbt.setBoolean("connectionMatrix[" + i + "]", connectionMatrix[i]);
		NBTBase.writeNamedTag(nbt, data);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		NBTBase nbt = NBTBase.readNamedTag(data);
		if (nbt instanceof NBTTagCompound) {
			for (int i = 0; i < 6; ++i)
				connectionMatrix[i] = ((NBTTagCompound) nbt).getBoolean("connectionMatrix[" + i + "]");
			isDirty = true;
		}
	}
}
