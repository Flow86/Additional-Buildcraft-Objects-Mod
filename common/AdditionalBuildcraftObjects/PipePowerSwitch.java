/** 
 * Copyright (C) 2011 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicGold;
import net.minecraft.src.buildcraft.transport.PipeTransportPower;

/**
 * @author Flow86
 *
 */
public class PipePowerSwitch extends Pipe implements IABOSolid {
	private final int unpoweredTexture = 2 * 16 + 0;
	private final int poweredTexture = 2 * 16 + 1;
	private int nextTexture = unpoweredTexture;
	private boolean powered;

	public PipePowerSwitch(int itemID) {
		super(new PipeTransportPower(), new PipeLogicGold(), itemID);

		((PipeTransportPower) transport).powerResitance = 0.001;
	}

	@Override
	public void prepareTextureFor(Orientations connection) {
		nextTexture = isPowered() ? poweredTexture : unpoweredTexture;
	}

	@Override
	public int getBlockTexture() {
		return nextTexture;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		// TODO: only allow specific sides
		return true;
	}

	@Override
	public boolean isPipeConnected(TileEntity tile) {
		if (!isPowered())
			return false;
		return super.isPipeConnected(tile);
	}

	/**
	 * @return
	 */
	public boolean isPowered() {
		return powered; // worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	@Override
	public void onNeighborBlockChange() {
		super.onNeighborBlockChange();

		boolean lastPowered = powered;
		powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		if(lastPowered != powered) {
			this.container.scheduleNeighborChange();
			this.container.updateEntity();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setBoolean("powered", powered);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		powered = nbttagcompound.getBoolean("powered");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.src.buildcraft.transport.Pipe#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		boolean lastPowered = powered;
		powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		if(lastPowered != powered) {
			this.container.scheduleNeighborChange();
			this.container.updateEntity();
		}
	}

}
