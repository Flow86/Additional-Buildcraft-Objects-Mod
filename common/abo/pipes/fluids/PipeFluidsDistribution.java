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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import abo.ABO;
import abo.PipeIconProvider;
import abo.gui.ABOGuiIds;
import abo.network.IFluidSlotChange;
import abo.pipes.ABOPipe;
import buildcraft.core.network.IClientState;
import buildcraft.core.utils.Utils;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeTransportFluids;

/**
 * @author Flow86
 * 
 */
public class PipeFluidsDistribution extends ABOPipe<PipeTransportFluids> implements IClientState, IFluidSlotChange {

	public final Fluid[] fluids = new Fluid[6 * 9];

	public PipeFluidsDistribution(int itemID) {
		super(new PipeTransportFluids(), itemID);

		transport.flowRate = 160;
		transport.travelDelay = 2;
	}

	@Override
	public boolean blockActivated(EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID < Block.blocksList.length) {
			if (Block.blocksList[entityplayer.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe)
				return false;
		}

		if (super.blockActivated(entityplayer))
			return true;

		if (!container.worldObj.isRemote)
			entityplayer.openGui(ABO.instance, ABOGuiIds.PIPE_DIAMOND_LIQUIDS, container.worldObj, container.xCoord, container.yCoord, container.zCoord);

		return true;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		switch (direction) {
		case UNKNOWN:
			return PipeIconProvider.PipeLiquidsDiamondCenter;
		case DOWN:
			return PipeIconProvider.PipeLiquidsDiamondDown;
		case UP:
			return PipeIconProvider.PipeLiquidsDiamondUp;
		case NORTH:
			return PipeIconProvider.PipeLiquidsDiamondNorth;
		case SOUTH:
			return PipeIconProvider.PipeLiquidsDiamondSouth;
		case WEST:
			return PipeIconProvider.PipeLiquidsDiamondWest;
		case EAST:
			return PipeIconProvider.PipeLiquidsDiamondEast;
		default:
			throw new IllegalArgumentException("direction out of bounds");
		}
	}

	@Override
	public boolean outputOpen(ForgeDirection to) {
		if (!super.outputOpen(to))
			return false;

		FluidTankInfo[] tanks = transport.getTankInfo(ForgeDirection.UNKNOWN);

		// center tank
		if (tanks == null || tanks[ForgeDirection.UNKNOWN.ordinal()] == null || tanks[ForgeDirection.UNKNOWN.ordinal()].fluid == null
				|| tanks[ForgeDirection.UNKNOWN.ordinal()].fluid.amount == 0)
			return true;

		Fluid fluidInTank = tanks[ForgeDirection.UNKNOWN.ordinal()].fluid.getFluid();

		boolean[] validDirections = new boolean[ForgeDirection.values().length];
		boolean[] filteredDirections = new boolean[ForgeDirection.values().length];
		boolean filterForLiquid = false;

		// check every direction
		// perhaps we should/can cache this?
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			validDirections[dir.ordinal()] = false;
			filteredDirections[dir.ordinal()] = false;

			if (Utils.checkPipesConnections(container.getTile(dir), container)) {
				for (int slot = 0; slot < 9; ++slot) {
					Fluid fluid = fluids[dir.ordinal() * 9 + slot];

					if (fluid != null) {
						filteredDirections[dir.ordinal()] = true;

						if (fluidInTank.getID() == fluid.getID()) {
							validDirections[dir.ordinal()] = true;
							filterForLiquid = true;
						}
					}
				}
			}
		}

		// the direction is filtered and liquids match
		if (filteredDirections[to.ordinal()] && validDirections[to.ordinal()])
			return true;

		// we havent found a filter for this liquid and the direction is free
		if (!filterForLiquid && !filteredDirections[to.ordinal()])
			return true;

		// we have a filter for the liquid, but not a valid Direction :/
		return false;
	}

	// ICLIENTSTATE
	@Override
	public void writeData(DataOutputStream data) throws IOException {
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < fluids.length; ++i) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			if (fluids[i] != null)
				nbttagcompound.setString("FluidName", fluids[i].getName());
			nbt.setTag("fluidStack[" + i + "]", nbttagcompound);
		}
		NBTBase.writeNamedTag(nbt, data);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		NBTBase nbt = NBTBase.readNamedTag(data);
		if (nbt instanceof NBTTagCompound) {
			for (int i = 0; i < fluids.length; ++i) {
				NBTTagCompound nbttagcompound = ((NBTTagCompound) nbt).getCompoundTag("fluidStack[" + i + "]");
				if (nbttagcompound != null)
					fluids[i] = FluidRegistry.getFluid(nbttagcompound.getString("FluidName"));
			}
		}
	}

	@Override
	public void update(int slot, Fluid fluid) {
		// System.out.println("update: " + worldObj.isRemote + " - " + slot + " to " + stack);

		if (fluid != fluids[slot]) {
			fluids[slot] = fluid;
		}
	}
}
