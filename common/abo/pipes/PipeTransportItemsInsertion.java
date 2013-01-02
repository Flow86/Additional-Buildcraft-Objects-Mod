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

import java.util.LinkedList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.transport.IPipeEntry;
import buildcraft.transport.EntityData;
import buildcraft.transport.PipeTransportItems;

/**
 * This pipe will always prefer to insert it's objects into a tile that is not a
 * pipe over another pipe.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeTransportItemsInsertion extends PipeTransportItems {

	@Override
	public LinkedList<ForgeDirection> getPossibleMovements(EntityData data) {
		LinkedList<ForgeDirection> nonPipesList = new LinkedList<ForgeDirection>();
		LinkedList<ForgeDirection> pipesList = new LinkedList<ForgeDirection>();

		data.blacklist.add(data.input.getOpposite());

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
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

		if (!nonPipesList.isEmpty())
			return nonPipesList;

		return pipesList;
	}
}
