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

import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;

/**
 * This pipe will always prefer to use the opposite direction,
 * so items will go "straight through"
 * 
 * @author blakmajik
 *         ported to BC > 2.2 by Flow86
 */
public class PipeItemsCrossover extends Pipe {

	public PipeItemsCrossover(int itemID) {
		super(new PipeTransportItemsCrossover(), new PipeLogicStone(), itemID);
	}

	@Override
	public int getMainBlockTexture() {
		return ABO.customTextures[21];
	}
}
