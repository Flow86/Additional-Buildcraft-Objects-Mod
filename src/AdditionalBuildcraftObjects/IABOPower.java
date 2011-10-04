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
