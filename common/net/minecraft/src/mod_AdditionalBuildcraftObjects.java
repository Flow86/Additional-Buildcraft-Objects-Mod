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

package net.minecraft.src;

import buildcraft.core.CoreProxy;
import buildcraft.transport.Pipe;
import AdditionalBuildcraftObjects.ABOProxy;
import AdditionalBuildcraftObjects.BlockABOPipe;
import AdditionalBuildcraftObjects.PipeItemsBounce;
import AdditionalBuildcraftObjects.PipeItemsCompactor;
import AdditionalBuildcraftObjects.PipeItemsCrossover;
import AdditionalBuildcraftObjects.PipeItemsExtraction;
import AdditionalBuildcraftObjects.PipeItemsInsertion;
import AdditionalBuildcraftObjects.PipeItemsRoundRobin;
import AdditionalBuildcraftObjects.PipeLiquidsBalance;
import AdditionalBuildcraftObjects.PipeLiquidsDiamond;
import AdditionalBuildcraftObjects.PipeLiquidsGoldenIron;
import AdditionalBuildcraftObjects.PipeLiquidsValve;
import AdditionalBuildcraftObjects.PipePowerSwitch;
import net.minecraft.src.forge.NetworkMod;

/**
 * @author Flow86
 * 
 */
public class mod_AdditionalBuildcraftObjects extends NetworkMod {

	@Override
	public void load() {
	}

	@Override
	public void modsLoaded() {
		super.modsLoaded();
		initialize();
	}

	@Override
	public boolean clientSideRequired() {
		return true;
	}

	@Override
	public boolean serverSideRequired() {
		return false;
	}
	
	private static boolean initialized = false;

	@MLProp(min = 0.0D, max = 255.0D)
	public static int blockABOPipeID = 200;
	public static Block blockABOPipe = null;

	//@MLProp(min = 0.0D, max = 255.0D)
	//public static int blockRedstonePowerConverterID = 201;
	//public static Block blockRedstonePowerConverter = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsValveID = 10200;
	public static Item pipeLiquidsValve = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsGoldenIronID = 10201;
	public static Item pipeLiquidsGoldenIron = null;

	//@MLProp(min = 256.0D, max = 32000.0D)
	//public static int pipeLiquidsFlowMeterID = 10202;
	//public static Item pipeLiquidsFlowMeter = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsBalanceID = 10203;
	public static Item pipeLiquidsBalance = null;
	
	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeLiquidsDiamondID = 10204;
	public static Item pipeLiquidsDiamond = null;
	
	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsRoundRobinID = 10300;
	public static Item pipeItemsRoundRobin = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsCompactorID = 10301;
	public static Item pipeItemsCompactor = null;
	
	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsInsertionID = 10302;
	public static Item pipeItemsInsertion = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsExtractionID = 10303;
	public static Item pipeItemsExtraction = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsBounceID = 10304;
	public static Item pipeItemsBounce = null;

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipeItemsCrossoverID = 10305;
	public static Item pipeItemsCrossover = null;
	
	@MLProp(min = 256.0D, max = 32000.0D)
	public static int pipePowerSwitchID = 10400;
	public static Item pipePowerSwitch = null;

	//@MLProp(min = 256.0D, max = 32000.0D)
	//public static int pipePowerEngineControlID = 10401;
	//public static Item pipePowerEngineControl = null;

	//@MLProp(min = 128.0D, max = 256.0D)
	//public static int triggerEngineControlID = 128;
	//public static Trigger triggerEngineControl = null;
	
	public static String customTexture = "AdditionalBuildcraftObjects/gui/block_textures.png";

	// public static String customSprites =
	// "/net/minecraft/src/AdditionalBuildcraftObjects/gui/item_textures.png";

	//public static String triggerTexture = "/net/minecraft/src/AdditionalBuildcraftObjects/gui/trigger_textures.png";

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
		
		ABOProxy.preloadTexture(customTexture);

		while(blockABOPipeID < Block.blocksList.length && Block.blocksList[blockABOPipeID] != null)
			blockABOPipeID++;
		if(blockABOPipeID >= Block.blocksList.length || Block.blocksList[blockABOPipeID] != null) {
			ModLoader.throwException("Additional Buildcraft Objects:", new RuntimeException("no free block-ID for Additional Buildcraft Objects's basic pipe!"));
			return;
		}

		blockABOPipe = new BlockABOPipe(blockABOPipeID);
		ModLoader.registerBlock(blockABOPipe);
		//ModLoader.registerTileEntity(ItemABOPipe.class, "net.minecraft.src.AdditionalBuildcraftObjects.ItemABOPipe");
		
		pipeLiquidsValve = createPipe(pipeLiquidsValveID, PipeLiquidsValve.class, "Valve Pipe", 1,
				BuildCraftTransport.pipeLiquidsWood, Block.lever, BuildCraftTransport.pipeLiquidsWood);

		pipeLiquidsGoldenIron = createPipe(pipeLiquidsGoldenIronID, PipeLiquidsGoldenIron.class, "Golden Iron Waterproof Pipe", 1,
				BuildCraftTransport.pipeLiquidsGold, BuildCraftTransport.pipeLiquidsIron, null);

		pipeLiquidsBalance = createPipe(pipeLiquidsBalanceID, PipeLiquidsBalance.class, "Balance Pipe", 1,
				BuildCraftTransport.pipeLiquidsWood, new ItemStack(BuildCraftEnergy.engineBlock, 1, 0), BuildCraftTransport.pipeLiquidsWood);

		pipeLiquidsDiamond = createPipe(pipeLiquidsDiamondID, PipeLiquidsDiamond.class, "Diamond Liquids Pipe", 1,
				BuildCraftTransport.pipeItemsDiamond, BuildCraftTransport.pipeWaterproof, null);

		pipeItemsRoundRobin = createPipe(pipeItemsRoundRobinID, PipeItemsRoundRobin.class, "RoundRobin Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.gravel, null);

		pipeItemsCompactor = createPipe(pipeItemsCompactorID, PipeItemsCompactor.class, "Compactor Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.pistonBase, null);

		pipeItemsInsertion = createPipe(pipeItemsInsertionID, PipeItemsInsertion.class, "Insertion Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Item.redstone, null);
		
		pipeItemsExtraction = createPipe(pipeItemsExtractionID, PipeItemsExtraction.class, "Extraction Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.planks, null);
		
		pipeItemsBounce = createPipe(pipeItemsBounceID, PipeItemsBounce.class, "Bounce Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, Block.cobblestone, null);
		
		pipeItemsCrossover = createPipe(pipeItemsCrossoverID, PipeItemsCrossover.class, "Crossover Transport Pipe", 1,
				BuildCraftTransport.pipeItemsStone, BuildCraftTransport.pipeItemsIron, null);
		
		pipePowerSwitch = createPipe(pipePowerSwitchID, PipePowerSwitch.class, "Power Switch Pipe", 1,
				BuildCraftTransport.pipePowerGold, Block.lever, null);

		//triggerEngineControl = new TriggerEngineControl(triggerEngineControlID);
		
		//BuildCraftAPI.registerTriggerProvider(new ABOTriggerProvider());
	}

	private static Item createPipe(int id, Class<? extends Pipe> clas, String descr, int count, Object r1, Object r2, Object r3) {
		Item res = BlockABOPipe.registerPipe(id, clas);
		res.setItemName(clas.getSimpleName());
		CoreProxy.addName(res, descr);

		if (r1 != null && r2 != null && r3 != null) {
			ModLoader.addRecipe(new ItemStack(res, count), new Object[] { "ABC",
					Character.valueOf('A'), r1, Character.valueOf('B'), r2, Character.valueOf('C'), r3 });
		} else if (r1 != null && r2 != null) {
			ModLoader.addRecipe(new ItemStack(res, count), new Object[] { "AB", Character.valueOf('A'), r1,
					Character.valueOf('B'), r2 });
		}

		ABOProxy.registerItemInRenderer(res.shiftedIndex);
		return res;
	}

	@Override
	public String getVersion() {
		return "0.9.1-97 (MC 1.2.5, BC 2.2.14)";
	}
}
