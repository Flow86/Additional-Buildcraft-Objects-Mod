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

import java.util.LinkedList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.PipeIconProvider;
import abo.actions.ActionToggleOffPipe;
import abo.actions.ActionToggleOnPipe;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.IPipeTransportPowerHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;

/**
 * @author Flow86
 * 
 */
public class PipePowerSwitch extends ABOPipe<PipeTransportPower> implements IActionReceptor, IPipeTransportPowerHook {
	private final int unpoweredTexture = PipeIconProvider.PipePowerSwitchUnpowered;
	private final int poweredTexture = PipeIconProvider.PipePowerSwitchPowered;
	private boolean powered;
	private boolean switched;
	private boolean toggled;

	public PipePowerSwitch(int itemID) {
		super(new PipeTransportPower(), itemID);

		transport.maxPower = 256;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return isPowered() ? poweredTexture : unpoweredTexture;
	}

	@Override
	public boolean canPipeConnect(TileEntity tile, ForgeDirection side) {
		Pipe pipe2 = null;

		if (tile instanceof TileGenericPipe)
			pipe2 = ((TileGenericPipe) tile).pipe;

		if (!isPowered())
			return false;

		return (pipe2 == null || !(pipe2 instanceof PipePowerSwitch)) && super.canPipeConnect(tile, side);
	}

	/**
	 * @return
	 */
	public boolean isPowered() {
		return powered || switched || toggled;
	}

	public void updateRedstoneCurrent() {
		boolean lastPowered = powered;

		LinkedList<TileGenericPipe> neighbours = new LinkedList<TileGenericPipe>();
		neighbours.add(this.container);

		powered = false;
		for (ForgeDirection o : ForgeDirection.VALID_DIRECTIONS) {
			Position pos = new Position(container.xCoord, container.yCoord, container.zCoord, o);
			pos.moveForwards(1.0);

			TileEntity tile = container.getTile(o);

			if (tile instanceof TileGenericPipe) {
				TileGenericPipe pipe = (TileGenericPipe) tile;
				if (BlockGenericPipe.isValid(pipe.pipe)) {
					neighbours.add(pipe);
					if (pipe.pipe.hasGate() && pipe.pipe.gate.isEmittingRedstone())
						powered = true;
				}
			}
		}

		if (!powered)
			powered = container.worldObj.isBlockIndirectlyGettingPowered(container.xCoord, container.yCoord, container.zCoord);

		if (lastPowered != powered) {
			for (TileGenericPipe pipe : neighbours) {
				pipe.scheduleNeighborChange();
				pipe.updateEntity();
			}
		}
	}

	@Override
	public void onNeighborBlockChange(int blockId) {
		super.onNeighborBlockChange(blockId);
		updateRedstoneCurrent();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setBoolean("powered", powered);
		nbttagcompound.setBoolean("switched", switched);
		nbttagcompound.setBoolean("toggled", toggled);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		powered = nbttagcompound.getBoolean("powered");
		switched = nbttagcompound.getBoolean("switched");
		toggled = nbttagcompound.getBoolean("toggled");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.src.buildcraft.transport.Pipe#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		updateRedstoneCurrent();
	}

	@Override
	public LinkedList<IAction> getActions() {
		LinkedList<IAction> actions = super.getActions();
		actions.add(ABO.actionSwitchOnPipe);
		actions.add(ABO.actionToggleOnPipe);
		actions.add(ABO.actionToggleOffPipe);
		return actions;
	}

	@Override
	public void actionActivated(IAction action) {
		boolean lastSwitched = switched;
		boolean lastToggled = toggled;

		switched = false;

		// Activate the actions
		if (action instanceof ActionToggleOnPipe) {
			toggled = true;
		} else if (action instanceof ActionToggleOffPipe) {
			toggled = false;
		}

		if ((lastSwitched != switched) || (lastToggled != toggled)) {
			if (lastSwitched != switched && !switched)
				toggled = false;
			container.scheduleNeighborChange();
			updateNeighbors(true);
		}
	}

	@Override
	public float receiveEnergy(ForgeDirection from, float val) {

		// no power is received if "disconnected"
		if (!isPowered())
			return val;

		if (val > 0.0) {
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
		// no power is requested if "disconnected"
		if (!isPowered())
			return;

		transport.nextPowerQuery[from.ordinal()] += amount;
	}
}
