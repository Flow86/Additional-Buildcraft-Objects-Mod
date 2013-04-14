package abo;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import buildcraft.core.gui.GuiAdvancedInterface;
import buildcraft.core.network.PacketIds;
import buildcraft.core.network.PacketSlotChange;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.StringUtil;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeLogicDiamond;

public class GuiLiquidDiamondPipe extends GuiAdvancedInterface {

	IInventory playerInventory;
	PipeLogicDiamond filterInventory;

	public GuiLiquidDiamondPipe(IInventory playerInventory, TileGenericPipe tile) {
		super(new ContainerLiquidDiamondPipe(playerInventory, (IInventory) tile.pipe.logic), (IInventory) tile.pipe.logic);
		this.playerInventory = playerInventory;
		this.filterInventory = (PipeLogicDiamond) tile.pipe.logic;
		xSize = 175;
		ySize = 132;

		slots = new AdvancedSlot[6 * 9];

		for (int k = 0; k < 6; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				int id = k * 9 + j1;
				slots[id] = new IInventorySlot(8 + j1 * 18, 18 + k * 18, filterInventory, j1 + k * 9);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(filterInventory.getInvName(), getCenteredOffset(filterInventory.getInvName()), 6, 0x404040);
		fontRenderer.drawString(StringUtil.localize("gui.inventory"), 8, ySize - 97, 0x404040);

		drawForegroundSelection(par1, par2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/gfx/abo/gui/pipeDiamondLiquids.png");
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

		drawBackgroundSlots();
	}

	int inventoryRows = 6;

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);

		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;

		int position = getSlotAtLocation(i - cornerX, j - cornerY);

		IInventorySlot slot = null;

		if (position != -1) {
			slot = (IInventorySlot) slots[position];
		}

		if (slot != null) {
			ItemStack playerStack = mc.thePlayer.inventory.getItemStack();

			ItemStack newStack;
			if (playerStack != null) {
				newStack = playerStack.copy();
				newStack.stackSize = 1;
			} else {
				newStack = null;
			}

			filterInventory.setInventorySlotContents(position, newStack);

			if (CoreProxy.proxy.isRenderWorld(filterInventory.worldObj)) {
				PacketSlotChange packet = new PacketSlotChange(PacketIds.DIAMOND_PIPE_SELECT, filterInventory.xCoord, filterInventory.yCoord,
						filterInventory.zCoord, position, newStack);
				CoreProxy.proxy.sendToServer(packet.getPacket());
			}
		}
	}
}
