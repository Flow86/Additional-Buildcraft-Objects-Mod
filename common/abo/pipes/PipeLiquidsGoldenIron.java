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

package abo.pipes;

import net.minecraftforge.common.ForgeDirection;
import buildcraft.transport.PipeTransportLiquids;
import buildcraft.transport.pipes.PipeLogicIron;

/**
 * @author Flow86
 * 
 */
public class PipeLiquidsGoldenIron extends ABOPipe {

	private final int baseTexture = 3 * 16 + 0;
	private final int sideTexture = baseTexture + 1;

	public PipeLiquidsGoldenIron(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicIron(), itemID);

		((PipeTransportLiquids) transport).flowRate = 80;
		((PipeTransportLiquids) transport).travelDelay = 2;
	}

	@Override
	public int getTextureIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return baseTexture;
		else {
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			if (metadata == direction.ordinal())
				return baseTexture;

			return sideTexture;
		}
	}
}
