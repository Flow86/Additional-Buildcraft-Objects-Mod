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

import net.minecraftforge.common.ForgeDirection;
import abo.IconTerrainConstants;
import abo.pipes.ABOPipe;
import buildcraft.BuildCraftTransport;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.PipeTransportPower;

public class PipePowerIron extends ABOPipe implements IPipeTransportPowerHook {

	private final int baseTexture = IconTerrainConstants.PipePowerIron;
	private final int sideTexture = IconTerrainConstants.PipePowerIronSide;

	public PipePowerIron(int itemID) {
		super(new PipeTransportPower(), new PipeLogicPowerIron(), itemID);
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return baseTexture;
		else {
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			if (metadata == direction.ordinal())
				return baseTexture;
			else
				return sideTexture;
		}
	}

	@Override
	public void receiveEnergy(ForgeDirection from, double val) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		PipeTransportPower ptransport = (PipeTransportPower) transport;

		if (metadata != from.ordinal() && val > 0.0) {
			if (BuildCraftTransport.usePipeLoss) {
				ptransport.internalNextPower[from.ordinal()] += val * (1 - ptransport.powerResistance);
			} else {
				ptransport.internalNextPower[from.ordinal()] += val;
			}

			if (ptransport.internalNextPower[from.ordinal()] >= 10000) {
				worldObj.createExplosion(null, xCoord, yCoord, zCoord, 3, false);
				worldObj.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
			}
		}
	}

	@Override
	public void requestEnergy(ForgeDirection from, int i) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		PipeTransportPower ptransport = (PipeTransportPower) transport;

		if (metadata == from.ordinal()) {
			ptransport.step();
			ptransport.nextPowerQuery[from.ordinal()] += i;
		}
	}
}
