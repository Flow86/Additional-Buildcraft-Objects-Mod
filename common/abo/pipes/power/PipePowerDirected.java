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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicIron;
import buildcraft.transport.pipes.PipePowerWood;
import buildcraft.transport.pipes.PipeStructureCobblestone;

public class PipePowerDirected extends ABOPipe<PipeTransportPower> implements IPipeTransportPowerHook {

	private final int baseTexture = PipeIconProvider.PipePowerIron;
	private final int sideTexture = PipeIconProvider.PipePowerIronSide;

	private final PipeLogicIron logic = new PipeLogicIron(this) {
		@Override
		protected boolean isValidConnectingTile(TileEntity tile) {
			if (tile instanceof TileGenericPipe) {
				Pipe otherPipe = ((TileGenericPipe) tile).pipe;
				if (otherPipe instanceof PipePowerWood || otherPipe instanceof PipeStructureCobblestone)
					return false;
				if (otherPipe.transport instanceof PipeTransportPower)
					return true;
				return false;
			}
			if (tile instanceof IPowerReceptor)
				return true;
			return false;
		}
	};

	@Override
	public boolean blockActivated(EntityPlayer entityplayer) {
		return logic.blockActivated(entityplayer);
	}

	@Override
	public void onNeighborBlockChange(int blockId) {
		logic.switchOnRedstone();
		super.onNeighborBlockChange(blockId);
	}

	@Override
	public void onBlockPlaced() {
		logic.onBlockPlaced();
		super.onBlockPlaced();
	}

	@Override
	public void initialize() {
		logic.initialize();
		super.initialize();
	}

	public PipePowerDirected(int itemID) {
		super(new PipeTransportPower(), itemID);

		transport.powerCapacities.put(PipePowerDirected.class, 1024);
		transport.initFromPipe(getClass());
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
	public float requestEnergy(ForgeDirection from, float amount) {
		int metadata = container.getBlockMetadata();

		if (metadata == from.ordinal()) {
			return amount;
		}
		return 0;
	}
}
