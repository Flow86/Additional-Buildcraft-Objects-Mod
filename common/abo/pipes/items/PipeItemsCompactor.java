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
	/**
	 * @author Flow86
	 * 
	 */
	private class ItemStacker {
		public ItemStack itemStack;

		public int underruns;

		/**
		 * @param iS
		 */
		public ItemStacker(ItemStack iS) {
			super();

			itemStack = iS;
			underruns = 0;
		}
	}

	private class Pair<A, B> implements java.lang.Comparable {
		private A first;
		private B second;

		public Pair(A first, B second) {
			super();

			this.first = first;
			this.second = second;
		}

		@Override
		public int hashCode() {
			int hashFirst = first != null ? first.hashCode() : 0;
			int hashSecond = second != null ? second.hashCode() : 0;

			return (hashFirst + hashSecond) * hashSecond + hashFirst;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Pair) {
				Pair otherPair = (Pair) other;
				return ((this.first == otherPair.first || (this.first != null && otherPair.first != null && this.first.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
						&& otherPair.second != null && this.second.equals(otherPair.second))));
			}

			return false;
		}

		@Override
		public String toString() {
			return "(" + first + ", " + second + ")";
		}

		public A getFirst() {
			return first;
		}

		public void setFirst(A first) {
			this.first = first;
		}

		public B getSecond() {
			return second;
		}

		public void setSecond(B second) {
			this.second = second;
		}

		@Override
		public int compareTo(Object other) {
			final int BEFORE = -1;
			final int EQUAL = 0;
			final int AFTER = 1;

			// this optimization is usually worthwhile, and can
			// always be added
			if (this == other)
				return EQUAL;

			if (!(other instanceof Pair))
				return BEFORE;

			Pair otherPair = (Pair) other;

			// objects, including type-safe enums, follow this form
			// note that null objects will throw an exception here
			int comparison = ((Comparable) this.first).compareTo(otherPair.first);
			if (comparison != EQUAL)
				return comparison;

			comparison = ((Comparable) this.second).compareTo(otherPair.second);
			if (comparison != EQUAL)
				return comparison;

			// all comparisons have yielded equality
			// verify that compareTo is consistent with equals (optional)
			assert this.equals(other) : "compareTo inconsistent with equals.";

			return EQUAL;
		}
	}

	private final int onTexture = PipeIconProvider.PipeItemsCompactorOn;
	private final int offTexture = PipeIconProvider.PipeItemsCompactorOff;
	private boolean powered = false;
	private boolean toggled = false;
	private boolean switched = false;
	TreeMap<ForgeDirection, TreeMap<Pair<Integer, Integer>, ItemStacker>> items = new TreeMap<ForgeDirection, TreeMap<Pair<Integer, Integer>, ItemStacker>>();
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
	public void addItemToItemStack(ForgeDirection orientation, Integer itemID, Integer dmgID, Integer stackSize) {
		if (!items.containsKey(orientation))
			items.put(orientation, new TreeMap<Pair<Integer, Integer>, ItemStacker>());

		System.out.println("ItemID: " + itemID + "/" + dmgID + " - Size: " + stackSize);

		Pair key = new Pair(itemID, dmgID);
		if (!items.get(orientation).containsKey(key))
			items.get(orientation).put(key, new ItemStacker(new ItemStack(itemID, stackSize, dmgID)));
		else
			items.get(orientation).get(key).itemStack.stackSize += stackSize;

		System.out.println("New Size: " + items.get(orientation).get(key).itemStack.stackSize);
	}

	@Override
	public void dropContents() {
		powered = false;
		toggled = false;
		switched = false;

		for (Entry<ForgeDirection, TreeMap<Pair<Integer, Integer>, ItemStacker>> item : items.entrySet()) {
			for (Entry<Pair<Integer, Integer>, ItemStacker> itemStack : item.getValue().entrySet()) {
				Utils.dropItems(worldObj, itemStack.getValue().itemStack, xCoord, yCoord, zCoord);
			}
		}
		items.clear();

		super.dropContents();
	}

	public void doWork() {
		System.out.println("doWork()");

		System.out.println("Output:");

		for (Entry<ForgeDirection, TreeMap<Pair<Integer, Integer>, ItemStacker>> item : items.entrySet()) {
			LinkedList<ItemStacker> toRemove = new LinkedList<ItemStacker>();

			for (Entry<Pair<Integer, Integer>, ItemStacker> itemStack : item.getValue().entrySet()) {
				boolean processed = false;
				while (itemStack.getValue().itemStack.stackSize >= 16 || itemStack.getValue().underruns >= 5) {
					processed = true;
					itemStack.getValue().underruns = 0;

					System.out.println(">>> itemStack(" + itemStack.getValue().underruns + "):" + item.getKey() + ", " + itemStack.getValue().itemStack.itemID
							+ " has " + itemStack.getValue().itemStack.stackSize);

					int stackSize = itemStack.getValue().itemStack.stackSize;
					if (itemStack.getValue().itemStack.stackSize >= 16)
						stackSize = Math.min(64, itemStack.getValue().itemStack.stackSize);

					System.out.println("Stacksize: " + stackSize);
					System.out.println("Before: " + itemStack.getValue().itemStack.stackSize);

					ItemStack output = itemStack.getValue().itemStack.splitStack(stackSize);

					System.out.println("After: " + itemStack.getValue().itemStack.stackSize);

					if (!Utils.addToRandomPipeEntry(this.container, item.getKey(), output)) {
						Position destPos = new Position(xCoord, yCoord, zCoord, item.getKey());

						destPos.moveForwards(0.3);

						Utils.dropItems(worldObj, output, (int) destPos.x, (int) destPos.y, (int) destPos.z);
					}

					if (itemStack.getValue().itemStack.stackSize <= 0)
						toRemove.add(itemStack.getValue());
				}

				if (!processed) {
					itemStack.getValue().underruns++;
					System.out.println("=== itemStack(" + itemStack.getValue().underruns + "):" + item.getKey() + ", " + itemStack.getValue().itemStack.itemID
							+ " has " + itemStack.getValue().itemStack.stackSize);
				}
			}

			item.getValue().values().removeAll(toRemove);
		}
		System.out.println("Done");
	}

	@Override
	public void entityEntered(IPipedItem item, ForgeDirection orientation) {
		if (isPowered() && item.getItemStack().stackSize < 16) {
			addItemToItemStack(orientation, item.getItemStack().itemID, item.getItemStack().getItemDamage(), item.getItemStack().stackSize);
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

				NBTTagList nbtItemStacks = nbtTreeMap.getTagList("itemStacks");
				for (int k = 0; k < nbtItemStacks.tagCount(); ++k) {
					try {
						NBTTagCompound nbtItemStack = (NBTTagCompound) nbtItemStacks.tagAt(k);
						Integer itemID = nbtItemStack.getInteger("itemID");
						Integer dmgID = nbtItemStack.getInteger("dmgID");
						Integer stackSize = nbtItemStack.getInteger("stackSize");

						// System.out.println("read: item " + itemID + " has size " + stackSize);

						addItemToItemStack(orientation, itemID, dmgID, stackSize);
					} catch (Throwable t) {
						// It may be the case that entities cannot be reloaded
						// between two versions - ignore these errors.
					}
				}

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

		for (Entry<ForgeDirection, TreeMap<Pair<Integer, Integer>, ItemStacker>> item : items.entrySet()) {
			NBTTagCompound nbtTreeMap = new NBTTagCompound();
			NBTTagList nbtItemStacks = new NBTTagList();

			for (Entry<Pair<Integer, Integer>, ItemStacker> itemStack : item.getValue().entrySet()) {
				// System.out.println("write: item " + itemStack.getKey() + " has size " + itemStack.getValue().itemStack.stackSize);

				NBTTagCompound nbtItemStack = new NBTTagCompound();

				nbtItemStack.setInteger("itemID", itemStack.getKey().getFirst());
				nbtItemStack.setInteger("dmgID", itemStack.getKey().getSecond());
				nbtItemStack.setInteger("stackSize", itemStack.getValue().itemStack.stackSize);
				// nbtItemStack.setInteger("underruns", itemStack.getValue().underruns);

				nbtItemStacks.appendTag(nbtItemStack);
			}

			nbtTreeMap.setInteger("orientation", item.getKey().ordinal());
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
			doWork();
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
