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

package abo.pipes.fluids;

import java.util.LinkedList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
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

	public int getFluidId() {
		FluidTankInfo tank = getTank();
		if (tank == null)
			return 0;

		FluidStack fluid = tank.fluid;
		return fluid != null ? fluid.fluidID : 0;
	}

	public int getFluidCapacity() {
		FluidTankInfo tank = getTank();
		if (tank == null)
			return 0;

		return tank.capacity;
	}

	public int getFluidAmount() {
		FluidTankInfo tank = getTank();
		if (tank == null)
			return 0;

		FluidStack fluid = tank.fluid;
		return fluid != null ? fluid.amount : 0;
	}

	public FluidTankInfo getTank() {
		try {
			return c.getInfo();
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				return c.getInfo();
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
public class PipeFluidsBalance extends ABOPipe<PipeTransportFluids> {
	private final int blockTexture = 5 * 16 + 0;

	public PipeFluidsBalance(int itemID) {
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

		int fluidID = 0;
		int fluidAmount = 0;
		int fluidCapacity = 0;
		int fluidNeighbors = 0;

		for (FluidTankInfo tank : transport.getTankInfo(ForgeDirection.UNKNOWN)) {
			if (tank == null)
				continue;

			FluidStack fluid = tank.fluid;
			if (fluid != null)
				fluidAmount += fluid.amount;
		}

		FluidTankInfo tank = transport.getTankInfo(ForgeDirection.UNKNOWN)[ForgeDirection.UNKNOWN.ordinal()];
		FluidStack fluid = null;

		if (tank != null)
			fluid = tank.fluid;

		if (fluid != null && fluid.amount > 0)
			fluidID = fluid.fluidID;

		if (fluidID == 0) {
			for (Neighbor neighbor : neighbors) {
				if (neighbor == null)
					continue;
				fluidID = neighbor.getFluidId();
				if (fluidID != 0)
					break;
			}
		}

		for (Neighbor neighbor : neighbors) {
			if (neighbor == null)
				continue;

			if (neighbor.getFluidCapacity() > 0 && (fluidID == neighbor.getFluidId() || neighbor.getFluidId() == 0)) {

				fluidAmount += neighbor.getFluidAmount();
				fluidCapacity += neighbor.getFluidCapacity();
				fluidNeighbors++;
			}
		}

		// should never happen ...
		if (fluidCapacity == 0 || fluidNeighbors == 0)
			return;

		int liquidAverage = fluidAmount / fluidNeighbors;

		for (Neighbor neighbor : neighbors) {
			int liquidToExtract = neighbor.getFluidAmount() - liquidAverage;

			if (liquidToExtract > 1) {
				// drain tank (read available liquid)
				FluidStack liquidExtracted = neighbor.getTankEntity().drain(liquidToExtract > transport.flowRate ? transport.flowRate : liquidToExtract, false);
				if (liquidExtracted != null) {
					// fill pipe
					int filled = transport.fill(neighbor.getOrientation(), liquidExtracted, true);
					if (filled != 0) {
						// really drain tank
						liquidExtracted = neighbor.getTankEntity().drain(filled, true);
					}
				}

			} else if (liquidToExtract < 1) {
				// drain pipe (read available liquid)
				FluidStack liquidExtracted = transport.drain(neighbor.getOrientation().getOpposite(), liquidToExtract > transport.flowRate ? transport.flowRate
						: liquidToExtract, false);
				if (liquidExtracted != null) {
					// fill tank
					int filled = neighbor.getTankEntity().fill(liquidExtracted, true);
					if (filled != 0) {
						// really drain pipe
						liquidExtracted = transport.drain(neighbor.getOrientation().getOpposite(), filled, true);
					}
				}
			}
		}

	}
}
