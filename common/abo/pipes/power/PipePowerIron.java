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
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.PipeTransportPower;

public class PipePowerIron extends ABOPipe<PipeTransportPower> implements IPipeTransportPowerHook {

	private final int baseTexture = PipeIconProvider.PipePowerIron;
	private final int sideTexture = PipeIconProvider.PipePowerIronSide;

	public PipePowerIron(int itemID) {
		super(new PipeTransportPower(), itemID);

		transport.maxPower = 256;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return baseTexture;
		else {
			int metadata = container.getBlockMetadata();

			if (metadata == direction.ordinal())
				return baseTexture;
			else
				return sideTexture;
		}
	}

	@Override
	public float receiveEnergy(ForgeDirection from, float val) {
		int metadata = container.getBlockMetadata();

		if (metadata != from.ordinal() && val > 0.0) {
			transport.internalNextPower[from.ordinal()] += val;

			if (transport.internalNextPower[from.ordinal()] > transport.maxPower) {
				val = transport.internalNextPower[from.ordinal()] - transport.maxPower;
				transport.internalNextPower[from.ordinal()] = transport.maxPower;
			}
		}
		return val;
	}

	@Override
	public void requestEnergy(ForgeDirection from, float amount) {
		int metadata = container.getBlockMetadata();

		if (metadata == from.ordinal()) {
			transport.nextPowerQuery[from.ordinal()] += amount;
		}
	}
}
