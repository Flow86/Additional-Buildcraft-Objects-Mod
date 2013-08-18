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
 * This pipe will bounce the items back if not powered.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeItemsBounce extends ABOPipe<PipeTransportItems> implements IPipeTransportItemsHook {
	private final int openTexture = PipeIconProvider.PipeItemsBounce;
	private final int closedTexture = PipeIconProvider.PipeItemsBounceClosed;

	public PipeItemsBounce(int itemID) {
		super(new PipeTransportItems(), itemID);
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (container != null && container.worldObj != null)
			return (container.worldObj.isBlockIndirectlyGettingPowered(container.xCoord, container.yCoord, container.zCoord) ? openTexture : closedTexture);
		return closedTexture;
	}

	@Override
	public LinkedList<ForgeDirection> filterPossibleMovements(LinkedList<ForgeDirection> possibleOrientations, Position pos, TravelingItem item) {

		if (container.worldObj.isBlockIndirectlyGettingPowered(container.xCoord, container.yCoord, container.zCoord))
			return possibleOrientations;

		// if unpowered - reverse all items
		LinkedList<ForgeDirection> reverse = new LinkedList<ForgeDirection>();

		reverse.add(pos.orientation.getOpposite());

		return reverse;
	}

	@Override
	public void entityEntered(TravelingItem item, ForgeDirection orientation) {
	}

	@Override
	public void readjustSpeed(TravelingItem item) {
		transport.defaultReajustSpeed(item);
	}
}
