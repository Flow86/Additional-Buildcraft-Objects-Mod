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
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import abo.network.PacketFluidSlotChange;
import abo.pipes.fluids.PipeFluidsDistribution;
import abo.proxy.ABOProxy;
import buildcraft.core.gui.GuiAdvancedInterface;
import buildcraft.core.render.FluidRenderer;
import buildcraft.core.utils.StringUtils;
import buildcraft.transport.TileGenericPipe;

public class GuiPipeFluidsDiamond extends GuiAdvancedInterface {
	private static final ResourceLocation TEXTURE = new ResourceLocation("additional-buildcraft-objects", "textures/gui/pipeLiquidsDiamond.png");

	private final ContainerPipeFluidsDiamond container;
	private final HashMap<String, Fluid> fluids = new HashMap<String, Fluid>();
	private final LinkedList<String> fluidsList = new LinkedList<String>();

	public class FluidSlot extends AdvancedSlot {
		public Fluid fluid;
		public final int nr;

		public FluidSlot(int nr, int x, int y) {
			super(x, y);
			this.nr = nr;
		}

		@Override
		public void drawSprite(int cornerX, int cornerY) {
			if (fluid != null)
				FluidRenderer.setColorForFluidStack(new FluidStack(fluid, 100));
			super.drawSprite(cornerX, cornerY);
		}

		@Override
		public Icon getIcon() {
			return FluidRenderer.getFluidTexture(fluid, false);
		}

		@Override
		public ResourceLocation getTexture() {
			return FluidRenderer.getFluidSheet(fluid);
		}
	}

	public GuiPipeFluidsDiamond(InventoryPlayer player, TileGenericPipe tile) {
		super(new ContainerPipeFluidsDiamond(player, tile), null);

		container = (ContainerPipeFluidsDiamond) inventorySlots;

		// initialize liquids, and add null for empty filter
		fluids.put(null, null);
		fluids.putAll(FluidRegistry.getRegisteredFluids());

		fluidsList.addAll(fluids.keySet());

		slots = new AdvancedSlot[6 * 9];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				int nr = i * 9 + j;
				slots[nr] = new FluidSlot(nr, 8 + j * (16 + 2), 18 + i * (16 + 2));

				((FluidSlot) slots[nr]).fluid = container.pipe.fluids[nr];
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
		mc.renderEngine.func_110577_a(TEXTURE);

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

		if (slot instanceof FluidSlot) {
			FluidSlot fluidSlot = (FluidSlot) slot;

			// left/right mouse button
			if (mouseB == 0 || mouseB == 1) {
				// advance to next/prev liquid
				for (Iterator<String> key = (mouseB == 0 ? fluidsList.iterator() : fluidsList.descendingIterator()); key.hasNext();) {
					if (fluidSlot.fluid == null || key.next() == fluidSlot.fluid.getName()) {
						if (!key.hasNext())
							key = (mouseB == 0 ? fluidsList.iterator() : fluidsList.descendingIterator());
						fluidSlot.fluid = fluids.get(key.next());
					}
				}
			}

			// middle mouse button
			else
				fluidSlot.fluid = null;

			container.pipe.fluids[fluidSlot.nr] = fluids.get(fluidSlot.fluid);

			container.detectAndSendChanges();

			if (container.pipe.container.worldObj.isRemote) {
				PacketFluidSlotChange packet = new PacketFluidSlotChange(container.pipe.container.xCoord, container.pipe.container.yCoord, container.pipe.container.zCoord, fluidSlot.nr,
						fluidSlot.fluid);
				ABOProxy.proxy.sendToServer(packet.getPacket());
			}
		}

	}
}
