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

package AdditionalBuildcraftObjects;

import net.minecraft.src.forge.NetworkMod;

/**
 * @author Flow86
 * 
 */
public class mod_AdditionalBuildcraftObjects extends NetworkMod {

	@Override
	public void load() {
	}

	@Override
	public void modsLoaded() {
		super.modsLoaded();
		ABO.initialize();
	}

	@Override
	public String getVersion() {
		return ABO.getVersion();
	}

	@Override
	public boolean clientSideRequired() {
		return true;
	}

	@Override
	public boolean serverSideRequired() {
		return false;
	}
}
