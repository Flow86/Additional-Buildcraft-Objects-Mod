package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.Item;
import net.minecraft.src.mod_AdditionalBuildcraftObjects;
import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.Pipe;

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
	 * net.minecraft.src.buildcraft.transport.BlockGenericPipe#getTextureFile()
	 */
	@Override
	public String getTextureFile() {
		return mod_AdditionalBuildcraftObjects.customTexture;
	}
}
