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

import net.minecraft.src.TileEntity;
import buildcraft.api.core.Orientations;
import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.ITankContainer;
import buildcraft.api.liquids.LiquidStack;
import buildcraft.transport.PipeTransportLiquids;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
class Neighbor {
	public Neighbor(ITankContainer container, Orientations orientation) {
		c = container;
		o = orientation;
	}

	public int getLiquidId() {
		LiquidStack liquid = getTank().getLiquid();
		return liquid != null ? liquid.itemID : 0;
	}

	public int getLiquidCapacity() {
		return getTank().getCapacity();
	}

	public int getLiquidAmount() {
		LiquidStack liquid = getTank().getLiquid();
		return liquid != null ? liquid.amount : 0;
	}

	public ILiquidTank getTank() {
		try {
			return c.getTanks()[o.reverse().ordinal()];
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				return c.getTanks()[0];
			} catch (ArrayIndexOutOfBoundsException f) {
				return null;
			}
		}
	}

	public ITankContainer getTankEntity() {
		return c;
	}

	public Orientations getOrientation() {
		return o;
	}

	private final ITankContainer c;
	private final Orientations o;
}

/**
 * A Pipe to balance/even tanks
 * 
 * 
 * @author Flow86
 * 
 */
public class PipeLiquidsBalance extends ABOPipe {
	private final int blockTexture = 5 * 16 + 0;

	public PipeLiquidsBalance(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicStone(), itemID);

		((PipeTransportLiquids) transport).flowRate = 160;
		((PipeTransportLiquids) transport).travelDelay = 1;
	}

	@Override
	public int getTextureIndex(Orientations direction) {
		return blockTexture;
	}

	@Override
	public int getTextureIndexForItem() {
		return blockTexture;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		doWork();
	}

	@Override
	public boolean isPipeConnected(TileEntity tile) {
		if (tile == null || !(tile instanceof ITankContainer) || tile instanceof TileGenericPipe)
			return false;

		return super.isPipeConnected(tile);
	}

	public void doWork() {

		LinkedList<Neighbor> neighbors = new LinkedList<Neighbor>();

		for (Orientations o : Orientations.dirs()) {
			TileEntity tile = container.tileBuffer[o.ordinal()].getTile();
			if (tile == null || !(tile instanceof ITankContainer))
				continue;

			Neighbor neighbor = new Neighbor((ITankContainer) tile, o);

			neighbors.add(neighbor);
		}

		PipeTransportLiquids ltransport = (PipeTransportLiquids) transport;

		int liquidID = 0;
		int liquidAmount = 0;
		int liquidCapacity = 0;
		int liquidNeighbors = 0;

		for (ILiquidTank tank : ltransport.getTanks()) {
			LiquidStack liquid = tank.getLiquid();
			if (liquid != null)
				liquidAmount += liquid.amount;
		}

		LiquidStack liquid = ltransport.getTanks()[Orientations.Unknown.ordinal()].getLiquid();
		if (liquid != null && liquid.amount > 0)
			liquidID = liquid.itemID;

		if (liquidID == 0) {
			for (Neighbor neighbor : neighbors) {
				if (neighbor == null)
					continue;
				liquidID = neighbor.getLiquidId();
				if (liquidID != 0)
					break;
			}
		}

		for (Neighbor neighbor : neighbors) {
			if (neighbor == null)
				continue;

			if (liquidID == neighbor.getLiquidId() || neighbor.getLiquidId() == 0) {

				liquidAmount += neighbor.getLiquidAmount();
				liquidCapacity += neighbor.getLiquidCapacity();
				liquidNeighbors++;
			}
		}

		// should never happen ...
		if (liquidCapacity == 0 || liquidNeighbors == 0)
			return;

		int liquidAverage = liquidAmount / liquidNeighbors;

		for (Neighbor neighbor : neighbors) {
			int liquidToExtract = neighbor.getLiquidAmount() - liquidAverage;

			if (liquidToExtract > 1) {
				// drain tank (read available liquid)
				LiquidStack liquidExtracted = neighbor.getTankEntity().drain(neighbor.getOrientation(),
						liquidToExtract > ltransport.flowRate ? ltransport.flowRate : liquidToExtract, false);
				if (liquidExtracted != null) {
					// fill pipe
					int filled = ltransport.fill(neighbor.getOrientation(), liquidExtracted, true);
					if (filled != 0) {
						// really drain tank
						liquidExtracted = neighbor.getTankEntity().drain(neighbor.getOrientation(), filled, true);
					}
				}

			} else if (liquidToExtract < 1) {
				// drain pipe (read available liquid)
				LiquidStack liquidExtracted = ltransport.drain(neighbor.getOrientation().reverse(), liquidToExtract > ltransport.flowRate ? ltransport.flowRate
						: liquidToExtract, false);
				if (liquidExtracted != null) {
					// fill tank
					int filled = neighbor.getTankEntity().fill(neighbor.getOrientation().reverse(), liquidExtracted, true);
					if (filled != 0) {
						// really drain pipe
						liquidExtracted = ltransport.drain(neighbor.getOrientation().reverse(), filled, true);
					}
				}
			}
		}

	}
}
