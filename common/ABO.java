/** 
 * Copyright (C) 2012 Flow86
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
import java.util.LinkedList;
import java.util.logging.Logger;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import AdditionalBuildcraftObjects.pipes.PipeItemsRoundRobin;
import buildcraft.BuildCraftTransport;
import buildcraft.core.utils.Localization;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author Flow86
 * 
 */
@Mod(modid = "Additional-Buildcraft-Objects", name = "Additional-Buildcraft-Objects", version = "@ABO_VERSION@", dependencies = "required-after:BuildCraft|Transport")
public class ABO {
	public static final String VERSION = "@ABO_VERSION@";

	public static ABOConfiguration aboConfiguration;
	public static Logger aboLog = Logger.getLogger("Additional-Buildcraft-Objects");

	public static String texturePipes = "/gfx/abo/pipes.png";;

	public static int pipeItemsRoundRobinID = 10300;
	public static Item pipeItemsRoundRobin = null;

	@Instance("Additional-Buildcraft-Objects")
	public static ABO instance;

	@PreInit
	public void preInitialize(FMLPreInitializationEvent evt) {

		aboLog.setParent(FMLLog.getLogger());
		aboLog.info("Starting Additional-Buildcraft-Objects " + VERSION);
		aboLog.info("Copyright (c) Flow86, 2012");

		aboConfiguration = new ABOConfiguration(new File(evt.getModConfigurationDirectory(), "abo/main.conf"));
		try {
			aboConfiguration.load();

			pipeItemsRoundRobin = createPipe(pipeItemsRoundRobinID, PipeItemsRoundRobin.class, "Round Robin Transport Pipe",
					BuildCraftTransport.pipeItemsStone, Block.gravel, null);
		} finally {
			aboConfiguration.save();
		}
	}

	@Init
	public void load(FMLInitializationEvent evt) {

		Localization.addLocalization("/lang/abo/", "en_US");
	}

	private static class PipeRecipe {
		boolean isShapeless = false;
		ItemStack result;
		Object[] input;
	}

	private static LinkedList<PipeRecipe> pipeRecipes = new LinkedList<PipeRecipe>();

	private static Item createPipe(int defaultID, Class<? extends Pipe> clas, String descr, Object ingredient1, Object ingredient2, Object ingredient3) {
		String name = Character.toLowerCase(clas.getSimpleName().charAt(0)) + clas.getSimpleName().substring(1);

		Property prop = aboConfiguration.getOrCreateIntProperty(name + ".id", Configuration.CATEGORY_ITEM, defaultID);

		int id = prop.getInt(defaultID);
		ItemPipe res = BlockGenericPipe.registerPipe(id, clas);
		res.setItemName(clas.getSimpleName());
		LanguageRegistry.addName(res, descr);

		// Add appropriate recipe to temporary list
		PipeRecipe recipe = new PipeRecipe();

		if (ingredient1 != null && ingredient2 != null && ingredient3 != null) {
			recipe.result = new ItemStack(res, 8);
			recipe.input = new Object[] { "ABC", Character.valueOf('A'), ingredient1, Character.valueOf('B'), ingredient2, Character.valueOf('C'), ingredient3 };

			pipeRecipes.add(recipe);
		} else if (ingredient1 != null && ingredient2 != null) {
			recipe.isShapeless = true;
			recipe.result = new ItemStack(res, 1);
			recipe.input = new Object[] { ingredient1, ingredient2 };

			pipeRecipes.add(recipe);
		}

		return res;
	}

	public void loadRecipes() {
		// Add pipe recipes
		for (PipeRecipe pipe : pipeRecipes) {
			if (pipe.isShapeless) {
				GameRegistry.addShapelessRecipe(pipe.result, pipe.input);
			} else {
				GameRegistry.addRecipe(pipe.result, pipe.input);
			}
		}
	}
}