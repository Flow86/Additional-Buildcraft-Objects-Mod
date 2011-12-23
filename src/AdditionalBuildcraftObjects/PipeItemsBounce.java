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

import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;

public class PipeItemsBounce extends Pipe implements IABOSolid {
	private final int openTexture = 10 * 16 + 0;
	private final int closedTexture = openTexture + 1;
	private int nextTexture = openTexture;

	public PipeItemsBounce(int itemID) {
		super(new PipeTransportItemsBounce(), new PipeLogicStone(), itemID);
	}

	@Override
	public void prepareTextureFor(Orientations connection) {
		if (connection == Orientations.Unknown) {
			nextTexture = (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == PipeTransportItemsBounce.metaOpen ? openTexture : closedTexture);
		} else {
			nextTexture = openTexture;
		}
	}

	@Override
	public int getMainBlockTexture() {
		return nextTexture;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		((PipeTransportItemsBounce)transport).updatePowerMeta(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		// TODO: only allow specific sides
		return true;
	}
}
