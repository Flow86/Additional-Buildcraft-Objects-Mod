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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;
import abo.ABO;
import abo.PipeIconProvider;
import abo.gui.ABOGuiIds;
import abo.pipes.ABOPipe;
import buildcraft.core.network.IClientState;
import buildcraft.core.utils.Utils;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeTransportLiquids;

/**
 * @author Flow86
 * 
 */
public class PipeLiquidsDiamond extends ABOPipe implements IClientState {

	public PipeLiquidsDiamond(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicLiquidsDiamond(), itemID);

		((PipeTransportLiquids) transport).flowRate = 160;
		((PipeTransportLiquids) transport).travelDelay = 2;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID < Block.blocksList.length) {
			if (Block.blocksList[entityplayer.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe)
				return false;
		}

		if (super.blockActivated(worldObj, x, y, z, entityplayer))
			return true;

		if (!worldObj.isRemote)
			entityplayer.openGui(ABO.instance, ABOGuiIds.PIPE_DIAMOND_LIQUIDS, worldObj, x, y, z);

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

		PipeTransportLiquids transportLiquids = (PipeTransportLiquids) transport;

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
					LiquidStack liquidStack = logicDiamond.liquidStacks[dir.ordinal() * 9 + slot];

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
		logic.writeToNBT(nbt);
		NBTBase.writeNamedTag(nbt, data);
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		NBTBase nbt = NBTBase.readNamedTag(data);
		if (nbt instanceof NBTTagCompound) {
			logic.readFromNBT((NBTTagCompound) nbt);
		}
	}
}
