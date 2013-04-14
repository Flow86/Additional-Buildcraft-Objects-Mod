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

package abo.pipes.power.gui;

import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import abo.pipes.power.PipeLogicDiamondConductive;
import abo.pipes.power.PipeDiamondConductive;
import abo.proxy.ABOProxy;
import buildcraft.core.gui.GuiAdvancedInterface;
import buildcraft.core.utils.StringUtil;
import buildcraft.transport.TileGenericPipe;

public class GuiPipeDiamondConductive extends GuiAdvancedInterface {

	class YesNoSlot extends AdvancedSlot {

		private final int nr;
		private final PipeLogicDiamondConductive logic;

		public YesNoSlot(int nr, int x, int y, TileGenericPipe tile) {
			super(x, y);
			logic = (PipeLogicDiamondConductive) tile.pipe.logic;
			this.nr = nr;
		}

		public boolean isYes() {
			return logic.connectionMatrix[nr];
		}

		public void toggle() {
			logic.update(nr, !logic.connectionMatrix[nr]);
		}
	}

	private final ContainerPipeDiamondConductive container;

	public GuiPipeDiamondConductive(InventoryPlayer player, TileGenericPipe tile) {
		super(new ContainerPipeDiamondConductive(player, tile), null);

		container = (ContainerPipeDiamondConductive) inventorySlots;

		slots = new AdvancedSlot[6];
		for (int i = 0; i < 6; ++i) {
			slots[i] = new YesNoSlot(i, 8, 18 + i * (16 + 2), tile);
		}

		xSize = 175;
		ySize = 132;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String name = StringUtil.localize("item." + PipeDiamondConductive.class.getSimpleName());

		fontRenderer.drawString(name, getCenteredOffset(name), 6, 0x404040);

		String names[] = { "Down", "Up", "North", "South", "West", "East" };

		for (int i = 0; i < 6; ++i) {
			fontRenderer.drawString(names[i], slots[i].x + 20, slots[i].y + 4, 0x404040);
		}

		drawForegroundSelection(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/gfx/abo/gui/pipeDiamondConductive.png");

		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;
		drawTexturedModalRect(cornerX, cornerY, 0, 0, xSize, ySize);

		for (AdvancedSlot slot : slots) {
			if (slot instanceof YesNoSlot) {
				YesNoSlot s = (YesNoSlot) slot;
				drawTexturedModalRect(cornerX + slot.x, cornerY + slot.y, 240, s.isYes() ? 0 : 16, 16, 16);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int k) {
		super.mouseClicked(mouseX, mouseY, k);

		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;

		int position = getSlotAtLocation(mouseX - cornerX, mouseY - cornerY);

		AdvancedSlot slot = null;

		if (position < 0)
			return;

		slot = slots[position];

		if (slot instanceof YesNoSlot) {
			YesNoSlot s = (YesNoSlot) slot;

			s.toggle();

			container.detectAndSendChanges();

			if (container.pipe.worldObj.isRemote) {
				PacketYesNoChange packet = new PacketYesNoChange(container.pipe.xCoord, container.pipe.yCoord, container.pipe.zCoord, s.nr, s.isYes());
				ABOProxy.proxy.sendToServer(packet.getPacket());
			}
		}

	}
}
