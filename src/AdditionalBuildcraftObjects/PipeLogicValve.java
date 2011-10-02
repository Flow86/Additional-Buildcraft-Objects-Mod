/** 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of the my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.BuildCraftCore;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.api.IPipeEntry;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.core.ILiquidContainer;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogic;
import net.minecraft.src.buildcraft.transport.PipeLogicWood;
import net.minecraft.src.buildcraft.transport.PipeTransport;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

/**
 * @author Flow86
 * 
 */
public class PipeLogicValve extends PipeLogic {
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.src.buildcraft.transport.PipeLogic#onBlockPlaced()
	 */
	@Override
	public void onBlockPlaced() {
		super.onBlockPlaced();

		worldObj.setBlockMetadata(xCoord, yCoord, zCoord, 0);
		switchPosition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.transport.PipeLogic#blockActivated(net.minecraft
	 * .src.EntityPlayer)
	 */
	@Override
	public boolean blockActivated(EntityPlayer entityplayer) {
		super.blockActivated(entityplayer);

		if (entityplayer.getCurrentEquippedItem() != null
				&& entityplayer.getCurrentEquippedItem().getItem() == BuildCraftCore.wrenchItem) {

			switchPosition();
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);

			return true;
		}

		return false;
	}
	
	/**
	 * 
	 */
	public void switchPosition() {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		int nextMetadata = metadata;
		
		for (int l = 0; l < 6; l += 2) {
			nextMetadata = (nextMetadata+2 % 6);
			
			int nextMetadataS = (nextMetadata + ((nextMetadata%2 == 0) ? 0 : -1)) % 6;
			int nextMetadataU = (nextMetadata + ((nextMetadata%2 == 0) ? 1 : 0)) % 6;
			
			//System.out.println("M:" + metadata + " N:" + nextMetadata + " S:" + nextMetadataS + " U:" + nextMetadataU);

			Position pos = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataS]);
			pos.moveForwards(1.0);

			Position pos2 = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataU]);
			pos2.moveForwards(1.0);

			TileEntity base = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
			TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
			TileEntity tile2 = worldObj.getBlockTileEntity((int) pos2.x, (int) pos2.y, (int) pos2.z);

			PipeTransport baseT = ((TileGenericPipe) base).pipe.transport;

			boolean fail = false;
			boolean fail2 = false;

			if (tile instanceof TileGenericPipe) {
				PipeTransport transport = ((TileGenericPipe) tile).pipe.transport;
				PipeLogic logic = ((TileGenericPipe) tile).pipe.logic;
				if (!baseT.getClass().isAssignableFrom(transport.getClass()) || !transport.getClass().isAssignableFrom(baseT.getClass()) ||
						logic instanceof PipeLogicWood || logic instanceof PipeLogicValve)
					fail = true;
			}

			if (tile2 instanceof TileGenericPipe) {
				PipeTransport transport = ((TileGenericPipe) tile2).pipe.transport;
				PipeLogic logic = ((TileGenericPipe) tile2).pipe.logic;
				if (!baseT.getClass().isAssignableFrom(transport.getClass()) || !transport.getClass().isAssignableFrom(baseT.getClass()) ||
						logic instanceof PipeLogicWood || logic instanceof PipeLogicValve)
					fail2 = true;
			}
			
			if(fail && fail2)
				continue;

			if (tile instanceof IPipeEntry || tile instanceof IInventory || tile instanceof ILiquidContainer
					|| tile instanceof TileGenericPipe) {
				worldObj.setBlockMetadata(xCoord, yCoord, zCoord, nextMetadataS);
				baseT.onNeighborBlockChange();
				return;
			}

			if (tile2 instanceof IPipeEntry || tile2 instanceof IInventory || tile2 instanceof ILiquidContainer
					|| tile2 instanceof TileGenericPipe) {
				worldObj.setBlockMetadata(xCoord, yCoord, zCoord, nextMetadataU);
				baseT.onNeighborBlockChange();
				return;
			}
		}
	}

	@Override
	public void onNeighborBlockChange() {
		super.onNeighborBlockChange();
		
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		int nextMetadataS = (metadata + ((metadata%2 == 0) ? 0 : -1)) % 6;
		int nextMetadataU = (metadata + ((metadata%2 == 0) ? 1 : 0)) % 6;

		Position pos = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataS]);
		pos.moveForwards(1.0);

		Position pos2 = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataU]);
		pos2.moveForwards(1.0);

		TileEntity base = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
		TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
		TileEntity tile2 = worldObj.getBlockTileEntity((int) pos2.x, (int) pos2.y, (int) pos2.z);
		
		PipeTransport baseT = ((TileGenericPipe) base).pipe.transport;
		
		boolean fail = false;
		boolean fail2 = false;
		
		if (tile instanceof TileGenericPipe) {
			PipeTransport transport = ((TileGenericPipe) tile).pipe.transport;
			PipeLogic logic = ((TileGenericPipe) tile).pipe.logic;
			if (!baseT.getClass().isAssignableFrom(transport.getClass()) || !transport.getClass().isAssignableFrom(baseT.getClass()) ||
					logic instanceof PipeLogicWood || logic instanceof PipeLogicValve)
				fail = true;
		}

		if (tile2 instanceof TileGenericPipe) {
			PipeTransport transport = ((TileGenericPipe) tile2).pipe.transport;
			PipeLogic logic = ((TileGenericPipe) tile2).pipe.logic;
			if (!baseT.getClass().isAssignableFrom(transport.getClass()) || !transport.getClass().isAssignableFrom(baseT.getClass()) ||
					logic instanceof PipeLogicWood || logic instanceof PipeLogicValve)
				fail2 = true;
		}
		
		// both neighbours are pipes but invalid, switch position
		if(fail && fail2) {
			switchPosition();
			return;
		}

		// is neighbour one okay?
		if (!fail && (tile instanceof IPipeEntry || tile instanceof IInventory || tile instanceof ILiquidContainer
				|| tile instanceof TileGenericPipe)) {
			return;
		}

		// neighbour one is not okay, is neighbour two okay?
		if (!fail2 && (tile2 instanceof IPipeEntry || tile2 instanceof IInventory || tile2 instanceof ILiquidContainer
				|| tile2 instanceof TileGenericPipe)) {
			return;
		}
		
		// both neighbours are invalid, switch position
		switchPosition();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.transport.PipeLogic#isPipeConnected(net.
	 * minecraft.src.TileEntity)
	 */
	@Override
	public boolean isPipeConnected(TileEntity tile2) {
		Pipe pipe1 = null;
		Pipe pipe2 = null;

		TileEntity tile1 = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);

		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (tile2 instanceof IPipeEntry || tile2 instanceof IInventory || tile2 instanceof ILiquidContainer) {
			int newMetaData = -1;
			boolean result = false;
			// straight X<->
			if ((metadata == 4 || metadata == 5) && zCoord == tile2.zCoord && yCoord == tile2.yCoord
					&& xCoord != tile2.xCoord) {
				result = true;
				newMetaData = 4;
			}

			// straight Y<->
			else if ((metadata == 0 || metadata == 1) && zCoord == tile2.zCoord && yCoord != tile2.yCoord
					&& xCoord == tile2.xCoord) {
				result = true;
				newMetaData = 0;
			}

			// straight Z<->
			else if ((metadata == 2 || metadata == 3) && zCoord != tile2.zCoord && yCoord == tile2.yCoord
					&& xCoord == tile2.xCoord) {
				result = true;
				newMetaData = 2;
			}

			if (metadata >= 6 && newMetaData >= 0)
				worldObj.setBlockMetadata(xCoord, yCoord, zCoord, newMetaData);

			return result;
		}

		if (metadata >= 6) {
			switchPosition();
			return false;
		}

		if (tile1 instanceof TileGenericPipe) {
			pipe1 = ((TileGenericPipe) tile1).pipe;
		}

		if (tile2 instanceof TileGenericPipe) {
			pipe2 = ((TileGenericPipe) tile2).pipe;
		}

		if (pipe1 == null || pipe2 == null || pipe2.logic instanceof PipeLogicValve)
			return false;

		// straight X<->
		if ((metadata == 4 || metadata == 5) && zCoord == pipe2.zCoord && yCoord == pipe2.yCoord
				&& xCoord != pipe2.xCoord)
			return true;

		// straight Y<->
		else if ((metadata == 0 || metadata == 1) && zCoord == pipe2.zCoord && yCoord != pipe2.yCoord
				&& xCoord == pipe2.xCoord)
			return true;

		// straight Z<->
		else if ((metadata == 2 || metadata == 3) && zCoord != pipe2.zCoord && yCoord == pipe2.yCoord
				&& xCoord == pipe2.xCoord)
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.transport.PipeLogic#inputOpen(net.minecraft
	 * .src.buildcraft.api.Orientations)
	 */
	@Override
	public boolean inputOpen(Orientations from) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (isPowered())
			return false;

		int nextMetadataS = (metadata + ((metadata%2 == 0) ? 0 : -1)) % 6;
		int nextMetadataU = (metadata + ((metadata%2 == 0) ? 1 : 0)) % 6;

		Position pos = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataS]);
		pos.moveForwards(1.0);
		TileEntity tile = worldObj.getBlockTileEntity((int)pos.x, (int)pos.y, (int)pos.z);

		Position pos2 = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataU]);
		pos2.moveForwards(1.0);
		TileEntity tile2 = worldObj.getBlockTileEntity((int)pos2.x, (int)pos2.y, (int)pos2.z);
		
		boolean isPipe = (tile instanceof IPipeEntry || tile instanceof IInventory || tile instanceof ILiquidContainer
				|| tile instanceof TileGenericPipe);

		boolean isPipe2 = (tile2 instanceof IPipeEntry || tile2 instanceof IInventory || tile2 instanceof ILiquidContainer
				|| tile2 instanceof TileGenericPipe);

		System.out.println("input  1:" + isPipe + " 2:" + isPipe2 + " m:" + metadata + " f:" + from.ordinal());
		
		switch (from.ordinal()) {
		case 0:
		case 1:
			return (isPipe || isPipe2) && (metadata == 0 || metadata == 1);
		case 2:
		case 3:
			return (isPipe || isPipe2) && (metadata == 2 || metadata == 3);
		case 4:
		case 5:
			return (isPipe || isPipe2) && (metadata == 4 || metadata == 5);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.transport.PipeLogic#outputOpen(net.minecraft
	 * .src.buildcraft.api.Orientations)
	 */
	@Override
	public boolean outputOpen(Orientations to) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (!isPowered())
			return false;

		int nextMetadataS = (metadata + ((metadata%2 == 0) ? 0 : -1)) % 6;
		int nextMetadataU = (metadata + ((metadata%2 == 0) ? 1 : 0)) % 6;

		Position pos = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataS]);
		pos.moveForwards(1.0);
		TileEntity tile = worldObj.getBlockTileEntity((int)pos.x, (int)pos.y, (int)pos.z);

		Position pos2 = new Position(xCoord, yCoord, zCoord, Orientations.values()[nextMetadataU]);
		pos2.moveForwards(1.0);
		TileEntity tile2 = worldObj.getBlockTileEntity((int)pos2.x, (int)pos2.y, (int)pos2.z);
		
		boolean isPipe = (tile instanceof IPipeEntry || tile instanceof IInventory || tile instanceof ILiquidContainer
				|| tile instanceof TileGenericPipe);

		boolean isPipe2 = (tile2 instanceof IPipeEntry || tile2 instanceof IInventory || tile2 instanceof ILiquidContainer
				|| tile2 instanceof TileGenericPipe);
		
		//System.out.println("output 1:" + isPipe + " 2:" + isPipe2 + " m:" + metadata + " t:" + to.ordinal());

		switch (to.ordinal()) {
		case 0:
		case 1:
			return (isPipe || isPipe2) && (metadata == 0 || metadata == 1);
		case 2:
		case 3:
			return (isPipe || isPipe2) && (metadata == 2 || metadata == 3);
		case 4:
		case 5:
			return (isPipe || isPipe2) && (metadata == 4 || metadata == 5);
		}

		return false;
	}

	/**
	 * @return
	 */
	public boolean isPowered() {
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
}
