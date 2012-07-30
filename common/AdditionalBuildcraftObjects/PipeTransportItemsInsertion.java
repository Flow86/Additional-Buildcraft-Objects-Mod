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

import java.util.LinkedList;

import net.minecraft.src.TileEntity;
import buildcraft.api.EntityPassiveItem;
import buildcraft.api.IPipeEntry;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.transport.PipeTransportItems;

/**
 * This pipe will always prefer to insert it's objects into a tile that is not a
 * pipe over another pipe.
 * 
 * @author Scott Chamberlain (Leftler)
 *         ported to BC > 2.2 by Flow86
 */
public class PipeTransportItemsInsertion extends PipeTransportItems {
	@Override
	public LinkedList<Orientations> getPossibleMovements(Position pos, EntityPassiveItem item) {
		LinkedList<Orientations> nonPipesList = new LinkedList<Orientations>();
		LinkedList<Orientations> pipesList = new LinkedList<Orientations>();

		for (int o = 0; o < 6; ++o) {
			if (Orientations.values()[o] != pos.orientation.reverse()
					&& container.pipe.outputOpen(Orientations.values()[o])) {
				Position newPos = new Position(pos);
				newPos.orientation = Orientations.values()[o];
				newPos.moveForwards(1.0);
				
				if (canReceivePipeObjects(newPos, item)) {

					TileEntity entity = worldObj.getBlockTileEntity((int)newPos.x, (int)newPos.y, (int)newPos.z);
					if (entity instanceof IPipeEntry)
						pipesList.add(newPos.orientation);
					else
						nonPipesList.add(newPos.orientation);
				}
			}
		}

		if (!nonPipesList.isEmpty())
			return nonPipesList;
		else
			return pipesList;
	}
}
