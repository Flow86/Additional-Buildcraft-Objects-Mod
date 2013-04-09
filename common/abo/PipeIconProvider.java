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

	public static final int PipeLiquidsPump = 33;

	public static final int MAX = 34;

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
	public void registerIcons(IconRegister iconRegister) {
		if (registered)
			return;
		registered = true;

		_icons = new Icon[MAX];

		_icons[PipeItemsBounceClosed] = iconRegister.registerIcon("abo:PipeItemsBounceClosed");
		_icons[PipeItemsBounce] = iconRegister.registerIcon("abo:PipeItemsBounce");

		_icons[PipeItemsCrossover] = iconRegister.registerIcon("abo:PipeItemsCrossover");

		_icons[PipeItemsExtraction] = iconRegister.registerIcon("abo:PipeItemsExtract");
		_icons[PipeItemsExtractionSide] = iconRegister.registerIcon("abo:PipeItemsExtractSide");

		_icons[PipeItemsInsertion] = iconRegister.registerIcon("abo:PipeItemsInsert");
		_icons[PipeItemsRoundRobin] = iconRegister.registerIcon("abo:PipeItemsRoundRobin");
		_icons[PipeItemsStripes] = iconRegister.registerIcon("abo:PipeItemsStripes");

		_icons[PipeLiquidsBalance] = iconRegister.registerIcon("abo:PipeLiquidsBalance");

		_icons[PipeLiquidsDiamondCenter] = iconRegister.registerIcon("abo:PipeLiquidsDiamondCenter");
		_icons[PipeLiquidsDiamondDown] = iconRegister.registerIcon("abo:PipeLiquidsDiamondDown");
		_icons[PipeLiquidsDiamondUp] = iconRegister.registerIcon("abo:PipeLiquidsDiamondUp");
		_icons[PipeLiquidsDiamondNorth] = iconRegister.registerIcon("abo:PipeLiquidsDiamondNorth");
		_icons[PipeLiquidsDiamondSouth] = iconRegister.registerIcon("abo:PipeLiquidsDiamondSouth");
		_icons[PipeLiquidsDiamondWest] = iconRegister.registerIcon("abo:PipeLiquidsDiamondWest");
		_icons[PipeLiquidsDiamondEast] = iconRegister.registerIcon("abo:PipeLiquidsDiamondEast");

		_icons[PipeLiquidsGoldenIron] = iconRegister.registerIcon("abo:PipeLiquidsGoldenIron");
		_icons[PipeLiquidsGoldenIronSide] = iconRegister.registerIcon("abo:PipeLiquidsGoldenIronSide");

		_icons[PipeLiquidsValveClosed] = iconRegister.registerIcon("abo:PipeLiquidsValveClosed");
		_icons[PipeLiquidsValveClosedSide] = iconRegister.registerIcon("abo:PipeLiquidsValveClosedSide");
		_icons[PipeLiquidsValveOpen] = iconRegister.registerIcon("abo:PipeLiquidsValveOpen");
		_icons[PipeLiquidsValveOpenSide] = iconRegister.registerIcon("abo:PipeLiquidsValveOpenSide");

		_icons[PipePowerSwitchPowered] = iconRegister.registerIcon("abo:PipePowerSwitchPowered");
		_icons[PipePowerSwitchUnpowered] = iconRegister.registerIcon("abo:PipePowerSwitchUnpowered");

		_icons[PipePowerDiamondCenter] = iconRegister.registerIcon("abo:PipePowerDiamondCenter");
		_icons[PipePowerDiamondDown] = iconRegister.registerIcon("abo:PipePowerDiamondDown");
		_icons[PipePowerDiamondUp] = iconRegister.registerIcon("abo:PipePowerDiamondUp");
		_icons[PipePowerDiamondNorth] = iconRegister.registerIcon("abo:PipePowerDiamondNorth");
		_icons[PipePowerDiamondSouth] = iconRegister.registerIcon("abo:PipePowerDiamondSouth");
		_icons[PipePowerDiamondWest] = iconRegister.registerIcon("abo:PipePowerDiamondWest");
		_icons[PipePowerDiamondEast] = iconRegister.registerIcon("abo:PipePowerDiamondEast");

		_icons[PipePowerIron] = iconRegister.registerIcon("abo:PipePowerIron");
		_icons[PipePowerIronSide] = iconRegister.registerIcon("abo:PipePowerIronSide");

		_icons[PipeLiquidsPump] = iconRegister.registerIcon("abo:PipeLiquidsPump");
	}
}
