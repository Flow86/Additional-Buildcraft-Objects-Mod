package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_AdditionalBuildcraftObjects;
import net.minecraft.src.buildcraft.api.IBlockPipe;
import net.minecraft.src.buildcraft.api.IPipeConnection;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.forge.ITextureProvider;

/**
 * @author Flow86
 * 
 */
public class BlockValve extends BlockContainer implements ITextureProvider, IPipeConnection, IBlockPipe {
	/**
	 * @param blockId
	 */
	public BlockValve(int blockId) {
		super(blockId, Material.iron);

		blockIndexInTexture = 16 * 0 + 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.transport.BlockGenericPipe#isPipeConnected
	 * (net.minecraft.src.IBlockAccess, int, int, int, int, int, int)
	 */
	@Override
	public boolean isPipeConnected(IBlockAccess blockAccess, int x1, int y1, int z1, int x2, int y2, int z2) {
		TileEntity tile1 = blockAccess.getBlockTileEntity(x2, y2, z2);
		TileEntity tile2 = blockAccess.getBlockTileEntity(x2, y2, z2);

		if (!(tile2 instanceof TileGenericPipe))
			return false;

		Pipe pipe = ((TileGenericPipe) tile2).pipe;

		if (!pipe.isPipeConnected(tile1))
			return false;

		// straight X<->
		if (z1 == z2 && y1 == y2 && x1 != x2)
			return true;

		// straight Y<->
		if (z1 == z2 && y1 != y2 && x1 == x2)
			return true;

		// straight Z<->
		if (z1 != z2 && y1 == y2 && x1 == x2)
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.src.BlockContainer#getBlockEntity()
	 */
	@Override
	public TileEntity getBlockEntity() {
		return new TileValve();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.src.forge.ITextureProvider#getTextureFile()
	 */
	@Override
	public String getTextureFile() {
		return mod_AdditionalBuildcraftObjects.customTexture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.api.IBlockPipe#prepareTextureFor(net.minecraft
	 * .src.IBlockAccess, int, int, int,
	 * net.minecraft.src.buildcraft.api.Orientations)
	 */
	@Override
	public void prepareTextureFor(IBlockAccess blockAccess, int i, int j, int k, Orientations connection) {
	}
}
