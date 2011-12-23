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

import net.minecraft.src.buildcraft.api.EntityPassiveItem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;

/**
 * This pipe will bounce the items back if not powered.
 * 
 * @author Scott Chamberlain (Leftler)
 *         ported to BC > 2.2 by Flow86
 */
public class PipeTransportItemsBounce extends PipeTransportItems {
	public static final int metaClosed = 0;
	public static final int metaOpen = 1;

	@Override
	public LinkedList<Orientations> getPossibleMovements(Position pos, EntityPassiveItem item) {
		LinkedList<Orientations> list;
		boolean powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

		if (powered)
			list = super.getPossibleMovements(pos, item);
		else {
			list = new LinkedList<Orientations>();
			list.add(pos.orientation.reverse());
		}

		updatePowerMeta(powered);

		return list;
	}

	void updatePowerMeta(boolean powered) {
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int newMeta = meta;

		if (powered)
			newMeta = metaOpen;
		else
			newMeta = metaClosed;

		if (newMeta != meta) {
			worldObj.setBlockMetadata(xCoord, yCoord, zCoord, newMeta);
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
	}
}
