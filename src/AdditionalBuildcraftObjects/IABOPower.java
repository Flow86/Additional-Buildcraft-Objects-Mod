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

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

/**
 * @author Flow86
 *
 */
public interface IABOPower {
	/**
	 * @param iblockaccess
	 * @param i
	 * @param j
	 * @param k
	 * @param side
	 * @return
	 */
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int side);

	/**
	 * @param world
	 * @param i
	 * @param j
	 * @param k
	 * @param side
	 * @return
	 */
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int side);
}
