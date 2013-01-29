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

package abo.pipes.liquids;

import java.util.LinkedList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import abo.pipes.ABOPipe;
import buildcraft.transport.PipeTransportLiquids;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
class Neighbor {
	public Neighbor(ITankContainer container, ForgeDirection orientation) {
		c = container;
		o = orientation;
	}

	public int getLiquidId() {
		ILiquidTank tank = getTank();
		if (tank == null)
			return 0;

		LiquidStack liquid = tank.getLiquid();
		return liquid != null ? liquid.itemID : 0;
	}

	public int getLiquidCapacity() {
		ILiquidTank tank = getTank();
		if (tank == null)
			return 0;

		return tank.getCapacity();
	}

	public int getLiquidAmount() {
		ILiquidTank tank = getTank();
		if (tank == null)
			return 0;

		LiquidStack liquid = tank.getLiquid();
		return liquid != null ? liquid.amount : 0;
	}

	public ILiquidTank getTank() {
		try {
			return c.getTanks(o.getOpposite())[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				return c.getTanks(ForgeDirection.UNKNOWN)[0];
			} catch (ArrayIndexOutOfBoundsException f) {
				return null;
			}
		}
	}

	public ITankContainer getTankEntity() {
		return c;
	}

	public ForgeDirection getOrientation() {
		return o;
	}

	private final ITankContainer c;
	private final ForgeDirection o;
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
	public int getTextureIndex(ForgeDirection direction) {
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
	public boolean isPipeConnected(TileEntity tile, ForgeDirection side) {
		if (tile == null || !(tile instanceof ITankContainer) || tile instanceof TileGenericPipe)
			return false;

		return super.isPipeConnected(tile, side);
	}

	public void doWork() {

		LinkedList<Neighbor> neighbors = new LinkedList<Neighbor>();

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = container.tileBuffer[o.ordinal()].getTile();
			if (tile == null || tile instanceof TileGenericPipe || !(tile instanceof ITankContainer))
				continue;

			Neighbor neighbor = new Neighbor((ITankContainer) tile, o);

			neighbors.add(neighbor);
		}

		PipeTransportLiquids ltransport = (PipeTransportLiquids) transport;

		int liquidID = 0;
		int liquidAmount = 0;
		int liquidCapacity = 0;
		int liquidNeighbors = 0;

		for (ILiquidTank tank : ltransport.getTanks(ForgeDirection.UNKNOWN)) {
			if (tank == null)
				continue;

			LiquidStack liquid = tank.getLiquid();
			if (liquid != null)
				liquidAmount += liquid.amount;
		}

		ILiquidTank tank = ltransport.getTanks(ForgeDirection.UNKNOWN)[ForgeDirection.UNKNOWN.ordinal()];
		LiquidStack liquid = null;

		if (tank != null)
			liquid = tank.getLiquid();

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

			if (neighbor.getLiquidCapacity() > 0 && (liquidID == neighbor.getLiquidId() || neighbor.getLiquidId() == 0)) {

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
				LiquidStack liquidExtracted = ltransport.drain(neighbor.getOrientation().getOpposite(),
						liquidToExtract > ltransport.flowRate ? ltransport.flowRate : liquidToExtract, false);
				if (liquidExtracted != null) {
					// fill tank
					int filled = neighbor.getTankEntity().fill(neighbor.getOrientation().getOpposite(), liquidExtracted, true);
					if (filled != 0) {
						// really drain pipe
						liquidExtracted = ltransport.drain(neighbor.getOrientation().getOpposite(), filled, true);
					}
				}
			}
		}

	}
}
