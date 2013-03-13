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

package abo.pipes.power;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicIron;
import buildcraft.transport.pipes.PipeLogicWood;
import buildcraft.transport.pipes.PipeStructureCobblestone;

public class PipeLogicPowerIron extends PipeLogicIron {
	@Override
	public void switchPosition() {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		int nextMetadata = metadata;

		for (int l = 0; l < 6; ++l) {
			nextMetadata++;

			if (nextMetadata > 5) {
				nextMetadata = 0;
			}

			TileEntity tile = container.getTile(ForgeDirection.values()[nextMetadata]);

			if (tile instanceof TileGenericPipe) {
				Pipe pipe = ((TileGenericPipe) tile).pipe;
				if (pipe.logic instanceof PipeLogicWood || pipe instanceof PipeStructureCobblestone) {
					continue;
				}
			}

			if (tile instanceof IPowerReceptor) {

				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, nextMetadata, 0);
				container.scheduleRenderUpdate();
				return;
			}
		}
	}
}
