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

import java.util.LinkedList;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
public class PipeItemsRoundRobin extends Pipe implements IPipeTransportItemsHook {

	public PipeItemsRoundRobin(int itemID) {
		super(new PipeTransportItemsRoundRobin(), new PipeLogicStone(), itemID);
	}

	@Override
	public int getBlockTexture() {
		return 1 * 16 + 0;
	}

	/*
	 * its the same as in PipeItemsStone, was not able to use it directly
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * buildcraft.transport.IPipeTransportItemsHook#readjustSpeed
	 * (buildcraft.api.EntityPassiveItem)
	 */
	@Override
	public void readjustSpeed(EntityPassiveItem item) {
		if (item.speed > Utils.pipeNormalSpeed) {
			item.speed = item.speed - Utils.pipeNormalSpeed / 2.0F;
		}

		if (item.speed < Utils.pipeNormalSpeed) {
			item.speed = Utils.pipeNormalSpeed;
		}
	}

	@Override
	public LinkedList<Orientations> filterPossibleMovements(LinkedList<Orientations> possibleOrientations,
			Position pos, EntityPassiveItem item) {
		return possibleOrientations;
	}

	@Override
	public void entityEntered(EntityPassiveItem item, Orientations orientation) {

	}
}
