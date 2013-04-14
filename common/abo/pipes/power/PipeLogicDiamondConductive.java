/** 
 * Copyright (C) 2011-2013 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.pipes.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import abo.pipes.power.gui.IYesNoChange;
import buildcraft.transport.pipes.PipeLogic;

public class PipeLogicDiamondConductive extends PipeLogic implements IYesNoChange {
	public final boolean[] connectionMatrix = { true, true, true, true, true, true };

	public boolean hasConnectionToSide(ForgeDirection side) {
		if (side == ForgeDirection.UNKNOWN)
			return false;

		return connectionMatrix[side.ordinal()];
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		for (int i = 0; i < 6; ++i)
			connectionMatrix[i] = nbttagcompound.getBoolean("connectionMatrix[" + i + "]");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		for (int i = 0; i < 6; ++i)
			nbttagcompound.setBoolean("connectionMatrix[" + i + "]", connectionMatrix[i]);
	}

	@Override
	public void update(int slot, boolean state) {
		// System.out.println("update: " + slot + " from " + connectionMatrix[slot] + " to " + state);

		if (connectionMatrix[slot] != state) {
			connectionMatrix[slot] = state;
			((PipeDiamondConductive) container.pipe).isDirty = true;
			container.pipe.updateEntity();
		}
	}
}
