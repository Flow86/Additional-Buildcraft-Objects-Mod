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
import net.minecraftforge.fluids.FluidStack;
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

	public final FluidStack[] fluidStacks = new FluidStack[6 * 9];
	

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

		PipeTransportFluids transportLiquids = transport;

		ILiquidTank[] tanks = transportLiquids.getTanks(ForgeDirection.UNKNOWN);

		// center tank
		if (tanks == null || tanks[ForgeDirection.UNKNOWN.ordinal()] == null || tanks[ForgeDirection.UNKNOWN.ordinal()].getLiquid() == null
				|| tanks[ForgeDirection.UNKNOWN.ordinal()].getLiquid().amount == 0)
			return true;

		LiquidStack liquid = tanks[ForgeDirection.UNKNOWN.ordinal()].getLiquid();

		PipeLogicLiquidsDiamond logicDiamond = (PipeLogicLiquidsDiamond) logic;

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
					LiquidStack liquidStack = logicDiamond.fluidStacks[dir.ordinal() * 9 + slot];

					if (liquidStack != null) {
						filteredDirections[dir.ordinal()] = true;

						if (liquid.isLiquidEqual(liquidStack)) {
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
		for (int i = 0; i < fluidStacks.length; ++i) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			if (fluidStacks[i] != null)
				fluidStacks[i].writeToNBT(nbttagcompound);
			nbt.setTag("liquidStack[" + i + "]", nbttagcompound);
		}
		NBTBase.writeNamedTag(nbt, data);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		NBTBase nbt = NBTBase.readNamedTag(data);
		if (nbt instanceof NBTTagCompound) {
			for (int i = 0; i < fluidStacks.length; ++i) {
				NBTTagCompound nbttagcompound = ((NBTTagCompound) nbt).getCompoundTag("liquidStack[" + i + "]");
				if (nbttagcompound != null)
					fluidStacks[i] = FluidStack.loadFluidStackFromNBT(nbttagcompound);
			}
		}
	}

	@Override
	public void update(int slot, FluidStack stack) {
		// System.out.println("update: " + worldObj.isRemote + " - " + slot + " to " + stack);

		if (stack != fluidStacks[slot]) {
			fluidStacks[slot] = stack;
		}
	}
}
