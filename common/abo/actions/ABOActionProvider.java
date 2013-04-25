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

package abo.actions;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import abo.ABO;
import abo.pipes.power.PipePowerSwitch;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionProvider;
import buildcraft.transport.TileGenericPipe;

/**
 * @author Flow86
 * 
 */
public class ABOActionProvider implements IActionProvider {
	@Override
	public LinkedList<IAction> getNeighborActions(Block block, TileEntity tile) {
		LinkedList<IAction> result = new LinkedList<IAction>();

		if (tile instanceof TileGenericPipe) {
			TileGenericPipe pipe = (TileGenericPipe) tile;
			if (pipe.pipe instanceof PipePowerSwitch) {
				result.add(ABO.actionToggleOnPipe);
				result.add(ABO.actionToggleOffPipe);
			}
		}

		return result;
	}
}
