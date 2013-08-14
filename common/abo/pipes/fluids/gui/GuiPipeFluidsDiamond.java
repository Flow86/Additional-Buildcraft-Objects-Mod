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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import abo.network.PacketFluidSlotChange;
import abo.pipes.fluids.PipeFluidsDistribution;
import abo.pipes.fluids.PipeLogicLiquidsDiamond;
import abo.proxy.ABOProxy;
import buildcraft.core.gui.GuiAdvancedInterface;
import buildcraft.core.utils.StringUtils;
import buildcraft.transport.TileGenericPipe;

public class GuiPipeFluidsDiamond extends GuiAdvancedInterface {

	private final ContainerPipeFluidsDiamond container;
	private final HashMap<String, LiquidStack> liquids = new HashMap<String, LiquidStack>();
	private final LinkedList<String> liquidsList = new LinkedList<String>();

	public class LiquidSlot extends AdvancedSlot {
		public final int nr;
		public String liquid = null;

		public LiquidSlot(int nr, int x, int y) {
			super(x, y);
			this.nr = nr;
		}

		@Override
		public ItemStack getItemStack() {
			if (liquid == null)
				return null;

			return liquids.get(liquid).canonical().asItemStack();
		}

		@Override
		public Icon getTexture() {
			if (liquid == null)
				return null;

			return liquids.get(liquid).canonical().getRenderingIcon();
		}

		@Override
		public void drawSprite(int cornerX, int cornerY) {
			if (liquid == null)
				return;

			Icon icon = getTexture();
			if (icon != null) {
				mc.renderEngine.bindTexture(liquids.get(liquid).canonical().getTextureSheet());
				drawTexturedModelRectFromIcon(cornerX + x + 1, cornerY + y + 1, icon, 14, 14);
			}
		}
	}

	public GuiPipeFluidsDiamond(InventoryPlayer player, TileGenericPipe tile) {
		super(new ContainerPipeFluidsDiamond(player, tile), null);

		container = (ContainerPipeFluidsDiamond) inventorySlots;

		PipeLogicLiquidsDiamond logic = (PipeLogicLiquidsDiamond) container.pipe.logic;

		// initialize liquids, and add null for empty filter
		liquids.put(null, null);
		liquids.putAll(LiquidDictionary.getLiquids());

		liquidsList.addAll(liquids.keySet());

		slots = new AdvancedSlot[6 * 9];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				int nr = i * 9 + j;
				slots[nr] = new LiquidSlot(nr, 8 + j * (16 + 2), 18 + i * (16 + 2));

				((LiquidSlot) slots[nr]).liquid = LiquidDictionary.findLiquidName(logic.liquidStacks[nr]);
			}
		}

		xSize = 175;
		ySize = 132;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String name = StringUtils.localize("item." + PipeFluidsDistribution.class.getSimpleName());

		fontRenderer.drawString(name, getCenteredOffset(name), 6, 0x404040);

		drawForegroundSelection(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/gfx/abo/gui/pipeLiquidsDiamond.png");

		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;
		drawTexturedModalRect(cornerX, cornerY, 0, 0, xSize, ySize);

		drawBackgroundSlots();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseB) {
		super.mouseClicked(mouseX, mouseY, mouseB);

		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;

		int position = getSlotAtLocation(mouseX - cornerX, mouseY - cornerY);

		AdvancedSlot slot = null;

		if (position < 0)
			return;

		slot = slots[position];

		if (slot instanceof LiquidSlot) {
			LiquidSlot liquidSlot = (LiquidSlot) slot;

			// left/right mouse button
			if (mouseB == 0 || mouseB == 1) {
				// advance to next/prev liquid
				for (Iterator<String> key = (mouseB == 0 ? liquidsList.iterator() : liquidsList.descendingIterator()); key.hasNext();) {
					if (key.next() == liquidSlot.liquid) {
						if (!key.hasNext())
							key = (mouseB == 0 ? liquidsList.iterator() : liquidsList.descendingIterator());
						liquidSlot.liquid = key.next();
					}
				}
			}

			// middle mouse button
			else
				liquidSlot.liquid = null;

			((PipeLogicLiquidsDiamond) container.pipe.logic).liquidStacks[liquidSlot.nr] = liquids.get(liquidSlot.liquid);

			container.detectAndSendChanges();

			if (container.pipe.worldObj.isRemote) {
				PacketFluidSlotChange packet = new PacketFluidSlotChange(container.pipe.xCoord, container.pipe.yCoord, container.pipe.zCoord, liquidSlot.nr,
						liquids.get(liquidSlot.liquid));
				ABOProxy.proxy.sendToServer(packet.getPacket());
			}
		}

	}
}
