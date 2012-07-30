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

import java.util.LinkedList;

import net.minecraft.src.buildcraft.api.EntityPassiveItem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;

/**
 * @author Flow86
 * 
 */
public class PipeItemsRoundRobin extends Pipe implements IPipeTransportItemsHook {

	public PipeItemsRoundRobin(int itemID) {
		super(new PipeTransportItemsRoundRobin(), new PipeLogicStone(), itemID);
	}

	@Override
	public int getMainBlockTexture() {
		return ABO.customTextures[4];
	}

	/*
	 * its the same as in PipeItemsStone, was not able to use it directly
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook#readjustSpeed
	 * (net.minecraft.src.buildcraft.api.EntityPassiveItem)
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
