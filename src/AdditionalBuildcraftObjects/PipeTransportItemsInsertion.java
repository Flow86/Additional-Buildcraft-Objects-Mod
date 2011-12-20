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

import java.util.LinkedList;

import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.api.EntityPassiveItem;
import net.minecraft.src.buildcraft.api.IPipeEntry;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;

/**
 * This pipe will always prefer to insert it's objects into a tile that is not a
 * pipe over another pipe.
 * 
 * @author Scott Chamberlain (Leftler)
 * 
 */
public class PipeTransportItemsInsertion extends PipeTransportItems {
	@Override
	public LinkedList<Orientations> getPossibleMovements(Position pos, EntityPassiveItem item) {
		LinkedList<Orientations> nonPipesList = new LinkedList<Orientations>();
		LinkedList<Orientations> pipesList = new LinkedList<Orientations>();

		for (Orientations o : Orientations.dirs()) {
			if (o != pos.orientation.reverse() && container.pipe.outputOpen(o)) {
				if (canReceivePipeObjects(o, item)) {

					TileEntity entity = container.getTile(o);
					if (entity instanceof IPipeEntry)
						pipesList.add(o);
					else
						nonPipesList.add(o);
				}
			}
		}

		if (!nonPipesList.isEmpty())
			return nonPipesList;
		else
			return pipesList;
	}
}
