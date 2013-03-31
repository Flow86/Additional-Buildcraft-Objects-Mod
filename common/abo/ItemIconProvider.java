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
public class ItemIconProvider implements IIconProvider {

	public static final int TriggerEngineSafe = 0;
	public static final int ActionSwitchOnPipe = 1;
	public static final int ActionToggleOnPipe = 2;
	public static final int ActionToggleOffPipe = 3;

	public static final int MAX = 4;

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

		_icons[TriggerEngineSafe] = iconRegister.registerIcon("abo:triggers/TriggerEngineSafe");
		_icons[ActionSwitchOnPipe] = iconRegister.registerIcon("abo:actions/ActionSwitchOnPipe");
		_icons[ActionToggleOnPipe] = iconRegister.registerIcon("abo:actions/ActionToggleOnPipe");
		_icons[ActionToggleOffPipe] = iconRegister.registerIcon("abo:actions/ActionToggleOffPipe");
	}
}
