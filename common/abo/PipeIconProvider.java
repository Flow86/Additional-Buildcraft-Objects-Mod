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

	public static int PipeItemsCompactorOn = 34;
	public static int PipeItemsCompactorOff = 35;

	public static final int MAX = 36;

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
		_icons = new Icon[MAX];

		_icons[PipeItemsBounceClosed] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsBounceClosed");
		_icons[PipeItemsBounce] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsBounce");

		_icons[PipeItemsCrossover] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsCrossover");

		_icons[PipeItemsExtraction] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsExtract");
		_icons[PipeItemsExtractionSide] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsExtractSide");

		_icons[PipeItemsInsertion] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsInsert");
		_icons[PipeItemsRoundRobin] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsRoundRobin");
		_icons[PipeItemsStripes] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsStripes");

		_icons[PipeLiquidsBalance] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsBalance");

		_icons[PipeLiquidsDiamondCenter] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondCenter");
		_icons[PipeLiquidsDiamondDown] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondDown");
		_icons[PipeLiquidsDiamondUp] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondUp");
		_icons[PipeLiquidsDiamondNorth] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondNorth");
		_icons[PipeLiquidsDiamondSouth] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondSouth");
		_icons[PipeLiquidsDiamondWest] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondWest");
		_icons[PipeLiquidsDiamondEast] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsDiamondEast");

		_icons[PipeLiquidsGoldenIron] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsGoldenIron");
		_icons[PipeLiquidsGoldenIronSide] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsGoldenIronSide");

		_icons[PipeLiquidsValveClosed] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsValveClosed");
		_icons[PipeLiquidsValveClosedSide] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsValveClosedSide");
		_icons[PipeLiquidsValveOpen] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsValveOpen");
		_icons[PipeLiquidsValveOpenSide] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsValveOpenSide");

		_icons[PipePowerSwitchPowered] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerSwitchPowered");
		_icons[PipePowerSwitchUnpowered] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerSwitchUnpowered");

		_icons[PipePowerDiamondCenter] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondCenter");
		_icons[PipePowerDiamondDown] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondDown");
		_icons[PipePowerDiamondUp] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondUp");
		_icons[PipePowerDiamondNorth] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondNorth");
		_icons[PipePowerDiamondSouth] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondSouth");
		_icons[PipePowerDiamondWest] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondWest");
		_icons[PipePowerDiamondEast] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerDiamondEast");

		_icons[PipePowerIron] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerIron");
		_icons[PipePowerIronSide] = iconRegister.registerIcon("additional-buildcraft-objects:PipePowerIronSide");

		_icons[PipeLiquidsPump] = iconRegister.registerIcon("additional-buildcraft-objects:PipeLiquidsPump");

		_icons[PipeItemsCompactorOn] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsCompactorOn");
		_icons[PipeItemsCompactorOff] = iconRegister.registerIcon("additional-buildcraft-objects:PipeItemsCompactorOff");
	}
}
