package abo.pipes.liquids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.LiquidStack;
import abo.network.ILiquidSlotChange;
import buildcraft.transport.pipes.PipeLogic;

public class PipeLogicLiquidsDiamond extends PipeLogic implements ILiquidSlotChange {

	public final LiquidStack[] liquidStacks = new LiquidStack[6 * 9];

	/* SAVING & LOADING */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		for (int i = 0; i < liquidStacks.length; ++i) {
			NBTTagCompound nbt = nbttagcompound.getCompoundTag("liquidStack[" + i + "]");
			if (nbt != null)
				liquidStacks[i] = LiquidStack.loadLiquidStackFromNBT(nbt);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		for (int i = 0; i < liquidStacks.length; ++i) {
			NBTTagCompound nbt = new NBTTagCompound();
			if (liquidStacks[i] != null)
				liquidStacks[i].writeToNBT(nbt);
			nbttagcompound.setTag("liquidStack[" + i + "]", nbt);
		}
	}

	@Override
	public void update(int slot, LiquidStack stack) {
		// System.out.println("update: " + worldObj.isRemote + " - " + slot + " to " + stack);

		if (stack != liquidStacks[slot]) {
			liquidStacks[slot] = stack;
		}
	}
}
