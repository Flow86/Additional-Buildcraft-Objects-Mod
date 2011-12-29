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

import net.minecraft.src.buildcraft.transport.pipes.PipePowerWood;

/**
 * @author Flow86
 *
 */
public class PipePowerEngineControl extends PipePowerWood {

	public PipePowerEngineControl(int itemID) {
		super(itemID);
	}

	@Override
	public int getMainBlockTexture() {
		return 12 * 16 + 0;
	}

}
