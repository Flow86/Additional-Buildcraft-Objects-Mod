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

import net.minecraft.src.Item;
import net.minecraft.src.World;
import net.minecraft.src.mod_AdditionalBuildcraftObjects;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;

/**
 * @author Flow86
 * 
 */
public class BlockABOPipe extends BlockGenericPipe {

	/**
	 * @param blockID
	 */
	public BlockABOPipe(int blockID) {
		super(blockID);
	}

	/**
	 * @param key
	 * @param clas
	 * @return
	 */
	public static Item registerPipe(int key, Class<? extends Pipe> clas) {
		Item item = new ItemABOPipe(key);

		pipes.put(item.shiftedIndex, clas);

		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * buildcraft.transport.BlockGenericPipe#getTextureFile()
	 */
	@Override
	public String getTextureFile() {
		return mod_AdditionalBuildcraftObjects.customTexture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.src.Block#isBlockSolidOnSide(net.minecraft.src.World,
	 * int, int, int, int)
	 */
	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		Pipe pipe = getPipe(world, i, j, k);
		if (pipe != null && pipe instanceof IABOSolid) {
			IABOSolid spipe = (IABOSolid)pipe;
			return spipe.isBlockSolidOnSide(world, i, j, k, side);
		}
		return false;
	}
}
