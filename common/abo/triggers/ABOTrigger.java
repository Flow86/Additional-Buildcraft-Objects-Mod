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

package abo.triggers;

import abo.ABO;
import buildcraft.api.gates.Trigger;

public abstract class ABOTrigger extends Trigger {

	public ABOTrigger(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return ABO.textureTriggers;
	}

}
