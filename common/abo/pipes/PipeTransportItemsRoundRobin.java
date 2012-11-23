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

import net.minecraft.src.NBTTagCompound;
import buildcraft.transport.EntityData;
import buildcraft.transport.PipeTransportItems;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author Flow86
 * 
 */
public class PipeTransportItemsRoundRobin extends PipeTransportItems {
	private int lastOrientation = 0;

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
	public ForgeDirection resolveDestination(EntityData data) {
		LinkedList<ForgeDirection> listOfPossibleMovements = getPossibleMovements(data);

		if (listOfPossibleMovements.size() == 0) {
			return ForgeDirection.UNKNOWN;
		} else {
			lastOrientation = (lastOrientation + 1) % listOfPossibleMovements.size();

			return listOfPossibleMovements.get(lastOrientation);
		}
	}
}
