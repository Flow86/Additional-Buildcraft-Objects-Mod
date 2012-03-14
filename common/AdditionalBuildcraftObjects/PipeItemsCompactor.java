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

package net.minecraft.src.AdditionalBuildcraftObjects;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.EntityPassiveItem;
import net.minecraft.src.buildcraft.api.IPowerReceptor;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.api.PowerProvider;
import net.minecraft.src.buildcraft.core.RedstonePowerFramework;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;

/**
 * @author Flow86
 * 
 */
public class PipeItemsCompactor extends Pipe implements IPipeTransportItemsHook, IPowerReceptor, /*IABODestroy,*/ IABOSolid {

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
			itemStack = iS;
			underruns = 0;
		}
	}

	private PowerProvider powerProvider;
	private boolean isPowered = false;
	TreeMap<Orientations, TreeMap<Integer, ItemStacker>> items = new TreeMap<Orientations, TreeMap<Integer, ItemStacker>>();

	/**
	 * @param itemID
	 */
	public PipeItemsCompactor(int itemID) {
		super(new PipeTransportItems(), new PipeLogicStone(), itemID);

		powerProvider = (new RedstonePowerFramework()).createPowerProvider();
		powerProvider.configure(25, 1, 1, 0, 64);
	}

	/**
	 * @param orientation
	 * @param itemID
	 * @param stackSize
	 */
	public void addItemToItemStack(Orientations orientation, Integer itemID, Integer stackSize) {
		if (!items.containsKey(orientation))
			items.put(orientation, new TreeMap<Integer, ItemStacker>());

		// System.out.println("ItemID: " + itemID + " - Size: " + stackSize);

		if (!items.get(orientation).containsKey(itemID))
			items.get(orientation).put(itemID, new ItemStacker(new ItemStack(itemID, stackSize, 0)));
		else
			items.get(orientation).get(itemID).itemStack.stackSize += stackSize;

		// System.out.println("New Size: " +
		// items.get(orientation).get(itemID).itemStack.stackSize);
	}

	@Override
	public void destroy() {
		isPowered = false;

		for (Entry<Orientations, TreeMap<Integer, ItemStacker>> item : items.entrySet()) {
			for (Entry<Integer, ItemStacker> itemStack : item.getValue().entrySet()) {
				Utils.dropItems(worldObj, itemStack.getValue().itemStack, xCoord, yCoord, zCoord);
			}
		}
		items.clear();
	}

	@Override
	public void doWork() {
		// System.out.println("doWork()");

		if (powerProvider.useEnergy(1, 1, true) < 1) {
			// System.out.println("isPowered = false");
			isPowered = false;
			return;
		}

		// System.out.println("isPowered = true");
		isPowered = true;
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);

		// System.out.println("Output:");

		for (Entry<Orientations, TreeMap<Integer, ItemStacker>> item : items.entrySet()) {
			LinkedList<ItemStacker> toRemove = new LinkedList<ItemStacker>();

			for (Entry<Integer, ItemStacker> itemStack : item.getValue().entrySet()) {
				boolean processed = false;
				while (itemStack.getValue().itemStack.stackSize >= 16 || itemStack.getValue().underruns >= 5) {
					processed = true;
					itemStack.getValue().underruns = 0;

					// System.out.println(">>> itemStack(" +
					// itemStack.getValue().underruns + "):" + item.getKey() +
					// ", "
					// + itemStack.getValue().itemStack.itemID + " has "
					// + itemStack.getValue().itemStack.stackSize);

					int stackSize = itemStack.getValue().itemStack.stackSize;
					if (itemStack.getValue().itemStack.stackSize >= 16)
						stackSize = Math.max(64, itemStack.getValue().itemStack.stackSize);

					ItemStack output = itemStack.getValue().itemStack.splitStack(stackSize);

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
					// System.out.println("=== itemStack(" +
					// itemStack.getValue().underruns + "):" + item.getKey() +
					// ", "
					// + itemStack.getValue().itemStack.itemID + " has "
					// + itemStack.getValue().itemStack.stackSize);
				}
			}

			item.getValue().values().removeAll(toRemove);
		}
		// System.out.println("Done");
	}

	/**
	 * @param item
	 * @param orientation
	 */
	@Override
	public void entityEntered(EntityPassiveItem item, Orientations orientation) {
		if (isPowered && item.item.stackSize < 16) {
			addItemToItemStack(orientation, item.item.itemID, item.item.stackSize);
			((PipeTransportItems) transport).scheduleRemoval(item);
		}
	}

	@Override
	public LinkedList<Orientations> filterPossibleMovements(LinkedList<Orientations> possibleOrientations,
			Position pos, EntityPassiveItem item) {
		return possibleOrientations;
	}

	@Override
	public int getBlockTexture() {

		return 7 * 16 + (isPowered ? 1 : 0);
	}

	@Override
	public PowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public int powerRequest() {
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		// System.out.println("readFromNBT");

		NBTTagList nbtItems = nbttagcompound.getTagList("items");

		for (int j = 0; j < nbtItems.tagCount(); ++j) {
			try {
				NBTTagCompound nbtTreeMap = (NBTTagCompound) nbtItems.tagAt(j);

				Orientations orientation = Orientations.values()[nbtTreeMap.getInteger("orientation")];

				NBTTagList nbtItemStacks = nbtTreeMap.getTagList("itemStacks");
				for (int k = 0; k < nbtItemStacks.tagCount(); ++k) {
					try {
						NBTTagCompound nbtItemStack = (NBTTagCompound) nbtItemStacks.tagAt(k);
						Integer itemID = nbtItemStack.getInteger("itemID");
						Integer stackSize = nbtItemStack.getInteger("stackSize");

						// System.out.println("read: item " + itemID +
						// " has size " + stackSize);

						addItemToItemStack(orientation, itemID, stackSize);
					} catch (Throwable t) {
						// It may be the case that entities cannot be reloaded
						// between
						// two versions - ignore these errors.
					}
				}

			} catch (Throwable t) {
				// It may be the case that entities cannot be reloaded between
				// two versions - ignore these errors.
			}
		}
	}

	/*
	 * its the same as in PipeItemsStone, was not able to use it directly
	 */
	@Override
	public void readjustSpeed(EntityPassiveItem item) {
		if (item.speed > Utils.pipeNormalSpeed) {
			item.speed = item.speed - Utils.pipeNormalSpeed / 2.0F;
		}

		if (item.speed < Utils.pipeNormalSpeed) {
			item.speed = Utils.pipeNormalSpeed;
		}
	}

	@Override
	public void setPowerProvider(PowerProvider provider) {
		powerProvider = provider;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
			isPowered = false;
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		// System.out.println("writeToNBT");

		NBTTagList nbtItems = new NBTTagList();

		for (Entry<Orientations, TreeMap<Integer, ItemStacker>> item : items.entrySet()) {
			NBTTagCompound nbtTreeMap = new NBTTagCompound();
			NBTTagList nbtItemStacks = new NBTTagList();

			for (Entry<Integer, ItemStacker> itemStack : item.getValue().entrySet()) {
				// System.out.println("write: item " + itemStack.getKey() +
				// " has size " + itemStack.getValue().itemStack.stackSize);

				NBTTagCompound nbtItemStack = new NBTTagCompound();

				nbtItemStack.setInteger("itemID", itemStack.getKey());
				nbtItemStack.setInteger("stackSize", itemStack.getValue().itemStack.stackSize);
				// nbtItemStack.setInteger("underruns",
				// itemStack.getValue().underruns);

				nbtItemStacks.appendTag(nbtItemStack);
			}

			nbtTreeMap.setInteger("orientation", item.getKey().ordinal());
			nbtTreeMap.setTag("itemStacks", nbtItemStacks);

			nbtItems.appendTag(nbtTreeMap);
		}

		nbttagcompound.setTag("items", nbtItems);
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		return true;
	}
}
