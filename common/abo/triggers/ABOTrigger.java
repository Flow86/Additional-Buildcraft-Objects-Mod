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

package abo.triggers;

import net.minecraft.util.Icon;
import abo.ABO;
import buildcraft.core.triggers.BCTrigger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Flow86
 * 
 */
public abstract class ABOTrigger extends BCTrigger {

	public ABOTrigger(int id, String uniqueTag) {
		super(id, "abo.trigger." + uniqueTag);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon() {
		return ABO.instance.itemIconProvider.getIcon(getIconIndex());
	}
}
