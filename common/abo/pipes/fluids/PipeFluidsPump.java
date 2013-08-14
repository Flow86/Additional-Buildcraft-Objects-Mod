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

import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.core.utils.BlockUtil;
import buildcraft.transport.PipeTransportFluids;

/**
 * a pump pipe like in AdditionalPipes, but possibility to enable it for different liquids (standard one is for water).
 * 
 * Original authors: Zeldo, DaStormBringer, Kyprus and/or tcooc
 * 
 * @author Flow86
 * 
 */
public class PipeFluidsPump extends ABOPipe<PipeTransportFluids> {

	private final Block liquid;

	public PipeFluidsPump(int itemID) {
		this(itemID, Block.waterStill);
	}

	public PipeFluidsPump(int itemID, Block liquidStill) {
		super(new PipeTransportFluids(), itemID);
		liquid = liquidStill;

		transport.flowRate = 80;
		transport.travelDelay = 4;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (container.worldObj.getBlockId(container.xCoord, container.yCoord - 1, container.zCoord) == liquid.blockID) {
			transport.fill(ForgeDirection.DOWN, new FluidStack(BlockUtil.getFluid(liquid.blockID), FluidContainerRegistry.BUCKET_VOLUME / 10), true);
		}
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeLiquidsPump;
	}
}
