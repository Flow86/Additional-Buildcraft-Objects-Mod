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

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.api.transport.IPipe;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TravelingItem;

/**
 * This pipe will always prefer to insert it's objects into a tile that is not a pipe over another pipe.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeItemsInsertion extends ABOPipe<PipeTransportItems> implements IPipeTransportItemsHook {

	public PipeItemsInsertion(int itemID) {
		super(new PipeTransportItems(), itemID);

		transport.allowBouncing = true;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeItemsInsertion;
	}

	@Override
	public LinkedList<ForgeDirection> filterPossibleMovements(LinkedList<ForgeDirection> possibleOrientations, Position pos, TravelingItem item) {
		LinkedList<ForgeDirection> nonPipesList = new LinkedList<ForgeDirection>();
		LinkedList<ForgeDirection> pipesList = new LinkedList<ForgeDirection>();

		item.blacklist.add(item.input.getOpposite());

		for (ForgeDirection o : possibleOrientations) {
			if (!item.blacklist.contains(o) && container.pipe.outputOpen(o)) {
				if (transport.canReceivePipeObjects(o, item)) {

					TileEntity entity = container.getTile(o);
					if (entity instanceof IPipe)
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

	@Override
	public void entityEntered(TravelingItem item, ForgeDirection orientation) {
	}

	@Override
	public void readjustSpeed(TravelingItem item) {
		transport.defaultReajustSpeed(item);
	}
}
