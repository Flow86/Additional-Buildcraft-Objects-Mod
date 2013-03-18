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

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import buildcraft.api.core.IIconProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Flow86
 * 
 */
public class PipeIconProvider implements IIconProvider {

	public static final int PipeItemsBounceClosed = 0;
	public static final int PipeItemsBounce = 1;

	public static final int PipeItemsCrossover = 2;

	public static final int PipeItemsExtraction = 3;
	public static final int PipeItemsExtractionSide = 4;

	public static final int PipeItemsInsertion = 5;
	public static final int PipeItemsRoundRobin = 6;
	public static final int PipeItemsStripes = 7;

	public static final int PipeLiquidsBalance = 8;

	public static final int PipeLiquidsDiamondCenter = 9;
	public static final int PipeLiquidsDiamondDown = 10;
	public static final int PipeLiquidsDiamondUp = 11;
	public static final int PipeLiquidsDiamondNorth = 12;
	public static final int PipeLiquidsDiamondSouth = 13;
	public static final int PipeLiquidsDiamondWest = 14;
	public static final int PipeLiquidsDiamondEast = 15;

	public static final int PipeLiquidsGoldenIron = 16;
	public static final int PipeLiquidsGoldenIronSide = 17;

	public static final int PipeLiquidsValveClosed = 18;
	public static final int PipeLiquidsValveClosedSide = 19;
	public static final int PipeLiquidsValveOpen = 20;
	public static final int PipeLiquidsValveOpenSide = 21;

	public static final int PipePowerSwitchPowered = 22;
	public static final int PipePowerSwitchUnpowered = 23;

	public static final int PipePowerDiamondCenter = 24;
	public static final int PipePowerDiamondDown = 25;
	public static final int PipePowerDiamondUp = 26;
	public static final int PipePowerDiamondNorth = 27;
	public static final int PipePowerDiamondSouth = 28;
	public static final int PipePowerDiamondWest = 29;
	public static final int PipePowerDiamondEast = 30;

	public static final int PipePowerIron = 31;
	public static final int PipePowerIronSide = 32;

	public static final int MAX = 33;

	private boolean registered = false;

	@SideOnly(Side.CLIENT)
	private Icon[] _icons;

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int iconIndex) {
		return _icons[iconIndex];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void RegisterIcons(IconRegister iconRegister) {
		if (registered)
			return;
		registered = true;

		_icons = new Icon[MAX];

		_icons[PipeItemsBounceClosed] = iconRegister.func_94245_a("abo:PipeItemsBounceClosed");
		_icons[PipeItemsBounce] = iconRegister.func_94245_a("abo:PipeItemsBounce");

		_icons[PipeItemsCrossover] = iconRegister.func_94245_a("abo:PipeItemsCrossover");

		_icons[PipeItemsExtraction] = iconRegister.func_94245_a("abo:PipeItemsExtract");
		_icons[PipeItemsExtractionSide] = iconRegister.func_94245_a("abo:PipeItemsExtractSide");

		_icons[PipeItemsInsertion] = iconRegister.func_94245_a("abo:PipeItemsInsert");
		_icons[PipeItemsRoundRobin] = iconRegister.func_94245_a("abo:PipeItemsRoundRobin");
		_icons[PipeItemsStripes] = iconRegister.func_94245_a("abo:PipeItemsStripes");

		_icons[PipeLiquidsBalance] = iconRegister.func_94245_a("abo:PipeLiquidsBalance");

		_icons[PipeLiquidsDiamondCenter] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondCenter");
		_icons[PipeLiquidsDiamondDown] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondDown");
		_icons[PipeLiquidsDiamondUp] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondUp");
		_icons[PipeLiquidsDiamondNorth] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondNorth");
		_icons[PipeLiquidsDiamondSouth] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondSouth");
		_icons[PipeLiquidsDiamondWest] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondWest");
		_icons[PipeLiquidsDiamondEast] = iconRegister.func_94245_a("abo:PipeLiquidsDiamondEast");

		_icons[PipeLiquidsGoldenIron] = iconRegister.func_94245_a("abo:PipeLiquidsGoldenIron");
		_icons[PipeLiquidsGoldenIronSide] = iconRegister.func_94245_a("abo:PipeLiquidsGoldenIronSide");

		_icons[PipeLiquidsValveClosed] = iconRegister.func_94245_a("abo:PipeLiquidsValveClosed");
		_icons[PipeLiquidsValveClosedSide] = iconRegister.func_94245_a("abo:PipeLiquidsValveClosedSide");
		_icons[PipeLiquidsValveOpen] = iconRegister.func_94245_a("abo:PipeLiquidsValveOpen");
		_icons[PipeLiquidsValveOpenSide] = iconRegister.func_94245_a("abo:PipeLiquidsValveOpenSide");

		_icons[PipePowerSwitchPowered] = iconRegister.func_94245_a("abo:PipePowerSwitchPowered");
		_icons[PipePowerSwitchUnpowered] = iconRegister.func_94245_a("abo:PipePowerSwitchUnpowered");

		_icons[PipePowerDiamondCenter] = iconRegister.func_94245_a("abo:PipePowerDiamondCenter");
		_icons[PipePowerDiamondDown] = iconRegister.func_94245_a("abo:PipePowerDiamondDown");
		_icons[PipePowerDiamondUp] = iconRegister.func_94245_a("abo:PipePowerDiamondUp");
		_icons[PipePowerDiamondNorth] = iconRegister.func_94245_a("abo:PipePowerDiamondNorth");
		_icons[PipePowerDiamondSouth] = iconRegister.func_94245_a("abo:PipePowerDiamondSouth");
		_icons[PipePowerDiamondWest] = iconRegister.func_94245_a("abo:PipePowerDiamondWest");
		_icons[PipePowerDiamondEast] = iconRegister.func_94245_a("abo:PipePowerDiamondEast");

		_icons[PipePowerIron] = iconRegister.func_94245_a("abo:PipePowerIron");
		_icons[PipePowerIronSide] = iconRegister.func_94245_a("abo:PipePowerIronSide");
	}
}
