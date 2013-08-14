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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.transport.PipeTransportFluids;
import buildcraft.transport.TileGenericPipe;

/**
 * @author Flow86
 * 
 */
class Neighbor {
	public Neighbor(IFluidTank container, ForgeDirection orientation) {
		c = container;
		o = orientation;
	}

	public int getLiquidId() {
		IFluidTank tank = getTank();
		if (tank == null)
			return 0;

		FluidStack liquid = tank.getFluid();
		return liquid != null ? liquid.itemID : 0;
	}

	public int getLiquidCapacity() {
		IFluidTank tank = getTank();
		if (tank == null)
			return 0;

		return tank.getCapacity();
	}

	public int getLiquidAmount() {
		IFluidTank tank = getTank();
		if (tank == null)
			return 0;

		FluidStack liquid = tank.getFluid();
		return liquid != null ? liquid.amount : 0;
	}

	public IFluidTank getTank() {
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

	public IFluidTank getTankEntity() {
		return c;
	}

	public ForgeDirection getOrientation() {
		return o;
	}

	private final IFluidTank c;
	private final ForgeDirection o;
}

/**
 * A Pipe to balance/even tanks
 * 
 * 
 * @author Flow86
 * 
 */
public class PipeLiquidsBalance extends ABOPipe<PipeTransportFluids> {
	private final int blockTexture = 5 * 16 + 0;

	public PipeLiquidsBalance(int itemID) {
		super(new PipeTransportFluids(), itemID);

		transport.flowRate = 160;
		transport.travelDelay = 1;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeLiquidsBalance;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		doWork();
	}

	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side) {
		if (tile == null || !(tile instanceof IFluidTank) || tile instanceof TileGenericPipe)
			return false;

		return super.canPipeConnect(tile, side);
	}

	public void doWork() {

		LinkedList<Neighbor> neighbors = new LinkedList<Neighbor>();

		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = container.getTile(o);
			if (tile == null || tile instanceof TileGenericPipe || !(tile instanceof IFluidTank))
				continue;

			Neighbor neighbor = new Neighbor((IFluidTank) tile, o);

			neighbors.add(neighbor);
		}

		PipeTransportFluids ltransport = transport;

		int liquidID = 0;
		int liquidAmount = 0;
		int liquidCapacity = 0;
		int liquidNeighbors = 0;

		for (IFluidTank tank : ltransport.getTanks(ForgeDirection.UNKNOWN)) {
			if (tank == null)
				continue;

			FluidStack liquid = tank.getLiquid();
			if (liquid != null)
				liquidAmount += liquid.amount;
		}

		IFluidTank tank = ltransport.getTanks(ForgeDirection.UNKNOWN)[ForgeDirection.UNKNOWN.ordinal()];
		FluidStack liquid = null;

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
				FluidStack liquidExtracted = neighbor.getTankEntity().drain(neighbor.getOrientation(), liquidToExtract > ltransport.flowRate ? ltransport.flowRate : liquidToExtract, false);
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
				FluidStack liquidExtracted = ltransport.drain(neighbor.getOrientation().getOpposite(), liquidToExtract > ltransport.flowRate ? ltransport.flowRate : liquidToExtract, false);
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
