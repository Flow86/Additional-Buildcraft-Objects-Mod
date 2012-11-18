/** 
 * Copyright (C) 2011 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.pipes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import abo.ABO;
import abo.actions.ActionSwitchOnPipe;
import buildcraft.api.core.Orientations;
import buildcraft.api.core.Position;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.IAction;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicGold;

/**
 * @author Flow86
 * 
 */
public class PipePowerSwitch extends ABOPipe {
	private final int unpoweredTexture = 2 * 16 + 0;
	private final int poweredTexture = 2 * 16 + 1;
	private boolean powered;
	private boolean switched;

	public PipePowerSwitch(int itemID) {
		super(new PipeTransportPower(), new PipeLogicGold(), itemID);

		((PipeTransportPower) transport).powerResitance = 0.001;
	}

	@Override
	public int getTextureIndex(Orientations direction) {

		return isPowered() ? poweredTexture : unpoweredTexture;
	}

	@Override
	public int getTextureIndexForItem() {
		return poweredTexture;
	}

	@Override
	public boolean isPipeConnected(TileEntity tile) {
		Pipe pipe2 = null;

		if (tile instanceof TileGenericPipe)
			pipe2 = ((TileGenericPipe) tile).pipe;

		if (!isPowered())
			return false;

		return (pipe2 == null || !(pipe2 instanceof PipePowerSwitch)) && super.isPipeConnected(tile);
	}

	/**
	 * @return
	 */
	public boolean isPowered() {
		return powered || switched; // worldObj.isBlockIndirectlyGettingPowered(xCoord,
									// yCoord, zCoord);
	}

	private void computeConnections() {
		try {
			Method computeConnections = TileGenericPipe.class.getDeclaredMethod("computeConnections");
			computeConnections.setAccessible(true);

			computeConnections.invoke(this.container);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void updateRedstoneCurrent() {
		boolean lastPowered = powered;

		LinkedList<TileGenericPipe> neighbours = new LinkedList<TileGenericPipe>();
		neighbours.add(this.container);

		powered = false;
		for (Orientations o : Orientations.dirs()) {
			Position pos = new Position(xCoord, yCoord, zCoord, o);
			pos.moveForwards(1.0);

			TileEntity tile = container.getTile(o);

			if (tile instanceof TileGenericPipe) {
				TileGenericPipe pipe = (TileGenericPipe) tile;
				if (BlockGenericPipe.isValid(pipe.pipe)) {
					neighbours.add(pipe);
					if (pipe.pipe.broadcastRedstone)
						powered = true;
				}
			}
		}

		if (!powered)
			powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

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
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		powered = nbttagcompound.getBoolean("powered");
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
		return actions;
	}

	@Override
	protected void actionsActivated(HashMap<Integer, Boolean> actions) {
		boolean lastSwitched = switched;

		super.actionsActivated(actions);

		switched = false;
		// Activate the actions
		for (Integer i : actions.keySet()) {
			if (actions.get(i)) {
				if (ActionManager.actions[i] instanceof ActionSwitchOnPipe) {
					switched = true;
				}
			}
		}
		if (lastSwitched != switched) {
			computeConnections();
		}
	}

}
