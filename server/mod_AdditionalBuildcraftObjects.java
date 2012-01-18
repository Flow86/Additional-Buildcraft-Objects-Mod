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

package net.minecraft.src;

import net.minecraft.src.AdditionalBuildcraftObjects.ABO;

/**
 * @author Flow86
 * 
 */
public class mod_AdditionalBuildcraftObjects extends BaseModMp {

	@Override
	public void load() {
	}
	
	@Override
	public void ModsLoaded() {
		super.ModsLoaded();
		ABO.initialize();
	}

	@Override
	public String getVersion() {
		return ABO.getVersion();
	}
}
