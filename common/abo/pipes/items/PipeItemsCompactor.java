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

package abo.pipes.items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.PipeIconProvider;
import abo.actions.ActionSwitchOnPipe;
import abo.actions.ActionToggleOffPipe;
import abo.actions.ActionToggleOnPipe;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.api.transport.IPipedItem;
import buildcraft.core.utils.Utils;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
public class PipeItemsCompactor extends ABOPipe implements IPipeTransportItemsHook, IActionReceptor {
	private final int onTexture = PipeIconProvider.PipeItemsCompactorOn;
	private final int offTexture = PipeIconProvider.PipeItemsCompactorOff;
	private boolean powered = false;
	private boolean toggled = false;
	private boolean switched = false;
	private final TreeMap<ForgeDirection, PipeItemsCompactorInventory> receivedStacks = new TreeMap<ForgeDirection, PipeItemsCompactorInventory>();
	private final SafeTimeTracker timeTracker = new SafeTimeTracker();

	/**
	 * @param itemID
	 */
	public PipeItemsCompactor(int itemID) {
		super(new PipeTransportItems(), new PipeLogicStone(), itemID);
	}

	/**
	 * @param orientation
	 * @param itemID
	 * @param stackSize
	 */
	public void addItemToItemStack(ForgeDirection orientation, ItemStack stack) {
		System.out.println("in:  Stack " + stack.toString());

		if (!receivedStacks.containsKey(orientation))
			receivedStacks.put(orientation, new PipeItemsCompactorInventory(worldObj));

		receivedStacks.get(orientation).addItemStack(stack);
	}

	@Override
	public void dropContents() {
		powered = false;
		toggled = false;
		switched = false;

		for (Entry<ForgeDirection, PipeItemsCompactorInventory> receivedStack : receivedStacks.entrySet()) {
			receivedStack.getValue().dropContents(xCoord, yCoord, zCoord);
		}
		receivedStacks.clear();

		super.dropContents();
	}

	@Override
	public void entityEntered(IPipedItem item, ForgeDirection orientation) {
		if (isPowered() && item.getItemStack().isStackable()) {
			addItemToItemStack(orientation, item.getItemStack());
			((PipeTransportItems) transport).scheduleRemoval(item);
		} else
			readjustSpeed(item);
	}

	@Override
	public LinkedList<ForgeDirection> filterPossibleMovements(LinkedList<ForgeDirection> possibleOrientations, Position pos, IPipedItem item) {
		return possibleOrientations;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		powered = nbttagcompound.getBoolean("powered");
		switched = nbttagcompound.getBoolean("switched");
		toggled = nbttagcompound.getBoolean("toggled");

		// System.out.println("readFromNBT");

		NBTTagList nbtItems = nbttagcompound.getTagList("items");

		for (int j = 0; j < nbtItems.tagCount(); ++j) {
			try {
				NBTTagCompound nbtTreeMap = (NBTTagCompound) nbtItems.tagAt(j);

				ForgeDirection orientation = ForgeDirection.values()[nbtTreeMap.getInteger("orientation")];

				if (!receivedStacks.containsKey(orientation))
					receivedStacks.put(orientation, new PipeItemsCompactorInventory(worldObj));

				NBTTagCompound nbtItemStacks = (NBTTagCompound) nbtTreeMap.getTag("itemStacks");

				receivedStacks.get(orientation).readFromNBT(nbtItemStacks);
			} catch (Throwable t) {
				// It may be the case that entities cannot be reloaded between
				// two versions - ignore these errors.
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setBoolean("powered", powered);
		nbttagcompound.setBoolean("switched", switched);
		nbttagcompound.setBoolean("toggled", toggled);

		// System.out.println("writeToNBT");

		NBTTagList nbtItems = new NBTTagList();

		for (Entry<ForgeDirection, PipeItemsCompactorInventory> receivedStack : receivedStacks.entrySet()) {
			NBTTagCompound nbtTreeMap = new NBTTagCompound();
			NBTTagCompound nbtItemStacks = new NBTTagCompound();

			nbtTreeMap.setInteger("orientation", receivedStack.getKey().ordinal());
			receivedStack.getValue().writeToNBT(nbtItemStacks);

			nbtTreeMap.setTag("itemStacks", nbtItemStacks);

			nbtItems.appendTag(nbtTreeMap);
		}

		nbttagcompound.setTag("items", nbtItems);
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
	public LinkedList<IAction> getActions() {
		LinkedList<IAction> actions = super.getActions();
		actions.add(ABO.actionSwitchOnPipe);
		actions.add(ABO.actionToggleOnPipe);
		actions.add(ABO.actionToggleOffPipe);
		return actions;
	}

	@Override
	protected void actionsActivated(HashMap<Integer, Boolean> actions) {
		boolean lastSwitched = switched;
		boolean lastToggled = toggled;

		super.actionsActivated(actions);

		switched = false;
		// Activate the actions
		for (Integer i : actions.keySet()) {
			if (actions.get(i)) {
				if (ActionManager.actions[i] instanceof ActionSwitchOnPipe) {
					switched = true;
				} else if (ActionManager.actions[i] instanceof ActionToggleOnPipe) {
					toggled = true;
				} else if (ActionManager.actions[i] instanceof ActionToggleOffPipe) {
					toggled = false;
				}
			}
		}
		if ((lastSwitched != switched) || (lastToggled != toggled)) {
			if (lastSwitched != switched && !switched)
				toggled = false;
		}
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
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		updateRedstoneCurrent();

		if (isPowered() && timeTracker.markTimeIfDelay(worldObj, 25)) {
			for (Entry<ForgeDirection, PipeItemsCompactorInventory> receivedStack : receivedStacks.entrySet()) {
				ItemStack stack = receivedStack.getValue().findItemStackToRemove(16, 100);
				if (stack != null) {
					System.out.println("out: Stack " + stack.toString());

					if (!Utils.addToRandomPipeEntry(this.container, receivedStack.getKey(), stack)) {
						Position destPos = new Position(xCoord, yCoord, zCoord, receivedStack.getKey());

						destPos.moveForwards(0.3);

						Utils.dropItems(worldObj, stack, (int) destPos.x, (int) destPos.y, (int) destPos.z);
					}

				}
			}
		}
	}

	@Override
	public void readjustSpeed(IPipedItem item) {
		item.setSpeed(Math.min(Math.max(Utils.pipeNormalSpeed, item.getSpeed()) * 2f, Utils.pipeNormalSpeed * 30F));
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (worldObj != null)
			return (isPowered() ? onTexture : offTexture);
		return offTexture;
	}
}
