/** 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of the my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src;

import net.minecraft.src.AdditionalBuildcraftObjects.BlockABOPipe;
import net.minecraft.src.AdditionalBuildcraftObjects.ItemABOPipe;
import net.minecraft.src.AdditionalBuildcraftObjects.PipeItemsRoundRobin;
import net.minecraft.src.AdditionalBuildcraftObjects.PipeLiquidsBalance;
import net.minecraft.src.AdditionalBuildcraftObjects.PipeLiquidsFlowMeter;
import net.minecraft.src.AdditionalBuildcraftObjects.PipeLiquidsGoldenIron;
import net.minecraft.src.AdditionalBuildcraftObjects.PipeLiquidsValve;
import net.minecraft.src.AdditionalBuildcraftObjects.PipePowerSwitch;
import net.minecraft.src.buildcraft.core.CoreProxy;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.forge.ICustomItemRenderer;
import net.minecraft.src.forge.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

/**
 * @author Flow86
 * 
 */
public class mod_AdditionalBuildcraftObjects extends BaseModMp implements ICustomItemRenderer {
	private static boolean initialized = false;
	public static mod_AdditionalBuildcraftObjects instance;

	@MLProp(min = 0.0D, max = 255.0D)
	public static int blockABOPipeID = 200;
	public static Block blockABOPipe = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsValveID = 10200;
	public static Item pipeLiquidsValve = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsGoldenIronID = 10201;
	public static Item pipeLiquidsGoldenIron = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsFlowMeterID = 10202;
	public static Item pipeLiquidsFlowMeter = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsBalanceID = 10203;
	public static Item pipeLiquidsBalance = null;
	
	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsRoundRobinID = 10300;
	public static Item pipeItemsRoundRobin = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipePowerSwitchID = 10400;
	public static Item pipePowerSwitch = null;

	public static String customTexture = "/net/minecraft/src/AdditionalBuildcraftObjects/gui/block_textures.png";

	// public static String customSprites =
	// "/net/minecraft/src/AdditionalBuildcraftObjects/gui/item_textures.png";

	/**
	 * 
	 */
	public mod_AdditionalBuildcraftObjects() {
		instance = this;
	}

	@Override
	public void ModsLoaded() {
		super.ModsLoaded();

		initialize();
	}

	public void initialize() {
		if (initialized) {
			return;
		}

		initialized = true;

		mod_BuildCraftCore.initialize();
		BuildCraftTransport.initialize();

		MinecraftForgeClient.preloadTexture(customTexture);

		blockABOPipe = new BlockABOPipe(blockABOPipeID);
		ModLoader.RegisterTileEntity(BlockABOPipe.class, "net.minecraft.src.AdditionalBuildcraftObjects.ABOPipe");

		pipeLiquidsValve = createPipe(pipeLiquidsValveID, PipeLiquidsValve.class, "Valve Pipe", 1,
				BuildCraftTransport.pipeLiquidsWood, Block.lever, BuildCraftTransport.pipeLiquidsWood);

		pipeLiquidsGoldenIron = createPipe(pipeLiquidsGoldenIronID, PipeLiquidsGoldenIron.class, "Golden Iron Waterproof Pipe", 1,
				BuildCraftTransport.pipeLiquidsGold, BuildCraftTransport.pipeLiquidsIron, null);

		pipeLiquidsFlowMeter = createPipe(pipeLiquidsFlowMeterID, PipeLiquidsFlowMeter.class, "Flow Meter Pipe", 1,
				BuildCraftTransport.pipeLiquidsGold, Block.torchRedstoneActive, null);

		pipeLiquidsBalance = createPipe(pipeLiquidsBalanceID, PipeLiquidsBalance.class, "Balance Pipe", 1,
				BuildCraftTransport.pipeLiquidsWood, pipeLiquidsFlowMeter, BuildCraftTransport.pipeLiquidsWood);
		
		pipeItemsRoundRobin = createPipe(pipeItemsRoundRobinID, PipeItemsRoundRobin.class, "RoundRobin Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.gravel, null);

		pipePowerSwitch = createPipe(pipePowerSwitchID, PipePowerSwitch.class, "Power Switch Pipe", 1,
				BuildCraftTransport.pipePowerGold, Block.lever, null);
	}

	/**
	 * @param id
	 * @param clas
	 * @param descr
	 * @param r1
	 * @param r2
	 * @param r3
	 * @return
	 */
	private static Item createPipe(int id, Class<? extends Pipe> clas, String descr, int count, Object r1, Object r2, Object r3) {
		Item res = BlockABOPipe.registerPipe(id, clas);
		res.setItemName(clas.getSimpleName());
		CoreProxy.addName(res, descr);

		CraftingManager craftingmanager = CraftingManager.getInstance();

		if (r1 != null && r2 != null && r3 != null) {
			craftingmanager.addRecipe(new ItemStack(res, count), new Object[] { "ABC",
					Character.valueOf('A'), r1, Character.valueOf('B'), r2, Character.valueOf('C'), r3 });
		} else if (r1 != null && r2 != null) {
			craftingmanager.addRecipe(new ItemStack(res, count), new Object[] { "AB", Character.valueOf('A'), r1,
					Character.valueOf('B'), r2 });
		}

		MinecraftForgeClient.registerCustomItemRenderer(res.shiftedIndex, mod_AdditionalBuildcraftObjects.instance);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.forge.ICustomItemRenderer#renderInventory(net.minecraft
	 * .src.RenderBlocks, int, int)
	 */
	@Override
	public void renderInventory(RenderBlocks renderblocks, int itemID, int meta) {
		Tessellator tessellator = Tessellator.instance;

		Block block = blockABOPipe;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.src.BaseMod#Version()
	 */
	@Override
	public String Version() {
		return "0.4 (MC 1.8.1, BC 2.2.2)";
	}
}
