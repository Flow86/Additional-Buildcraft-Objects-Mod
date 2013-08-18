package abo.pipes.items;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.core.inventory.InvUtils;

public class PipeItemsCompactorInventory {
	private class InventorySlot {
		private final SafeTimeTracker timeTracker = new SafeTimeTracker();
		private final ItemStack itemStack;

		InventorySlot(World worldObj, ItemStack stack) {
			itemStack = stack;
			markModifed(worldObj);
		}

		public void markModifed(World worldObj) {
			if (worldObj != null)
				timeTracker.markTime(worldObj);
		}

		public boolean isNotModifiedSince(World worldObj, int unchangedSince) {
			if (worldObj != null)
				return timeTracker.markTimeIfDelay(worldObj, unchangedSince);

			return true;
		}

		public ItemStack getItemStack() {
			return itemStack;
		}

		public boolean isItemStackFull() {
			return !itemStack.isStackable() || itemStack.stackSize == itemStack.getMaxStackSize();
		}

		public ItemStack appendToStack(World worldObj, ItemStack stack) {
			if (itemStack.isItemEqual(stack)) {
				int stackSize = itemStack.stackSize + stack.stackSize;

				// does it fit in that stack?
				if (stackSize < itemStack.getMaxStackSize()) {
					itemStack.stackSize = stackSize;
					markModifed(worldObj);
					return null;
				} else {
					// no, fill up existing stack and reduce stacksize of other one
					itemStack.stackSize = stack.getMaxStackSize();
					stack.stackSize = stackSize - stack.getMaxStackSize();
					markModifed(worldObj);
				}
			}
			return stack;
		}
	};

	private final LinkedList<InventorySlot> inventoryContents = new LinkedList<InventorySlot>();

	/**
	 * creates the inventory
	 * 
	 * @param world
	 */
	public PipeItemsCompactorInventory() {
	}

	/**
	 * adds an itemstack to the list
	 * 
	 * @param stack
	 */
	public void addItemStack(World worldObj, ItemStack stack) {

		// if isnt stackable or full stack, add directly as new
		if (!stack.isStackable() || stack.stackSize == stack.getMaxStackSize()) {
			inventoryContents.add(new InventorySlot(worldObj, stack));
			return;
		}

		// find same item
		for (InventorySlot slot : inventoryContents) {
			stack = slot.appendToStack(worldObj, stack);
			if (stack == null)
				return;
		}

		inventoryContents.add(new InventorySlot(worldObj, stack));
	}

	/**
	 * drops the contents on a specific coordinate
	 * 
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 */
	public void dropContents(World worldObj, int xCoord, int yCoord, int zCoord) {
		for (InventorySlot inventorySlot : inventoryContents) {
			InvUtils.dropItems(worldObj, inventorySlot.getItemStack(), xCoord, yCoord, zCoord);
		}
		inventoryContents.clear();
	}

	/**
	 * finds a stack in the list which fulfills on of these criteria: - stackSize is larger than stackSize - has not been changed since unchangedSince ticks -
	 * stack is full or not stackable
	 * 
	 * @param stackSize
	 * @param unchangedSince
	 * @return
	 */
	public ItemStack findItemStackToRemove(World worldObj, int stackSize, int unchangedSince) {
		for (Iterator<InventorySlot> inventorySlots = inventoryContents.iterator(); inventorySlots.hasNext();) {
			InventorySlot inventorySlot = inventorySlots.next();

			if (inventorySlot.getItemStack().stackSize >= stackSize || inventorySlot.isItemStackFull() || inventorySlot.isNotModifiedSince(worldObj, unchangedSince)) {
				inventorySlots.remove();
				return inventorySlot.getItemStack();
			}
		}
		return null;
	}

	public void readFromNBT(World worldObj, NBTTagCompound nbtItemStacks) {
		int itemCount = nbtItemStacks.getInteger("itemCount");
		for (int itemStackNr = 0; itemStackNr < itemCount; ++itemStackNr) {
			try {
				NBTTagCompound nbtItemStack = (NBTTagCompound) nbtItemStacks.getTag("itemStack" + itemStackNr);
				inventoryContents.add(new InventorySlot(worldObj, ItemStack.loadItemStackFromNBT(nbtItemStack)));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbtItemStacks) {
		int itemStackNr = 0;
		for (InventorySlot inventorySlot : inventoryContents) {
			NBTTagCompound nbtItemStack = new NBTTagCompound();
			inventorySlot.getItemStack().writeToNBT(nbtItemStack);

			nbtItemStacks.setTag("itemStack" + itemStackNr++, nbtItemStack);
		}

		nbtItemStacks.setInteger("itemCount", itemStackNr);
	}
}
