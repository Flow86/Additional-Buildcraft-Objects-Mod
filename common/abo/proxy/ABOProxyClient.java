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

package abo.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraftforge.client.MinecraftForgeClient;
import abo.ABO;
import abo.IconItemConstants;
import abo.IconTerrainConstants;
import buildcraft.transport.TransportProxyClient;

public class ABOProxyClient extends ABOProxy {
	@Override
	public void registerPipe(int itemID) {
		super.registerPipe(itemID);

		MinecraftForgeClient.registerItemRenderer(itemID, TransportProxyClient.pipeItemRenderer);
	}

	@Override
	public void loadTerrainIcons(ABO instance) {
		instance.terrainIcons = new Icon[IconTerrainConstants.MAX];
		TextureMap terrainTextures = Minecraft.getMinecraft().renderEngine.field_94154_l;

		instance.terrainIcons[IconTerrainConstants.PipeItemsBounceClosed] = terrainTextures.func_94245_a("abo:PipeItemsBounceClosed");
		instance.terrainIcons[IconTerrainConstants.PipeItemsBounce] = terrainTextures.func_94245_a("abo:PipeItemsBounce");

		instance.terrainIcons[IconTerrainConstants.PipeItemsCrossover] = terrainTextures.func_94245_a("abo:PipeItemsCrossover");

		instance.terrainIcons[IconTerrainConstants.PipeItemsExtract] = terrainTextures.func_94245_a("abo:PipeItemsExtract");
		instance.terrainIcons[IconTerrainConstants.PipeItemsExtractSide] = terrainTextures.func_94245_a("abo:PipeItemsExtractSide");

		instance.terrainIcons[IconTerrainConstants.PipeItemsInsert] = terrainTextures.func_94245_a("abo:PipeItemsInsert");
		instance.terrainIcons[IconTerrainConstants.PipeItemsRoundRobin] = terrainTextures.func_94245_a("abo:PipeItemsRoundRobin");
		instance.terrainIcons[IconTerrainConstants.PipeItemsStripes] = terrainTextures.func_94245_a("abo:PipeItemsStripes");

		instance.terrainIcons[IconTerrainConstants.PipeLiquidsBalance] = terrainTextures.func_94245_a("abo:PipeLiquidsBalance");

		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondCenter] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondCenter");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondDown] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondDown");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondUp] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondUp");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondNorth] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondNorth");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondSouth] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondSouth");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondWest] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondWest");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsDiamondEast] = terrainTextures.func_94245_a("abo:PipeLiquidsDiamondEast");

		instance.terrainIcons[IconTerrainConstants.PipeLiquidsGoldenIron] = terrainTextures.func_94245_a("abo:PipeLiquidsGoldenIron");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsGoldenIronSide] = terrainTextures.func_94245_a("abo:PipeLiquidsGoldenIronSide");

		instance.terrainIcons[IconTerrainConstants.PipeLiquidsValveClosed] = terrainTextures.func_94245_a("abo:PipeLiquidsValveClosed");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsValveClosedSide] = terrainTextures.func_94245_a("abo:PipeLiquidsValveClosedSide");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsValveOpen] = terrainTextures.func_94245_a("abo:PipeLiquidsValveOpen");
		instance.terrainIcons[IconTerrainConstants.PipeLiquidsValveOpenSide] = terrainTextures.func_94245_a("abo:PipeLiquidsValveOpenSide");

		instance.terrainIcons[IconTerrainConstants.PipePowerSwitchPowered] = terrainTextures.func_94245_a("abo:PipePowerSwitchPowered");
		instance.terrainIcons[IconTerrainConstants.PipePowerSwitchUnpowered] = terrainTextures.func_94245_a("abo:PipePowerSwitchUnpowered");
	}

	@Override
	public void loadItemIcons(ABO instance) {
		instance.itemIcons = new Icon[IconItemConstants.MAX];
		TextureMap itemTextures = Minecraft.getMinecraft().renderEngine.field_94155_m;

		instance.itemIcons[IconItemConstants.ActionEngineSafe] = itemTextures.func_94245_a("abo:actions/ActionEngineSafe");
	}
}