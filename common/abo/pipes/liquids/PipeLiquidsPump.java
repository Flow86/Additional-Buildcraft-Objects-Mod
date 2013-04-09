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

import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidStack;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.transport.PipeTransportLiquids;

/**
 * a pump pipe like in AdditionalPipes, but possibility to enable it for
 * different liquids.
 * 
 * @author Flow86
 * 
 */
public class PipeLiquidsPump extends ABOPipe {

	private final Block liquid;

	public PipeLiquidsPump(int itemID) {
		this(itemID, Block.waterStill);
	}

	public PipeLiquidsPump(int itemID, Block liquidStill) {
		super(new PipeTransportLiquids(), new PipeLogicPump(), itemID);
		liquid = liquidStill;

		((PipeTransportLiquids) transport).flowRate = 80;
		((PipeTransportLiquids) transport).travelDelay = 4;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if ((worldObj.isRemote) && worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == liquid.blockID) {
			((PipeTransportLiquids) transport).fill(ForgeDirection.UNKNOWN, new LiquidStack(liquid, 100), true);
		}
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeLiquidsPump;
	}
}
