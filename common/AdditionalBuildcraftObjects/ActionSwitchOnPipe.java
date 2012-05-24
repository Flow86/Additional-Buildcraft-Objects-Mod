/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.BuildCraftCore;
import net.minecraft.src.buildcraft.api.Action;

public class ActionSwitchOnPipe extends Action {

	public ActionSwitchOnPipe (int id) {
		super (id);
	}

	@Override
	public String getTexture() {
		return ABO.triggerTexture;
	}

	@Override
	public int getIndexInTexture() {
		return 0 * 16 + 0;
	}

	@Override
	public String getDescription () {
		return "Switch On Pipe";
	}

}
