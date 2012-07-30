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

package AdditionalBuildcraftObjects;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicStone;
import buildcraft.transport.PipeTransportItems;

/**
 * This pipe will always prefer to insert it's objects into a tile that is not a
 * pipe over another pipe.
 * 
 * @author Scott Chamberlain (Leftler)
 *         ported to BC > 2.2 by Flow86
 */
public class PipeItemsInsertion extends Pipe {
	public PipeItemsInsertion(int itemID) {
		super(new PipeTransportItemsInsertion(), new PipeLogicStone(), itemID);
		
		((PipeTransportItems) transport).allowBouncing = true;
	}

	@Override
	public int getBlockTexture() {
		return 8 * 16 + 0;
	}

}
