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

import java.util.LinkedList;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.PipeIconProvider;
import abo.actions.ABOEnergyPulser;
import abo.actions.ActionSwitchOnPipe;
import abo.actions.ActionToggleOffPipe;
import abo.actions.ActionToggleOnPipe;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.core.Position;
import buildcraft.api.gates.IAction;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeFluidsWood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipeFluidsValve extends PipeFluidsWood {

	private final ABOEnergyPulser pulser;
	private boolean powered;
	private boolean switched;
	private boolean toggled;

	private final int closedTexture = PipeIconProvider.PipeLiquidsValveClosed;
	private final int closedTextureSide = PipeIconProvider.PipeLiquidsValveClosedSide;
	private final int openTexture = PipeIconProvider.PipeLiquidsValveOpen;
	private final int openTextureSide = PipeIconProvider.PipeLiquidsValveOpenSide;

	public PipeFluidsValve(int itemID) {
		super(itemID);

		transport.flowRate = 160;
		transport.travelDelay = 2;

		pulser = new ABOEnergyPulser(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider() {
		return ABO.instance.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return isPowered() ? openTexture : closedTexture;
		else {
			int metadata = container.getBlockMetadata();

			if (metadata == direction.ordinal())
				return isPowered() ? openTextureSide : closedTextureSide;
			else
				return isPowered() ? openTexture : closedTexture;
		}
	}

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

		NBTTagCompound nbttagcompoundC = new NBTTagCompound();
		pulser.writeToNBT(nbttagcompoundC);
		nbttagcompound.setTag("Pulser", nbttagcompoundC);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		powered = nbttagcompound.getBoolean("powered");
		switched = nbttagcompound.getBoolean("switched");
		toggled = nbttagcompound.getBoolean("toggled");

		NBTTagCompound nbttagcompoundP = nbttagcompound.getCompoundTag("Pulser");
		pulser.readFromNBT(nbttagcompoundP);
	}

	@Override
	public void updateEntity() {
		updateRedstoneCurrent();

		if (isPowered())
			pulser.enablePulse();
		else {
			pulser.disablePulse();
			liquidToExtract = 0;
		}

		pulser.update();

		super.updateEntity();
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
	protected void actionsActivated(Map<IAction, Boolean> actions) {
		boolean lastSwitched = switched;
		boolean lastToggled = toggled;

		super.actionsActivated(actions);

		switched = false;
		// Activate the actions
		for (IAction i : actions.keySet()) {
			if (actions.get(i)) {
				if (i instanceof ActionSwitchOnPipe) {
					switched = true;
				} else if (i instanceof ActionToggleOnPipe) {
					toggled = true;
				} else if (i instanceof ActionToggleOffPipe) {
					toggled = false;
				}
			}
		}
		if ((lastSwitched != switched) || (lastToggled != toggled)) {
			if (lastSwitched != switched && !switched)
				toggled = false;

			container.scheduleRenderUpdate();
			updateNeighbors(true);
		}
	}
}
