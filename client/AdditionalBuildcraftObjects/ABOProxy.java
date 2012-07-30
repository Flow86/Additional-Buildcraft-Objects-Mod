/** 
 * Copyright (C) 2011 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.forge.IItemRenderer;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.buildcraft.core.CoreProxy;

import org.lwjgl.opengl.GL11;

public class ABOProxy implements IItemRenderer {
	private static ABOProxy instance = null;

	private ABOProxy() {
	}

	public static ABOProxy instance() {
		if (instance == null)
			instance = new ABOProxy();

		return instance;
	}

	public static void registerItemInRenderer(int itemID) {
		MinecraftForgeClient.registerItemRenderer(itemID, instance());
	}

	public static void preloadTexture(String pipeTexturePath) {
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_0.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_1.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_2.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_3.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_16.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_32.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_33.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_48.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_49.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_64.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_65.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_66.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_80.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_96.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_112.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_113.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_128.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_144.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_145.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_160.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_161.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_176.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_192.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_208.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_209.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_210.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_211.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_212.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_213.png");
	        MinecraftForgeClient.preloadTexture(pipeTexturePath + "/ABOPipe_214.png");
	}

	public static void AddCustomTextures(String pipeTexturePath) {
	        ABO.customTextures[0] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_0.png");//
	        ABO.customTextures[1] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_1.png");//
	        ABO.customTextures[2] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_2.png");//
	        ABO.customTextures[3] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_3.png");//
	        ABO.customTextures[4] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_16.png");//
	        ABO.customTextures[5] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_32.png");//
	        ABO.customTextures[6] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_33.png");//
	        ABO.customTextures[7] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_48.png");//
	        ABO.customTextures[8] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_49.png");//
	        ABO.customTextures[9] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_64.png");
	        ABO.customTextures[10] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_65.png");
	        ABO.customTextures[11] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_66.png");
	        ABO.customTextures[12] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_80.png");//
	        ABO.customTextures[13] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_96.png");
	        ABO.customTextures[14] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_112.png");//
	        ABO.customTextures[15] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_113.png");//
	        ABO.customTextures[16] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_128.png");//
	        ABO.customTextures[17] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_144.png");//
	        ABO.customTextures[18] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_145.png");//
	        ABO.customTextures[19] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_160.png");//
	        ABO.customTextures[20] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_161.png");//
	        ABO.customTextures[21] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_176.png");//
	        ABO.customTextures[22] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_192.png");
	        ABO.customTextures[23] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_208.png");//
	        ABO.customTextures[24] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_209.png");//
	        ABO.customTextures[25] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_210.png");//
	        ABO.customTextures[26] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_211.png");//
	        ABO.customTextures[27] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_212.png");//
	        ABO.customTextures[28] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_213.png");//
	        ABO.customTextures[29] = CoreProxy.addCustomTexture(pipeTexturePath + "/ABOPipe_214.png");//

	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type){
			case ENTITY: return true;
			case EQUIPPED: return true;
			case INVENTORY: return true;
		}

		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case ENTITY:
			renderPipeItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case EQUIPPED:
			renderPipeItem((RenderBlocks) data[0], item, -0.4f, 0.50f, 0.35f);
			break;
		case INVENTORY:
			renderPipeItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		}
	}

	private void renderPipeItem(RenderBlocks render, ItemStack item, float translateX, float translateY,
			float translateZ) {
		Tessellator tessellator = Tessellator.instance;

		Block block = ABO.blockABOPipe;
		int textureID = ((ItemABOPipe)Item.itemsList[item.itemID]).getTextureIndex();

		block.setBlockBounds(Utils.pipeMinPos, 0.0F, Utils.pipeMinPos, Utils.pipeMaxPos, 1.0F, Utils.pipeMaxPos);
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(translateX, translateY, translateZ);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		render.renderBottomFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		render.renderTopFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		render.renderEastFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		render.renderWestFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		render.renderNorthFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		render.renderSouthFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}
