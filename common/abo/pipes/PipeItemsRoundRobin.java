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

import net.minecraftforge.common.ForgeDirection;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
public class PipeItemsRoundRobin extends ABOPipe {

	public PipeItemsRoundRobin(int itemID) {
		super(new PipeTransportItemsRoundRobin(), new PipeLogicStone(), itemID);
	}

	@Override
	public int getTextureIndex(ForgeDirection direction) {
		return 1 * 16 + 0;
	}
}
