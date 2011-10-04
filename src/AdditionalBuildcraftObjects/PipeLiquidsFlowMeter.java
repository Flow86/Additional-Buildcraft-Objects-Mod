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

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.PowerProvider;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicGold;
import net.minecraft.src.buildcraft.transport.PipeTransportLiquids;

/**
 * TODO: use gui to change hysteresis, invert, etc
 * 
 * @author Flow86
 * 
 */
public class PipeLiquidsFlowMeter extends Pipe implements IABOSolid, IABOPower {

	PowerProvider powerProvider;

	private final int closedTexture = 4 * 16 + 0;
	private final int unknownTexture = 4 * 16 + 1;
	private final int openTexture = 4 * 16 + 2;
	private int nextTexture = closedTexture;

	private boolean lastPower = false;
	private boolean currentPower = false;
	private boolean unknownPower = false;

	private final double offAmountInPercent = 15.0;
	private final double onAmountInPercent = 60.0;
	private final int offAmount;
	private final int onAmount;

	public PipeLiquidsFlowMeter(int itemID) {
		super(new PipeTransportLiquids(2, 80), new PipeLogicGold(), itemID);

		offAmount = (int) ((offAmountInPercent * getLiquidRealCapacity()) / 100.0);
		onAmount = (int) ((onAmountInPercent * getLiquidRealCapacity()) / 100.0);
	}

	@Override
	public void initialize() {
		super.initialize();

		updatePower();
	}

	@Override
	public void prepareTextureFor(Orientations connection) {
		int amount = getLiquidRealAmount();
		if(unknownPower)
			nextTexture = unknownTexture;
		else
			nextTexture = (currentPower ? openTexture : closedTexture);
	}

	/**
	 * getCapacity does not work correctly
	 * 
	 * @return
	 */
	private int getLiquidRealCapacity() {
		return 600;
	}

	/**
	 * getLiquidQuantity does not work correctly
	 * 
	 * @return
	 */
	private int getLiquidRealAmount() {
		int total = ((PipeTransportLiquids) transport).getCenter();

		for (int i = 0; i < 6; ++i)
			total += ((PipeTransportLiquids) transport).getSide(i);

		return total;
	}
	
	private void notifyNeighbor(int x, int y, int z) {
		int blockID = worldObj.getBlockId(x, y, z);
		if(Block.blocksList[blockID] != null)
			Block.blocksList[blockID].onNeighborBlockChange(worldObj, x, y, z, worldObj.getBlockId(xCoord, yCoord, zCoord));
	}

	/**
	 * update notify neighbors (but only not connected ones!)
	 */
	private void notifyNeighbors() {
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);

		TileEntity tilePX = worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);
		TileEntity tileNX = worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);
		TileEntity tilePY = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
		TileEntity tileNY = worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord);
		TileEntity tilePZ = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);
		TileEntity tileNZ = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);

		int blockID = worldObj.getBlockId(xCoord, yCoord, zCoord);
		if (tilePX == null || !isPipeConnected(tilePX))
			notifyNeighbor(xCoord + 1, yCoord, zCoord);
		if (tileNX == null || !isPipeConnected(tileNX))
			notifyNeighbor(xCoord - 1, yCoord, zCoord);

		if (tilePY == null || !isPipeConnected(tilePY))
			notifyNeighbor(xCoord, yCoord + 1, zCoord);
		if (tileNY == null || !isPipeConnected(tileNY))
			notifyNeighbor(xCoord, yCoord - 1, zCoord);

		if (tilePZ == null || !isPipeConnected(tilePZ))
			notifyNeighbor(xCoord, yCoord, zCoord + 1);
		if (tileNZ == null || !isPipeConnected(tileNZ))
			notifyNeighbor(xCoord, yCoord, zCoord - 1);
	}

	/**
	 * updates power state
	 */
	private void updatePower() {
		int amount = getLiquidRealAmount();

		if (lastPower && amount <= offAmount) {
			currentPower = false;
			lastPower = false;
			unknownPower = false;
			notifyNeighbors();
		} 
		else if (!lastPower && amount >= onAmount) {
			currentPower = true;
			lastPower = true;
			unknownPower = false;
			notifyNeighbors();
		}
		else {
			unknownPower = (currentPower == lastPower && amount > offAmount && amount < onAmount);
			notifyNeighbors();
		}
		
		//System.out.println("currentPower: " + currentPower + " amount: " + amount);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		updatePower();
	}

	@Override
	public int getBlockTexture() {
		return nextTexture;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		// TODO: only allow specific sides
		return true;
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int side) {
		return currentPower;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int side) {
		return currentPower;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setBoolean("lastPower", lastPower);
		nbttagcompound.setBoolean("currentPower", currentPower);
		nbttagcompound.setBoolean("unknownPower", unknownPower);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		lastPower = nbttagcompound.getBoolean("lastPower");
		currentPower = nbttagcompound.getBoolean("currentPower");
		unknownPower = nbttagcompound.getBoolean("unknownPower");
	}
}
