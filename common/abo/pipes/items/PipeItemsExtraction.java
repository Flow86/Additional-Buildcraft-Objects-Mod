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
import abo.ABO;
import abo.PipeIconProvider;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipe;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.PipeConnectionBans;
import buildcraft.transport.TravelingItem;
import buildcraft.transport.pipes.PipeItemsWood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This pipe will always prefer to insert it's objects into another pipe over one that is not a pipe.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeItemsExtraction extends PipeItemsWood implements IPowerReceptor, IPipeTransportItemsHook {
	private final int standardIconIndex = PipeIconProvider.PipeItemsExtraction;
	private final int solidIconIndex = PipeIconProvider.PipeItemsExtractionSide;

	public PipeItemsExtraction(int itemID) {
		super(itemID);

		PipeConnectionBans.banConnection(PipeItemsExtraction.class, PipeItemsWood.class);

		transport.allowBouncing = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider() {
		return ABO.instance.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return standardIconIndex;
		else {
			int metadata = container.getBlockMetadata();

			if (metadata == direction.ordinal())
				return solidIconIndex;
			else
				return standardIconIndex;
		}
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

		if (!pipesList.isEmpty())
			return pipesList;
		else
			return nonPipesList;
	}

	@Override
	public void entityEntered(TravelingItem item, ForgeDirection orientation) {
	}

	@Override
	public void readjustSpeed(TravelingItem item) {
		transport.defaultReajustSpeed(item);
	}
}
