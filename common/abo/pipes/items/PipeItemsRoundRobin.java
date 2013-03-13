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

package abo.pipes.items;

import net.minecraftforge.common.ForgeDirection;
import abo.IconTerrainConstants;
import abo.pipes.ABOPipe;
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
	public int getIconIndex(ForgeDirection direction) {
		return IconTerrainConstants.PipeItemsRoundRobin;
	}
}
