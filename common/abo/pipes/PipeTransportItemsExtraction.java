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

import java.util.LinkedList;

import net.minecraft.src.TileEntity;
import buildcraft.api.core.Orientations;
import buildcraft.api.core.Position;
import buildcraft.api.transport.IPipeEntry;
import buildcraft.api.transport.IPipedItem;
import buildcraft.transport.EntityData;
import buildcraft.transport.PipeTransportItems;

/**
 * This pipe will always prefer to insert it's objects into another pipe over
 * one that is not a pipe.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeTransportItemsExtraction extends PipeTransportItems {
	@Override
	public LinkedList<Orientations> getPossibleMovements(EntityData data) {
		LinkedList<Orientations> nonPipesList = new LinkedList<Orientations>();
		LinkedList<Orientations> pipesList = new LinkedList<Orientations>();

		data.blacklist.add(data.input.reverse());
		
		for (Orientations o : Orientations.dirs()) {
			if (!data.blacklist.contains(o) && container.pipe.outputOpen(o)) {
				if (canReceivePipeObjects(o, data.item)) {

					TileEntity entity = container.getTile(o);
					if (entity instanceof IPipeEntry)
						pipesList.add(o);
					else
						nonPipesList.add(o);
				}
			}
		}

		if (!pipesList.isEmpty())
			return pipesList;
		else
			return nonPipesList;
	}
}
