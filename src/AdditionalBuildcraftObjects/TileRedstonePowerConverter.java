package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.buildcraft.api.IPowerReceptor;
import net.minecraft.src.buildcraft.api.PowerFramework;
import net.minecraft.src.buildcraft.api.PowerProvider;
import net.minecraft.src.buildcraft.api.SafeTimeTracker;
import net.minecraft.src.buildcraft.core.IMachine;
import net.minecraft.src.buildcraft.factory.TileMachine;

public class TileRedstonePowerConverter extends TileMachine implements IMachine, IPowerReceptor {

	public SafeTimeTracker timeTracker = new SafeTimeTracker();
	public SafeTimeTracker resetTracker = new SafeTimeTracker();
	
	private PowerProvider powerProvider;
	private boolean isPowered = false;

	public TileRedstonePowerConverter() {
		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		powerProvider.configure(20, 1, 5, 1, 5);
	}

	@Override
	public void setPowerProvider(PowerProvider provider) {
		powerProvider = provider;
	}

	@Override
	public PowerProvider getPowerProvider() {
		return powerProvider;
	}

	/**
	 * update notify neighbors (but only not connected ones!)
	 */
	private void notifyNeighbors() {
		int blockID = worldObj.getBlockId(xCoord, yCoord, zCoord);
		//worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, isPowered ? 1 : 0);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockID);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, blockID);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, blockID);
		worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(resetTracker.markTimeIfDelay(worldObj, 17) && powerProvider.energyStored < 1) {
			isPowered = false;
			notifyNeighbors();
		}
	}

	@Override
	public void doWork() {
		if(!timeTracker.markTimeIfDelay(worldObj, 7))
			return;

		if(powerProvider.useEnergy(1, 1, true) == 1) {
			isPowered = true;
			resetTracker.markTime(worldObj);
			notifyNeighbors();
		}

		//System.out.println("p: " + isPowered);
	}
	
	public boolean isPowered() {
		return isPowered;
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		isPowered = nbttagcompound.getBoolean("isPowered");

		PowerFramework.currentFramework.loadPowerProvider(this, nbttagcompound);
		//powerProvider.configure(20, 1, 10, 1, 10);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
		nbttagcompound.setBoolean("isPowered", isPowered);

		PowerFramework.currentFramework.savePowerProvider(this, nbttagcompound);
	}

	@Override
	public boolean manageLiquids() {
		return false;
	}

	@Override
	public boolean manageSolids() {
		return false;
	}
}
