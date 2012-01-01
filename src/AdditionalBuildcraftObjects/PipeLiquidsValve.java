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

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.BuildCraftAPI;
import net.minecraft.src.buildcraft.api.ILiquidContainer;
import net.minecraft.src.buildcraft.api.IPowerReceptor;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.api.PowerProvider;
import net.minecraft.src.buildcraft.api.TileNetworkData;
import net.minecraft.src.buildcraft.core.ILiquid;
import net.minecraft.src.buildcraft.core.RedstonePowerProvider;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicWood;
import net.minecraft.src.buildcraft.transport.PipeTransportLiquids;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

/**
 * @author Flow86
 *
 */
public class PipeLiquidsValve extends Pipe implements IPowerReceptor, IABOSolid {
	PowerProvider powerProvider;

	public @TileNetworkData
	int liquidToExtract;

	private final int closedTexture = 0 * 16 + 0;
	private final int closedSideTexture = closedTexture + 2;
	private final int openTexture = 0 * 16 + 1;
	private final int openSideTexture = openTexture + 2;
	private int nextTexture = closedTexture;

	public PipeLiquidsValve(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicValve(), itemID);

		((PipeTransportLiquids)transport).flowRate = 80;
		((PipeTransportLiquids)transport).travelDelay = 2;

		powerProvider = new RedstonePowerProvider();
		powerProvider.configure(25, 1, 64, 1, 64);
		powerProvider.configurePowerPerdition(64, 1);
	}

	@Override
	public void prepareTextureFor(Orientations connection) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (connection == Orientations.Unknown || metadata != connection.ordinal()) {
			nextTexture = ((PipeLogicValve) logic).isPowered() ? openTexture : closedTexture;
		} else {
			nextTexture = ((PipeLogicValve) logic).isPowered() ? openSideTexture : closedSideTexture;
		}
	}
	
	public int liquidIDtoBlockID(int liquidID)
	{
		if (liquidID == Block.waterStill.blockID) {
			return Block.waterMoving.blockID;
		} else if (liquidID == Block.lavaStill.blockID) {
			return Block.lavaMoving.blockID;
		}
		
		for(Block b : Block.blocksList) {
			if(b instanceof ILiquid) {
				if(liquidID == ((ILiquid)b).stillLiquidId()) {
					return b.blockID;
				}
			}
		}
		return 0;
	}
	
	boolean isLiquidBlock(int liquid, int x, int y, int z) {
		int blockID = worldObj.getBlockId( x, y, z );
		if(blockID == 0)
			return false;
		
		return (liquid == Utils.liquidId(blockID));
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
			powerProvider.receiveEnergy(1, Orientations.YNeg);
		}

		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (liquidToExtract > 0 && meta < 6) {
			Position pos = new Position(xCoord, yCoord, zCoord, Orientations.values()[meta]);
			pos.moveForwards(1);

			TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);

			if (tile instanceof ILiquidContainer) {
				ILiquidContainer container = (ILiquidContainer) tile;

				int flowRate = ((PipeTransportLiquids) transport).flowRate;

				int extracted = container.empty(liquidToExtract > flowRate ? flowRate : liquidToExtract, false);

				pos.moveBackwards(2);
				TileEntity tile2 = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
				if( (tile2 == null || !isPipeConnected(tile2)) && (worldObj.isAirBlock((int) pos.x, (int) pos.y, (int) pos.z) || isLiquidBlock(container.getLiquidId(), (int) pos.x, (int) pos.y, (int) pos.z)))
				{
					int blockID = liquidIDtoBlockID(container.getLiquidId());
					int flowDecay = extracted / 10;
					if(worldObj.getBlockId((int) pos.x, (int) pos.y, (int) pos.z) == blockID) {
						flowDecay += worldObj.getBlockMetadata((int) pos.x, (int) pos.y, (int) pos.z);
						if(flowDecay > 14)
							flowDecay = 14;
					}
					
					worldObj.setBlockAndMetadataWithNotify((int) pos.x, (int) pos.y, (int) pos.z, blockID, flowDecay);
					worldObj.markBlocksDirty((int) pos.x, (int) pos.y, (int) pos.z, (int) pos.x, (int) pos.y, (int) pos.z);
					worldObj.markBlockNeedsUpdate((int) pos.x, (int) pos.y, (int) pos.z);
				}
				else
					extracted = ((PipeTransportLiquids) transport).fill(pos.orientation, extracted,
						container.getLiquidId(), true);

				container.empty(extracted, true);

				liquidToExtract -= extracted;
			}
		}
	}

	@Override
	public int getMainBlockTexture() {
		return nextTexture;
	}

	@Override
	public void setPowerProvider(PowerProvider provider) {
		// powerProvider = provider;
	}

	@Override
	public PowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() {
		if (powerProvider.useEnergy(1, 1, false) < 1)
			return;

		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (meta > 5) {
			return;
		}

		Position pos = new Position(xCoord, yCoord, zCoord, Orientations.values()[meta]);
		pos.moveForwards(1);
		int blockId = worldObj.getBlockId((int) pos.x, (int) pos.y, (int) pos.z);
		TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);

		if (tile == null || !(tile instanceof ILiquidContainer)
				|| PipeLogicWood.isExcludedFromExtraction(Block.blocksList[blockId])) {
			return;
		}

		if (tile instanceof ILiquidContainer && !(tile instanceof TileGenericPipe)) {
			if (liquidToExtract <= BuildCraftAPI.BUCKET_VOLUME) {
				liquidToExtract += powerProvider.useEnergy(1, 1, true) * BuildCraftAPI.BUCKET_VOLUME;

				// sendNetworkUpdate();
			}
		}

	}

	@Override
	public int powerRequest() {
		return getPowerProvider().maxEnergyReceived;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		// TODO: only allow specific sides
		return true;
	}
}
