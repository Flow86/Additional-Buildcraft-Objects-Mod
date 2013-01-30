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

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import abo.pipes.ABOPipe;
import buildcraft.transport.PipeTransportLiquids;
import buildcraft.transport.pipes.PipeLogicDiamond;

/**
 * @author Flow86
 * 
 */
public class PipeLiquidsDiamond extends ABOPipe {

	public PipeLiquidsDiamond(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicDiamond(), itemID);

		((PipeTransportLiquids) transport).flowRate = 160;
		((PipeTransportLiquids) transport).travelDelay = 2;
	}

	@Override
	public int getTextureIndex(ForgeDirection direction) {
		int baseTexture = 13 * 16 + 0;

		if (direction == ForgeDirection.UNKNOWN)
			return baseTexture;

		return baseTexture + 1 + direction.ordinal();
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

		PipeLogicDiamond logicDiamond = (PipeLogicDiamond) logic;

		boolean filtered = false;
		boolean open = false;
		for (int slot = 0; slot < 9; ++slot) {
			ItemStack stack = logicDiamond.getStackInSlot(to.ordinal() * 9 + slot);

			LiquidStack liquidStack = LiquidContainerRegistry.getLiquidForFilledItem(stack);
			if (liquidStack != null) {
				filtered = true;

				if (liquid.isLiquidEqual(liquidStack))
					open = true;
			}
		}

		return (filtered ? open : true);
	}

}
