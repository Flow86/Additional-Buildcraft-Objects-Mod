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

package abo.pipes.fluids.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import abo.pipes.fluids.PipeFluidsDistribution;
import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.transport.TileGenericPipe;

public class ContainerPipeFluidsDiamond extends BuildCraftContainer {

	public final PipeFluidsDistribution pipe;
	private final FluidStack[] fluidStacks = new FluidStack[6 * 9];

	public ContainerPipeFluidsDiamond(InventoryPlayer inventory, TileGenericPipe tile) {
		super(0);

		pipe = (PipeFluidsDistribution) tile.pipe;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		// send update to all connected crafters
		for (Object crafter : crafters) {
			for (int i = 0; i < fluidStacks.length; ++i) {
				if (fluidStacks[i] != pipe.fluidStacks[i]) {
					// System.out.println("detectAndSendChanges: " + i + " to " + (logic.FluidStacks[i] != null ? logic.FluidStacks[i].asItemStack() : null));
					((ICrafting) crafter).sendProgressBarUpdate(this, i, pipe.fluidStacks[i] != null ? pipe.fluidStacks[i].fluidID : 0);
					// ((ICrafting) crafter).sendSlotContents(this, i, pipe.fluidStacks[i] != null ? pipe.fluidStacks[i].asItemStack() : null);
					fluidStacks[i] = pipe.fluidStacks[i];
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
		FluidStack fluidStack = null;

		if (value != null) {
			fluidStack = FluidContainerRegistry.getFluidForFilledItem(value);
		}

		// System.out.println("putStackInSlot: " + id + " to " + FluidStack);
		pipe.update(id, fluidStack);
		fluidStacks[id] = pipe.fluidStacks[id];
	}
}
