/** 
 * Copyright (C) 2012 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package AdditionalBuildcraftObjects.pipes;

import AdditionalBuildcraftObjects.ABO;
import buildcraft.api.core.Orientations;
import buildcraft.transport.Pipe;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
public class PipeItemsRoundRobin extends Pipe {

	public PipeItemsRoundRobin(int itemID) {
		super(new PipeTransportItemsRoundRobin(), new PipeLogicStone(), itemID);
	}

	@Override
	public String getTextureFile() {
		return ABO.texturePipes;
	}

	@Override
	public int getTextureIndex(Orientations direction) {
		return 1 * 16 + 0;
	}
}
