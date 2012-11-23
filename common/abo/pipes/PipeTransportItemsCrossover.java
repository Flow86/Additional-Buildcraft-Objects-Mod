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

import buildcraft.api.core.Position;
import buildcraft.api.transport.IPipedItem;
import buildcraft.transport.EntityData;
import buildcraft.transport.PipeTransportItems;
import net.minecraftforge.common.ForgeDirection;

/**
 * This pipe will always prefer to use the opposite direction, so items will go
 * "straight through"
 * 
 * @author blakmajik ported to BC > 2.2 by Flow86
 */
public class PipeTransportItemsCrossover extends PipeTransportItems {

	@Override
	public LinkedList<ForgeDirection> getPossibleMovements(EntityData data) {
		LinkedList<ForgeDirection> list = new LinkedList<ForgeDirection>();

		Position newPos = new Position(xCoord, yCoord, zCoord, data.input);
		newPos.moveForwards(1.0);
		if (canReceivePipeObjects(newPos.orientation, data.item))
			list.add(newPos.orientation);
		else
			list = super.getPossibleMovements(data);

		return list;
	}
}
