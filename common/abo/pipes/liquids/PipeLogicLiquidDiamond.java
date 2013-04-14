package abo.pipes.liquids;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.ABOGuiIds;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.SimpleInventory;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.pipes.PipeLogic;

public class PipeLogicLiquidDiamond extends PipeLogic implements ISpecialInventory {

	private final SimpleInventory filters = new SimpleInventory(54, "items", 1);

	/* PIPE LOGIC */
	@Override
	public boolean doDrop() {
		return false;
	}

	@Override
	public boolean blockActivated(EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID < Block.blocksList.length)
			if (Block.blocksList[entityplayer.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe)
				return false;

		if (!CoreProxy.proxy.isRenderWorld(container.worldObj)) {
			entityplayer.openGui(ABO.instance, ABOGuiIds.PIPE_DIAMOND_LIQUIDS, container.worldObj, container.xCoord, container.yCoord, container.zCoord);
		}

		return true;
	}

	/* SAVING & LOADING */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		filters.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		filters.writeToNBT(nbttagcompound);
	}

	/* ISPECIALINVENTORY */
	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		return 0;
	}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount) {
		return new ItemStack[0];
	}

	/* IINVENTORY IMPLEMENTATION */
	@Override
	public int getSizeInventory() {
		return filters.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return filters.getStackInSlot(i);
	}

	@Override
	public String getInvName() {
		return "Filters";
	}

	@Override
	public int getInventoryStackLimit() {
		return filters.getInventoryStackLimit();
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return filters.getStackInSlotOnClosing(i);
	}

	@Override
	public void onInventoryChanged() {
		filters.onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == container;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack = filters.decrStackSize(i, j);

		if (CoreProxy.proxy.isSimulating(worldObj)) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {

		filters.setInventorySlotContents(i, itemstack);
		if (CoreProxy.proxy.isSimulating(worldObj)) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

}