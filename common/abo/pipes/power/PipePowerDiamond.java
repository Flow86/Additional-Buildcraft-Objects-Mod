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
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.PipeIconProvider;
import abo.gui.ABOGuiIds;
import abo.pipes.ABOPipe;
import buildcraft.core.network.IClientState;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeTransportPower;

public class PipePowerDiamond extends ABOPipe implements IClientState {

	public boolean isDirty = true;

	public PipePowerDiamond(int itemID) {
		super(new PipeTransportPower(), new PipeLogicPowerDiamond(), itemID);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID < Block.blocksList.length) {
			if (Block.blocksList[entityplayer.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe)
				return false;
		}

		if (super.blockActivated(worldObj, x, y, z, entityplayer))
			return true;

		if (!worldObj.isRemote)
			entityplayer.openGui(ABO.instance, ABOGuiIds.PIPE_DIAMOND_POWER, worldObj, x, y, z);

		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (isDirty) {
			// System.out.println("updateEntity: " + worldObj.isRemote + ": " + isDirty);
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
	public boolean isPipeConnected(TileEntity tile, ForgeDirection side) {
		boolean connected = super.isPipeConnected(tile, side);

		if (container == tile)
			side = side.getOpposite();

		if (connected && ((PipeLogicPowerDiamond) logic).hasConnectionToSide(side))
			return true;
		return false;
	}

	// ICLIENTSTATE
	@Override
	public void writeData(DataOutputStream data) throws IOException {
		NBTTagCompound nbt = new NBTTagCompound();
		logic.writeToNBT(nbt);
		NBTBase.writeNamedTag(nbt, data);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		NBTBase nbt = NBTBase.readNamedTag(data);
		if (nbt instanceof NBTTagCompound) {
			logic.readFromNBT((NBTTagCompound) nbt);
		}
	}
}
