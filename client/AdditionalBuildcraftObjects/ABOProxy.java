package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.forge.ICustomItemRenderer;
import net.minecraft.src.forge.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

public class ABOProxy implements ICustomItemRenderer {
	private static ABOProxy instance = null;

	private ABOProxy() {
	}
	
	public static ABOProxy instance()
	{
		if(instance == null)
			instance = new ABOProxy();
		
		return instance;
	}

	public static void registerItemInRenderer (int itemID) {
		MinecraftForgeClient.registerCustomItemRenderer(itemID, instance());
	}
	
	public static void preloadTexture(String texture) {
		MinecraftForgeClient.preloadTexture(texture);
	}

	@Override
	public void renderInventory(RenderBlocks renderblocks, int itemID, int meta) {
		Tessellator tessellator = Tessellator.instance;

		Block block = ABO.blockABOPipe;
		int textureID = ((ItemABOPipe) Item.itemsList[itemID]).getTextureIndex();

		block.setBlockBounds(Utils.pipeMinPos, 0.0F, Utils.pipeMinPos, Utils.pipeMaxPos, 1.0F, Utils.pipeMaxPos);
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}
