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

import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_AdditionalBuildcraftObjects;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.core.Trigger;
import net.minecraft.src.buildcraft.energy.TileEngine;
import net.minecraft.src.buildcraft.transport.pipes.PipePowerWood;

public class PipePowerEngineControl extends PipePowerWood {

	public PipePowerEngineControl(int itemID) {
		super(itemID);
	}

	@Override
	public LinkedList<Trigger> getTriggers() {
		LinkedList<Trigger> triggers = new LinkedList<Trigger>();

		triggers.addAll(super.getTriggers());

		for (Orientations o : Orientations.dirs()) {
			TileEntity entity = container.getTile(o);

			if (entity instanceof TileEngine && !triggers.contains(mod_AdditionalBuildcraftObjects.triggerEngineControl))
				triggers.add(mod_AdditionalBuildcraftObjects.triggerEngineControl);
		}
		
		return triggers;
	}

	@Override
	public int getMainBlockTexture() {
		return 12 * 16 + 0;
	}

}
