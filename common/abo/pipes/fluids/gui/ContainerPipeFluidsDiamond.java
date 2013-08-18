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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import abo.pipes.fluids.PipeFluidsDistribution;
import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.transport.TileGenericPipe;

public class ContainerPipeFluidsDiamond extends BuildCraftContainer {

	public final PipeFluidsDistribution pipe;
	private final Fluid[] fluids = new Fluid[6 * 9];

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
			for (int i = 0; i < fluids.length; ++i) {
				if (fluids[i] != pipe.fluids[i]) {
					((ICrafting) crafter).sendProgressBarUpdate(this, i, pipe.fluids[i] != null ? pipe.fluids[i].getID() : 0);
					fluids[i] = pipe.fluids[i];
				}
			}
		}

	}

	@Override
	public void updateProgressBar(int id, int data) {
		pipe.update(id, FluidRegistry.getFluid(data));
		fluids[id] = pipe.fluids[id];
	}

	@Override
	public Slot getSlot(int id) {
		return null;
	}
}
