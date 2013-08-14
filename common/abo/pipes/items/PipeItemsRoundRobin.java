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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TravelingItem;

/**
 * @author Flow86
 * 
 */
public class PipeItemsRoundRobin extends ABOPipe<PipeTransportItems> implements IPipeTransportItemsHook {
	private int lastOrientation = 0;

	public PipeItemsRoundRobin(int itemID) {
		super(new PipeTransportItems(), itemID);
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeItemsRoundRobin;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		lastOrientation = nbttagcompound.getInteger("lastOrientation");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("lastOrientation", lastOrientation);
	}

	@Override
	public LinkedList<ForgeDirection> filterPossibleMovements(LinkedList<ForgeDirection> possibleOrientations, Position pos, TravelingItem item) {
		if (possibleOrientations.size() == 0) {
			return new LinkedList();
		} else {
			lastOrientation = (lastOrientation + 1) % possibleOrientations.size();

			LinkedList<ForgeDirection> newPossibleOrientations = new LinkedList();
			newPossibleOrientations.add(possibleOrientations.get(lastOrientation));
			return newPossibleOrientations;
		}
	}

	@Override
	public void entityEntered(TravelingItem item, ForgeDirection orientation) {
	}

	@Override
	public void readjustSpeed(TravelingItem item) {
		transport.defaultReajustSpeed(item);
	}
}
