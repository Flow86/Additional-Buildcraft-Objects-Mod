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

package AdditionalBuildcraftObjects;

import java.io.File;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.Configuration;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftTransport;
import buildcraft.mod_BuildCraftCore;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.gates.Action;
import buildcraft.api.gates.Trigger;
import buildcraft.core.CoreProxy;
import buildcraft.transport.BptBlockPipe;
import buildcraft.transport.BptItemPipeDiamond;
import buildcraft.transport.BptItemPipeIron;
import buildcraft.transport.BptItemPipeWodden;
import buildcraft.transport.Pipe;

public class ABO {
	private static boolean initialized = false;

	public static int blockABOPipeID = 200;
	public static Block blockABOPipe = null;

	// @MLProp(min = 0.0D, max = 255.0D)
	// public static int blockRedstonePowerConverterID = 201;
	// public static Block blockRedstonePowerConverter = null;

	public static int pipeLiquidsValveID = 10200;
	public static Item pipeLiquidsValve = null;

	public static int pipeLiquidsGoldenIronID = 10201;
	public static Item pipeLiquidsGoldenIron = null;

	// @MLProp(min = 256.0D, max = 32000.0D)
	// public static int pipeLiquidsFlowMeterID = 10202;
	// public static Item pipeLiquidsFlowMeter = null;

	public static int pipeLiquidsBalanceID = 10203;
	public static Item pipeLiquidsBalance = null;

	public static int pipeLiquidsDiamondID = 10204;
	public static Item pipeLiquidsDiamond = null;

	public static int pipeItemsRoundRobinID = 10300;
	public static Item pipeItemsRoundRobin = null;

	public static int pipeItemsCompactorID = 10301;
	public static Item pipeItemsCompactor = null;

	public static int pipeItemsInsertionID = 10302;
	public static Item pipeItemsInsertion = null;

	public static int pipeItemsExtractionID = 10303;
	public static Item pipeItemsExtraction = null;

	public static int pipeItemsBounceID = 10304;
	public static Item pipeItemsBounce = null;

	public static int pipeItemsCrossoverID = 10305;
	public static Item pipeItemsCrossover = null;

	public static int pipePowerSwitchID = 10400;
	public static Item pipePowerSwitch = null;

	// @MLProp(min = 256.0D, max = 32000.0D)
	// public static int pipePowerEngineControlID = 10401;
	// public static Item pipePowerEngineControl = null;

	public static int triggerEngineControlID = 128;
	public static Trigger triggerEngineControl = null;

	public static int actionSwitchOnPipeID = 128;
	public static Action actionSwitchOnPipe = null;

	public static String customTexturePath = "/net/minecraft/src/AdditionalBuildcraftObjects/gui";
	public static int[] customTextures = new int[30];

	// public static String customSprites =
	// "/net/minecraft/src/AdditionalBuildcraftObjects/gui/item_textures.png";

	public static String triggerTexture = "/net/minecraft/src/AdditionalBuildcraftObjects/gui/trigger_textures.png";

	public static void initialize() {
		if (initialized) {
			return;
		}

		initialized = true;

		mod_BuildCraftCore.initialize();
		BuildCraftTransport.initialize();
		BuildCraftEnergy.initialize();

		// set continuous current model
		BuildCraftCore.continuousCurrentModel = true;

		setupProperties();

		ABOProxy.preloadTexture(customTexturePath);
		ABOProxy.AddCustomTextures(customTexturePath);

		while (blockABOPipeID < Block.blocksList.length && Block.blocksList[blockABOPipeID] != null)
			blockABOPipeID++;
		if (blockABOPipeID >= Block.blocksList.length || Block.blocksList[blockABOPipeID] != null) {
			ModLoader.throwException("Additional Buildcraft Objects:", new RuntimeException(
					"no free block-ID for Additional Buildcraft Objects's basic pipe!"));
			return;
		}

		blockABOPipe = new BlockABOPipe(blockABOPipeID);
		ModLoader.registerBlock(blockABOPipe);
		//ModLoader.registerTileEntity(ItemABOPipe.class, "AdditionalBuildcraftObjects.ItemABOPipe",new RenderPipe());

		pipeLiquidsValve = createPipe(pipeLiquidsValveID, PipeLiquidsValve.class, "Valve Pipe", 1,
				BuildCraftTransport.pipeLiquidsWood, Block.lever, BuildCraftTransport.pipeLiquidsWood);

		pipeLiquidsGoldenIron = createPipe(pipeLiquidsGoldenIronID, PipeLiquidsGoldenIron.class,
				"Golden Iron Waterproof Pipe", 1, BuildCraftTransport.pipeLiquidsGold,
				BuildCraftTransport.pipeLiquidsIron, null);

		pipeLiquidsBalance = createPipe(pipeLiquidsBalanceID, PipeLiquidsBalance.class, "Balance Pipe", 1,
				BuildCraftTransport.pipeLiquidsWood, new ItemStack(BuildCraftEnergy.engineBlock, 1, 0),
				BuildCraftTransport.pipeLiquidsWood);

		pipeLiquidsDiamond = createPipe(pipeLiquidsDiamondID, PipeLiquidsDiamond.class, "Diamond Liquids Pipe", 1,
				BuildCraftTransport.pipeItemsDiamond, BuildCraftTransport.pipeWaterproof, null);

		pipeItemsRoundRobin = createPipe(pipeItemsRoundRobinID, PipeItemsRoundRobin.class, "RoundRobin Transport Pipe",
				1, BuildCraftTransport.pipeItemsStone, Block.gravel, null);

		pipeItemsCompactor = createPipe(pipeItemsCompactorID, PipeItemsCompactor.class, "Compactor Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.pistonBase, null);

		pipeItemsInsertion = createPipe(pipeItemsInsertionID, PipeItemsInsertion.class, "Insertion Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Item.redstone, null);

		pipeItemsExtraction = createPipe(pipeItemsExtractionID, PipeItemsExtraction.class, "Extraction Transport Pipe",
				1, BuildCraftTransport.pipeItemsStone, Block.planks, null);

		pipeItemsBounce = createPipe(pipeItemsBounceID, PipeItemsBounce.class, "Bounce Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.cobblestone, null);

		pipeItemsCrossover = createPipe(pipeItemsCrossoverID, PipeItemsCrossover.class, "Crossover Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, BuildCraftTransport.pipeItemsIron, null);

		pipePowerSwitch = createPipe(pipePowerSwitchID, PipePowerSwitch.class, "Power Switch Pipe", 1,
				BuildCraftTransport.pipePowerGold, Block.lever, null);

		triggerEngineControl = new TriggerEngineControl(triggerEngineControlID);
		actionSwitchOnPipe = new ActionSwitchOnPipe(actionSwitchOnPipeID);

		BuildCraftAPI.registerTriggerProvider(new ABOTriggerProvider());

		new BptBlockPipe(blockABOPipeID);

		BuildCraftCore.itemBptProps[pipeItemsExtraction.shiftedIndex] = new BptItemPipeWodden();
		BuildCraftCore.itemBptProps[pipeLiquidsValve.shiftedIndex] = new BptItemPipeIron();
		BuildCraftCore.itemBptProps[pipeLiquidsGoldenIron.shiftedIndex] = new BptItemPipeIron();
		BuildCraftCore.itemBptProps[pipeLiquidsDiamond.shiftedIndex] = new BptItemPipeDiamond();
	}

	private static void setupProperties() {
		File configFile = new File(CoreProxy.getBuildCraftBase(), "config/ABO.cfg");
		Configuration configuration = new Configuration(configFile);
		configuration.load();

		blockABOPipeID = Integer.parseInt(configuration.getOrCreateIntProperty("blockABOPipeID", Configuration.CATEGORY_BLOCK, 200).value);

		pipeLiquidsValveID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeLiquidsValveID", Configuration.CATEGORY_ITEM, 10200).value);
		pipeLiquidsGoldenIronID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeLiquidsGoldenIronID", Configuration.CATEGORY_ITEM, 10201).value);
		pipeLiquidsBalanceID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeLiquidsBalanceID", Configuration.CATEGORY_ITEM, 10203).value);
		pipeLiquidsDiamondID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeLiquidsDiamondID", Configuration.CATEGORY_ITEM, 10204).value);
		pipeItemsRoundRobinID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeItemsRoundRobinID", Configuration.CATEGORY_ITEM, 10300).value);
		pipeItemsCompactorID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeItemsCompactorID", Configuration.CATEGORY_ITEM, 10301).value);
		pipeItemsInsertionID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeItemsInsertionID", Configuration.CATEGORY_ITEM, 10302).value);
		pipeItemsExtractionID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeItemsExtractionID", Configuration.CATEGORY_ITEM, 10303).value);
		pipeItemsBounceID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeItemsBounceID", Configuration.CATEGORY_ITEM, 10304).value);
		pipeItemsCrossoverID = Integer.parseInt(configuration.getOrCreateIntProperty("pipeItemsCrossoverID", Configuration.CATEGORY_ITEM, 10305).value);
		pipePowerSwitchID = Integer.parseInt(configuration.getOrCreateIntProperty("pipePowerSwitchID", Configuration.CATEGORY_ITEM, 10400).value);

		triggerEngineControlID = Integer.parseInt(configuration.getOrCreateIntProperty("triggerEngineControlID", Configuration.CATEGORY_GENERAL, 128).value);
		actionSwitchOnPipeID = Integer.parseInt(configuration.getOrCreateIntProperty("actionSwitchOnPipeID", Configuration.CATEGORY_GENERAL, 128).value);

		configuration.save();
	}

	private static Item createPipe(int id, Class<? extends Pipe> clas, String descr, int count, Object r1, Object r2,
			Object r3) {
		Item res = BlockABOPipe.registerPipe(id, clas);
		res.setItemName(clas.getSimpleName());
		CoreProxy.addName(res, descr);

		if (r1 != null && r2 != null && r3 != null) {
			ModLoader.addRecipe(
					new ItemStack(res, count),
					new Object[] { "ABC", Character.valueOf('A'), r1, Character.valueOf('B'), r2,
							Character.valueOf('C'), r3 });
		} else if (r1 != null && r2 != null) {
			ModLoader.addRecipe(new ItemStack(res, count),
					new Object[] { "AB", Character.valueOf('A'), r1, Character.valueOf('B'), r2 });
		}

		ABOProxy.registerItemInRenderer(res.shiftedIndex);
		return res;
	}

	public static String getVersion() {
		return "1.0.0 (BC {$buildcraft.version}, Forge {$forge.version})";
	}
}
