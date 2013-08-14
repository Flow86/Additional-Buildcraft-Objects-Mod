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

import java.util.LinkedList;

import net.minecraftforge.common.ForgeDirection;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TravelingItem;

/**
 * This pipe will always prefer to use the opposite direction, so items will go "straight through"
 * 
 * @author blakmajik ported to BC > 2.2 by Flow86
 */
public class PipeItemsCrossover extends ABOPipe<PipeTransportItems> implements IPipeTransportItemsHook {

	public PipeItemsCrossover(int itemID) {
		super(new PipeTransportItems(), itemID);
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeItemsCrossover;
	}

	@Override
	public LinkedList<ForgeDirection> filterPossibleMovements(LinkedList<ForgeDirection> possibleOrientations, Position pos, TravelingItem item) {
		LinkedList<ForgeDirection> list = new LinkedList<ForgeDirection>();

		pos.moveForwards(1.0);
		if (transport.canReceivePipeObjects(pos.orientation, item))
			list.add(pos.orientation);
		else
			list = possibleOrientations;

		return list;
	}

	@Override
	public void entityEntered(TravelingItem item, ForgeDirection orientation) {
	}

	@Override
	public void readjustSpeed(TravelingItem item) {
		transport.defaultReajustSpeed(item);
	}
}
