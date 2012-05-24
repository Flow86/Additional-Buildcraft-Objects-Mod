/** 
 * Copyright (C) 2011 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.buildcraft.api.Action;

/**
 * 
 * 
 * @author mistaqur
 */
public class ActionSwitchOnPipe extends Action {

	public ActionSwitchOnPipe(int id) {
		super(id);
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
	public String getDescription() {
		return "Switch On Pipe";
	}
}
