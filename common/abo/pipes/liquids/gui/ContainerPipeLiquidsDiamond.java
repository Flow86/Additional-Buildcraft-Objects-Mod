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

package abo.pipes.liquids.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.LiquidStack;
import abo.pipes.liquids.PipeLiquidsDiamond;
import abo.pipes.liquids.PipeLogicLiquidsDiamond;
import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.transport.TileGenericPipe;

public class ContainerPipeLiquidsDiamond extends BuildCraftContainer {

	public final PipeLiquidsDiamond pipe;
	private final LiquidStack[] liquidStacks = new LiquidStack[6 * 9];

	public ContainerPipeLiquidsDiamond(InventoryPlayer inventory, TileGenericPipe tile) {
		super(0);

		pipe = (PipeLiquidsDiamond) tile.pipe;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		PipeLogicLiquidsDiamond logic = (PipeLogicLiquidsDiamond) pipe.logic;

		// send update to all connected crafters
		for (Object crafter : crafters) {
			for (int i = 0; i < liquidStacks.length; ++i) {
				if (liquidStacks[i] != logic.liquidStacks[i]) {
					// System.out.println("detectAndSendChanges: " + i + " to " + (logic.liquidStacks[i] != null ? logic.liquidStacks[i].asItemStack() : null));
					((ICrafting) crafter).sendSlotContents(this, i, logic.liquidStacks[i] != null ? logic.liquidStacks[i].asItemStack() : null);
					liquidStacks[i] = logic.liquidStacks[i];
				}
			}
		}

	}

	@Override
	public Slot getSlot(int id) {
		return null;
	}

	@Override
	public void putStackInSlot(int id, ItemStack value) {
		PipeLogicLiquidsDiamond logic = (PipeLogicLiquidsDiamond) pipe.logic;

		LiquidStack liquidStack = null;

		if (value != null) {
			liquidStack = new LiquidStack(value.itemID, 1, value.getItemDamage());
			if (value.stackTagCompound != null)
				liquidStack.extra = (NBTTagCompound) value.stackTagCompound.copy();
		}

		// System.out.println("putStackInSlot: " + id + " to " + liquidStack);
		logic.update(id, liquidStack);
		liquidStacks[id] = logic.liquidStacks[id];
	}
}
