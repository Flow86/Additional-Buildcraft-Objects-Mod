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
import buildcraft.api.transport.IPipedItem;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * This pipe will bounce the items back if not powered.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeItemsBounce extends ABOPipe implements IPipeTransportItemsHook {
	private final int openTexture = PipeIconProvider.PipeItemsBounce;
	private final int closedTexture = PipeIconProvider.PipeItemsBounceClosed;

	public PipeItemsBounce(int itemID) {
		super(new PipeTransportItems(), new PipeLogicStone(), itemID);
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (worldObj != null)
			return (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) ? openTexture : closedTexture);
		return closedTexture;
	}

	@Override
	public LinkedList<ForgeDirection> filterPossibleMovements(LinkedList<ForgeDirection> possibleOrientations, Position pos, IPipedItem item) {

		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
			return possibleOrientations;

		// if unpowered - reverse all items
		LinkedList<ForgeDirection> reverse = new LinkedList<ForgeDirection>();

		reverse.add(pos.orientation.getOpposite());

		return reverse;
	}

	@Override
	public void entityEntered(IPipedItem item, ForgeDirection orientation) {
	}

	@Override
	public void readjustSpeed(IPipedItem item) {
		((PipeTransportItems) transport).defaultReajustSpeed(item);
	}
}
