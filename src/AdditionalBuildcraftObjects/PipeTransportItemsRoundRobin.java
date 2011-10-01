package net.minecraft.src.AdditionalBuildcraftObjects;

import java.util.LinkedList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;

public class PipeTransportItemsRoundRobin extends PipeTransportItems {
	private int lastOrientation = 0;

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		lastOrientation = nbttagcompound.getInteger("lastOrientation");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("lastOrientation", lastOrientation);
	}

	@Override
	public Orientations resolveDestination(EntityData data) {
		LinkedList<Orientations> listOfPossibleMovements = getPossibleMovements(new Position(xCoord, yCoord, zCoord,
				data.orientation), data.item);

		if (listOfPossibleMovements.size() == 0) {
			return Orientations.Unknown;
		} else {
			lastOrientation = (lastOrientation + 1) % listOfPossibleMovements.size();

			return listOfPossibleMovements.get(lastOrientation);
		}
	}
}
