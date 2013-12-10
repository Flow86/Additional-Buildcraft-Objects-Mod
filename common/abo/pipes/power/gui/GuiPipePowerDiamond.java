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
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import abo.network.PacketYesNoChange;
import abo.pipes.power.PipePowerDistribution;
import abo.proxy.ABOProxy;
import buildcraft.core.gui.GuiAdvancedInterface;
import buildcraft.core.utils.StringUtils;
import buildcraft.transport.TileGenericPipe;

public class GuiPipePowerDiamond extends GuiAdvancedInterface {
	private static final ResourceLocation TEXTURE = new ResourceLocation("additional-buildcraft-objects", "textures/gui/pipePowerDiamond.png");

	class YesNoSlot extends AdvancedSlot {

		private final int nr;
		private final PipePowerDistribution pipe;

		public YesNoSlot(int nr, int x, int y, TileGenericPipe tile) {
			super(x, y);
			pipe = (PipePowerDistribution) tile.pipe;
			this.nr = nr;
		}

		public boolean isYes() {
			return pipe.connectionMatrix[nr];
		}

		public void toggle() {
			pipe.update(nr, !pipe.connectionMatrix[nr]);
		}
	}

	private final ContainerPipePowerDiamond guiContainer;

	public GuiPipePowerDiamond(InventoryPlayer player, TileGenericPipe tile) {
		super(new ContainerPipePowerDiamond(player, tile), null, TEXTURE);

		guiContainer = (ContainerPipePowerDiamond) inventorySlots;

		slots = new AdvancedSlot[6];
		for (int i = 0; i < 6; ++i) {
			slots[i] = new YesNoSlot(i, 8, 18 + i * (16 + 2), tile);
		}

		xSize = 175;
		ySize = 132;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String name = StringUtils.localize("item." + PipePowerDistribution.class.getSimpleName());

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
		mc.renderEngine.bindTexture(TEXTURE);

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

			guiContainer.detectAndSendChanges();

			if (s.pipe.container.worldObj.isRemote) {
				PacketYesNoChange packet = new PacketYesNoChange(s.pipe.container.xCoord, s.pipe.container.yCoord, s.pipe.container.zCoord, s.nr, s.isYes());
				ABOProxy.proxy.sendToServer(packet.getPacket());
			}
		}

	}
}
