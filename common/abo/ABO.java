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

package abo;

import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Property;
import abo.actions.ABOActionProvider;
import abo.actions.ActionSwitchOnPipe;
import abo.actions.ActionToggleOffPipe;
import abo.actions.ActionToggleOnPipe;
import abo.gui.ABOGuiHandler;
import abo.items.ABOItem;
import abo.items.ItemGateSettingsDuplicator;
import abo.network.ABOPacketHandler;
import abo.pipes.items.PipeItemsBounce;
import abo.pipes.items.PipeItemsCompactor;
import abo.pipes.items.PipeItemsCrossover;
import abo.pipes.items.PipeItemsExtraction;
import abo.pipes.items.PipeItemsInsertion;
import abo.pipes.items.PipeItemsRoundRobin;
import abo.pipes.items.PipeItemsStripes;
import abo.pipes.liquids.PipeLiquidsBalance;
import abo.pipes.liquids.PipeLiquidsDiamond;
import abo.pipes.liquids.PipeLiquidsGoldenIron;
import abo.pipes.liquids.PipeLiquidsPump;
import abo.pipes.liquids.PipeLiquidsValve;
import abo.pipes.power.PipePowerDistribution;
import abo.pipes.power.PipePowerIron;
import abo.pipes.power.PipePowerSwitch;
import abo.proxy.ABOProxy;
import abo.triggers.ABOTriggerProvider;
import abo.triggers.TriggerEngineSafe;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.ITrigger;
import buildcraft.core.utils.Localization;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.blueprints.BptItemPipeDiamond;
import buildcraft.transport.blueprints.BptItemPipeIron;
import buildcraft.transport.blueprints.BptItemPipeWooden;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author Flow86
 * 
 */
@Mod(modid = "Additional-Buildcraft-Objects", name = "Additional-Buildcraft-Objects", version = "@ABO_VERSION@", dependencies = "required-after:Forge@7.7.2.682,);required-after:BuildCraft|Transport;required-after:BuildCraft|Energy")
@NetworkMod(channels = { "ABO" }, packetHandler = ABOPacketHandler.class)
public class ABO {
	public static final String VERSION = "@ABO_VERSION@";

	public IIconProvider itemIconProvider = new ItemIconProvider();
	public IIconProvider pipeIconProvider = new PipeIconProvider();

	public static ABOConfiguration aboConfiguration;
	public static Logger aboLog = Logger.getLogger("Additional-Buildcraft-Objects");

	public static int itemGateSettingsDuplicatorID = 10100;
	public static Item itemGateSettingsDuplicator = null;

	public static int pipeLiquidsValveID = 10200;
	public static Item pipeLiquidsValve = null;

	public static int pipeLiquidsGoldenIronID = 10201;
	public static Item pipeLiquidsGoldenIron = null;

	public static int pipeLiquidsBalanceID = 10203;
	public static Item pipeLiquidsBalance = null;

	public static int pipeLiquidsDiamondID = 10204;
	public static Item pipeLiquidsDiamond = null;

	public static int pipeLiquidsWaterPumpID = 14054;
	public static Item pipeLiquidsWaterPump = null;

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

	public static int pipeItemsStripesID = 4071;
	public static Item pipeItemsStripes = null;

	public static int pipePowerSwitchID = 10400;
	public static Item pipePowerSwitch = null;

	public static int pipePowerIronID = 10401;
	public static Item pipePowerIron = null;

	public static int pipeDistributionConductiveID = 10402;
	public static Item pipeDistributionConductive = null;

	public static int triggerEngineSafeID = 128;
	public static ITrigger triggerEngineSafe = null;

	public static int actionSwitchOnPipeID = 128;
	public static IAction actionSwitchOnPipe = null;

	public static int actionToggleOnPipeID = 129;
	public static IAction actionToggleOnPipe = null;

	public static int actionToggleOffPipeID = 130;
	public static IAction actionToggleOffPipe = null;

	@Instance("Additional-Buildcraft-Objects")
	public static ABO instance;

	@PreInit
	public void preInitialize(FMLPreInitializationEvent evt) {

		aboLog.setParent(FMLLog.getLogger());
		aboLog.info("Starting Additional-Buildcraft-Objects #@BUILD_NUMBER@ " + VERSION
				+ " (Built for Minecraft @MINECRAFT_VERSION@ with Buildcraft @BUILDCRAFT_VERSION@ and Forge @FORGE_VERSION@");
		aboLog.info("Copyright (c) Flow86, 2011-2013");

		aboConfiguration = new ABOConfiguration(new File(evt.getModConfigurationDirectory(), "abo/main.conf"));
		try {
			aboConfiguration.load();

			itemGateSettingsDuplicator = createItem(itemGateSettingsDuplicatorID, ItemGateSettingsDuplicator.class, "Gate Settings Duplicator",
					BuildCraftCore.wrenchItem, BuildCraftTransport.pipeGateAutarchic, null);

			pipeLiquidsValve = createPipe(pipeLiquidsValveID, PipeLiquidsValve.class, "Valve Pipe", 1, BuildCraftTransport.pipeLiquidsWood,
					BuildCraftTransport.pipeGateAutarchic, null);

			pipeLiquidsGoldenIron = createPipe(pipeLiquidsGoldenIronID, PipeLiquidsGoldenIron.class, "Golden Iron Waterproof Pipe", 1,
					BuildCraftTransport.pipeLiquidsGold, BuildCraftTransport.pipeLiquidsIron, null);

			pipeLiquidsBalance = createPipe(pipeLiquidsBalanceID, PipeLiquidsBalance.class, "Balancing Waterproof Pipe", 1,
					BuildCraftTransport.pipeLiquidsWood, new ItemStack(BuildCraftEnergy.engineBlock, 1, 0), BuildCraftTransport.pipeLiquidsWood);

			pipeLiquidsDiamond = createPipe(pipeLiquidsDiamondID, PipeLiquidsDiamond.class, "Diamond Waterproof Pipe", 1, BuildCraftTransport.pipeItemsDiamond,
					BuildCraftTransport.pipeWaterproof, null);

			if (GameRegistry.findItem("APUnofficial", "item.PipeLiquidsWaterPump") == null) {
				pipeLiquidsWaterPump = createPipe(pipeLiquidsWaterPumpID, PipeLiquidsPump.class, "Water Pump Pipe", 1, false, new Object[] { " L ", "rPr",
						" W ", 'r', Item.redstone, 'P', BuildCraftCore.ironGearItem, 'L', BuildCraftTransport.pipeLiquidsGold, 'W',
						BuildCraftTransport.pipeLiquidsWood });
			}

			pipeItemsRoundRobin = createPipe(pipeItemsRoundRobinID, PipeItemsRoundRobin.class, "RoundRobin Transport Pipe", 1,
					BuildCraftTransport.pipeItemsStone, Block.gravel, null);

			pipeItemsCompactor = createPipe(pipeItemsCompactorID, PipeItemsCompactor.class, "Compactor Pipe", 1, BuildCraftTransport.pipeItemsStone,
					Block.pistonBase, null);

			pipeItemsInsertion = createPipe(pipeItemsInsertionID, PipeItemsInsertion.class, "Insertion Pipe", 1, BuildCraftTransport.pipeItemsIron,
					new ItemStack(Item.dyePowder, 1, 2), null);

			pipeItemsExtraction = createPipe(pipeItemsExtractionID, PipeItemsExtraction.class, "Extraction Transport Pipe", 1,
					BuildCraftTransport.pipeItemsWood, Block.planks, null);

			pipeItemsBounce = createPipe(pipeItemsBounceID, PipeItemsBounce.class, "Bounce Transport Pipe", 1, BuildCraftTransport.pipeItemsStone,
					Block.cobblestone, null);

			pipeItemsCrossover = createPipe(pipeItemsCrossoverID, PipeItemsCrossover.class, "Crossover Transport Pipe", 1, BuildCraftTransport.pipeItemsStone,
					BuildCraftTransport.pipeItemsIron, null);

			pipeItemsStripes = createPipe(pipeItemsStripesID, PipeItemsStripes.class, "Stripes Transport Pipe", 8, new ItemStack(Item.dyePowder, 1, 0),
					Block.glass, new ItemStack(Item.dyePowder, 1, 11));

			pipePowerSwitch = createPipe(pipePowerSwitchID, PipePowerSwitch.class, "Power Switch Pipe", 1, BuildCraftTransport.pipePowerGold, Block.lever, null);

			pipePowerIron = createPipe(pipePowerIronID, PipePowerIron.class, "Iron Power Pipe", 1, Item.redstone, BuildCraftTransport.pipeItemsIron, null);

			pipeDistributionConductive = createPipe(pipeDistributionConductiveID, PipePowerDistribution.class, "Distribution Conductive Pipe", 2, pipePowerIron,
					BuildCraftTransport.pipeItemsDiamond, pipePowerIron);

			triggerEngineSafe = new TriggerEngineSafe(triggerEngineSafeID);
			actionSwitchOnPipe = new ActionSwitchOnPipe(actionSwitchOnPipeID);
			actionToggleOnPipe = new ActionToggleOnPipe(actionToggleOnPipeID);
			actionToggleOffPipe = new ActionToggleOffPipe(actionToggleOffPipeID);

			ActionManager.registerActionProvider(new ABOActionProvider());
			ActionManager.registerTriggerProvider(new ABOTriggerProvider());

			BuildCraftCore.itemBptProps[pipeItemsExtraction.itemID] = new BptItemPipeWooden();
			BuildCraftCore.itemBptProps[pipeLiquidsValve.itemID] = new BptItemPipeIron();
			BuildCraftCore.itemBptProps[pipeLiquidsGoldenIron.itemID] = new BptItemPipeIron();
			BuildCraftCore.itemBptProps[pipeLiquidsDiamond.itemID] = new BptItemPipeDiamond();
			BuildCraftCore.itemBptProps[pipePowerIron.itemID] = new BptItemPipeIron();
		} finally {
			if (aboConfiguration.hasChanged())
				aboConfiguration.save();
		}
	}

	@Init
	public void load(FMLInitializationEvent evt) {

		Localization.addLocalization("/lang/abo/", "en_US");

		// fix problem with autarchic gate initialization sequence
		ABORecipe recipe = new ABORecipe();

		recipe.itemID = pipeLiquidsValve.itemID;
		recipe.isShapeless = true;
		recipe.result = new ItemStack(pipeLiquidsValve, 1);
		recipe.input = new Object[] { BuildCraftTransport.pipeLiquidsWood, BuildCraftTransport.pipeGateAutarchic };

		aboRecipes.add(recipe);

		loadRecipes();

		NetworkRegistry.instance().registerGuiHandler(instance, new ABOGuiHandler());
	}

	private static class ABORecipe {
		int itemID;
		boolean isPipe = false;
		boolean isShapeless = false;
		ItemStack result;
		Object[] input;
	}

	private static LinkedList<ABORecipe> aboRecipes = new LinkedList<ABORecipe>();

	private static Item createItem(int defaultID, Class<? extends ABOItem> clazz, String descr, Object ingredient1, Object ingredient2, Object ingredient3) {
		String name = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);

		Property prop = aboConfiguration.getItem(name + ".id", defaultID);

		int id = prop.getInt(defaultID);

		while (id < Item.itemsList.length && Item.itemsList[id] != null)
			id++;

		if (id >= Item.itemsList.length) {
			aboLog.log(Level.SEVERE, "Cannot find free ID for Item + " + name + " starting from " + defaultID);
			return null;
		}

		prop.set(id);

		Item item = null;
		try {
			item = clazz.getConstructor(int.class).newInstance(id);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (item == null)
			return item;

		item.setUnlocalizedName(clazz.getSimpleName());
		LanguageRegistry.addName(item, descr);
		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));

		addReceipe(item, 1, ingredient1, ingredient2, ingredient3);

		return item;
	}

	private static void addReceipe(Item item, int count, Object ingredient1, Object ingredient2, Object ingredient3) {
		if (ingredient1 != null && ingredient2 != null && ingredient3 != null) {
			addReceipe(item, count, false,
					new Object[] { "ABC", Character.valueOf('A'), ingredient1, Character.valueOf('B'), ingredient2, Character.valueOf('C'), ingredient3 });
		} else if (ingredient1 != null && ingredient2 != null) {
			addReceipe(item, count, true, new Object[] { ingredient1, ingredient2 });
		}
	}

	private static void addReceipe(Item item, int count, boolean isShapeless, Object[] ingredients) {
		// Add appropriate recipe to temporary list
		ABORecipe recipe = new ABORecipe();

		recipe.isPipe = (item instanceof ItemPipe);
		recipe.itemID = item.itemID;
		recipe.isShapeless = isShapeless;
		recipe.input = ingredients;
		recipe.result = new ItemStack(item, count);

		aboRecipes.add(recipe);
	}

	private static ItemPipe createPipe(int defaultID, Class<? extends Pipe> clazz, String descr) {
		String name = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);

		Property prop = aboConfiguration.getItem(name + ".id", defaultID);

		int id = prop.getInt(defaultID);

		try {
			// search for free id
			while (BlockGenericPipe.isPipeRegistered(id))
				++id;

			prop.set(id);
		} catch (NoSuchMethodError e) {
			// e.printStackTrace();
		}

		ItemPipe pipe = BlockGenericPipe.registerPipe(id, clazz);
		pipe.setUnlocalizedName(clazz.getSimpleName());
		LanguageRegistry.addName(pipe, descr);
		GameRegistry.registerItem(pipe, pipe.getUnlocalizedName().replace("item.", ""));

		return pipe;
	}

	private static Item createPipe(int defaultID, Class<? extends Pipe> clazz, String descr, int count, boolean isShapeless, Object[] ingredients) {
		ItemPipe pipe = createPipe(defaultID, clazz, descr);

		addReceipe(pipe, count, isShapeless, ingredients);

		return pipe;
	}

	private static Item createPipe(int defaultID, Class<? extends Pipe> clazz, String descr, int count, Object ingredient1, Object ingredient2,
			Object ingredient3) {
		ItemPipe pipe = createPipe(defaultID, clazz, descr);

		addReceipe(pipe, count, ingredient1, ingredient2, ingredient3);

		return pipe;
	}

	public void loadRecipes() {
		// Add pipe recipes
		for (ABORecipe recipe : aboRecipes) {
			if (recipe.isShapeless) {
				GameRegistry.addShapelessRecipe(recipe.result, recipe.input);
			} else {
				GameRegistry.addRecipe(recipe.result, recipe.input);
			}

			if (recipe.isPipe)
				ABOProxy.proxy.registerPipe(recipe.itemID);
		}
	}
}