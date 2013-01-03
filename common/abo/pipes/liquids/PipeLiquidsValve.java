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

package abo.pipes.liquids;

import java.util.LinkedList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.actions.ABOEnergyPulser;
import buildcraft.api.core.Position;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeTransportLiquids;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLiquidsWood;

public class PipeLiquidsValve extends PipeLiquidsWood {

	private final ABOEnergyPulser pulser;
	private boolean powered;
	private final int baseTexture = 0 * 16 + 0;
	private final int plainTexture = 0 * 16 + 2;

	public PipeLiquidsValve(int itemID) {
		super(itemID);

		((PipeTransportLiquids) transport).flowRate = 160;
		((PipeTransportLiquids) transport).travelDelay = 2;

		pulser = new ABOEnergyPulser(this);
	}

	@Override
	public int getTextureIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return baseTexture + (isPowered() ? 1 : 0);
		else {
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			if (metadata == direction.ordinal())
				return plainTexture + (isPowered() ? 1 : 0);
			else
				return baseTexture + (isPowered() ? 1 : 0);
		}
	}

	@Override
	public int getTextureIndexForItem() {
		return baseTexture;
	}

	@Override
	public String getTextureFile() {
		return ABO.texturePipes;
	}

	public boolean isPowered() {
		return powered;
	}

	public void updateRedstoneCurrent() {
		boolean lastPowered = powered;

		LinkedList<TileGenericPipe> neighbours = new LinkedList<TileGenericPipe>();
		neighbours.add(this.container);

		powered = false;
		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			Position pos = new Position(xCoord, yCoord, zCoord, o);
			pos.moveForwards(1.0);

			TileEntity tile = container.getTile(o);

			if (tile instanceof TileGenericPipe) {
				TileGenericPipe pipe = (TileGenericPipe) tile;
				if (BlockGenericPipe.isValid(pipe.pipe)) {
					neighbours.add(pipe);
					if (pipe.pipe.broadcastRedstone)
						powered = true;
				}
			}
		}

		if (!powered)
			powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

		if (lastPowered != powered) {
			for (TileGenericPipe pipe : neighbours) {
				pipe.scheduleNeighborChange();
				pipe.updateEntity();
			}
		}
	}

	@Override
	public void onNeighborBlockChange(int blockId) {
		super.onNeighborBlockChange(blockId);
		updateRedstoneCurrent();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setBoolean("powered", powered);
		NBTTagCompound nbttagcompoundC = new NBTTagCompound();
		pulser.writeToNBT(nbttagcompoundC);
		nbttagcompound.setTag("Pulser", nbttagcompoundC);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		powered = nbttagcompound.getBoolean("powered");
		NBTTagCompound nbttagcompoundP = nbttagcompound.getCompoundTag("Pulser");
		pulser.readFromNBT(nbttagcompoundP);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		updateRedstoneCurrent();

		if (powered)
			pulser.enablePulse();
		else
			pulser.disablePulse();

		pulser.update();
	}
}
