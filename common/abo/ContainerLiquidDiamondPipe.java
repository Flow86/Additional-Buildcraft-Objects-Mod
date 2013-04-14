package abo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import buildcraft.core.gui.BuildCraftContainer;

public class ContainerLiquidDiamondPipe extends BuildCraftContainer {

	IInventory playerIInventory;
	IInventory filterIInventory;

	public ContainerLiquidDiamondPipe(IInventory playerInventory, IInventory filterInventory) {
		super(filterInventory.getSizeInventory());
		this.playerIInventory = playerInventory;
		this.filterIInventory = filterInventory;

		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 9; k1++) {
				addSlotToContainer(new Slot(playerInventory, k1 + l * 9 + 9, 8 + k1 * 18, 140 + l * 18));
			}
		}

		for (int i1 = 0; i1 < 9; i1++) {
			addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 198));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return filterIInventory.isUseableByPlayer(entityplayer);
	}
}
