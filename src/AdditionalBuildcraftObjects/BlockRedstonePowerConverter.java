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

import java.util.Random;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_AdditionalBuildcraftObjects;
import net.minecraft.src.buildcraft.api.IPipeConnection;
import net.minecraft.src.buildcraft.transport.PipeTransportPower;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.forge.ITextureProvider;

public class BlockRedstonePowerConverter extends BlockContainer implements ITextureProvider, IPipeConnection {

	public BlockRedstonePowerConverter(int i) {
		super(i, Material.iron);

		setHardness(0.5F);
		setRequiresSelfNotify();
		setTickOnLoad(true);
	}

    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	public String getTextureFile() {
		return mod_AdditionalBuildcraftObjects.customTexture;
	}

	@Override
	public int getBlockTextureFromSide(int i) {
		return getBlockTextureFromSideAndMetadata(i, 0);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		switch (i) {
		default:
			return 6 * 16 + 0;
		}
	}
	
	@Override
	public int tickRate() {
		return 25;
	}

    @Override
	public void updateTick(World world, int i, int j, int k, Random random)
    {
		TileRedstonePowerConverter tile = (TileRedstonePowerConverter) world.getBlockTileEntity(i, j, k);
		tile.updateEntity();
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
    }

	@Override
	public TileEntity getBlockEntity() {
		return new TileRedstonePowerConverter();
	}

	@Override
	public boolean isPipeConnected(IBlockAccess blockAccess, int x1, int y1, int z1, int x2, int y2, int z2) {
		TileGenericPipe tile2 = (TileGenericPipe) blockAccess.getBlockTileEntity(x2, y2, z2);
		if (tile2 == null)
			return false;

		// TODO:
		return (tile2.pipe.transport instanceof PipeTransportPower);
	}

	@Override
	public boolean isPoweringTo(IBlockAccess blockAccess, int i, int j, int k, int side) {
		TileRedstonePowerConverter tile = (TileRedstonePowerConverter) blockAccess.getBlockTileEntity(i, j, k);
		if (tile == null)
			return false;
		return tile.isPowered();
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int side) {
		TileRedstonePowerConverter tile = (TileRedstonePowerConverter) world.getBlockTileEntity(i, j, k);
		if (tile == null)
			return false;
		return tile.isPowered();
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
}
