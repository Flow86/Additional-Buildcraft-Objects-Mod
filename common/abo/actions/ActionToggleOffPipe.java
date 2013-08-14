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

package abo.actions;

import abo.ItemIconProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ActionToggleOffPipe extends ABOAction {

	public ActionToggleOffPipe(int id) {
		super(id, "toggleoffpipe");
	}

	@Override
	public String getDescription() {
		return "Toggle Pipe Off";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconIndex() {
		return ItemIconProvider.ActionToggleOffPipe;
	}
}
