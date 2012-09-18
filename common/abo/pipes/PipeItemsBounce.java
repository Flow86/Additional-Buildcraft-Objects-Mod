/** 
 * Copyright (C) 2011-2012 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.pipes;

import buildcraft.api.core.Orientations;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * This pipe will bounce the items back if not powered.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeItemsBounce extends ABOPipe {
	private final int openTexture = 10 * 16 + 0;
	private final int closedTexture = openTexture + 1;

	public PipeItemsBounce(int itemID) {
		super(new PipeTransportItemsBounce(), new PipeLogicStone(), itemID);
	}

	@Override
	public int getTextureIndex(Orientations direction) {

		// TODO: does not work ?!?
		return (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == PipeTransportItemsBounce.metaOpen ? openTexture : closedTexture);
	}

	@Override
	public int getTextureIndexForItem() {
		return openTexture;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		((PipeTransportItemsBounce) transport).updatePowerMeta(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
	}
}
