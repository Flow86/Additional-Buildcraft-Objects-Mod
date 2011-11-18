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

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicGold;
import net.minecraft.src.buildcraft.transport.PipeTransportPower;

public class PipePowerSwitch extends Pipe implements IABOSolid {
	private final int unpoweredTexture = 2 * 16 + 0;
	private final int poweredTexture = 2 * 16 + 1;
	private int nextTexture = unpoweredTexture;

	public PipePowerSwitch(int itemID) {
		super(new PipeTransportPower(), new PipeLogicGold(), itemID);

		((PipeTransportPower) transport).powerResitance = 0.001;
	}

	@Override
	public void prepareTextureFor(Orientations connection) {
		nextTexture = isPowered() ? poweredTexture : unpoweredTexture;
	}

	@Override
	public int getMainBlockTexture() {
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
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
}
