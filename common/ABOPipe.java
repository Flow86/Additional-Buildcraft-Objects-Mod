/** 
 * Copyright (C) 2012 Flow86
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

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransport;
import buildcraft.transport.pipes.PipeLogic;

public abstract class ABOPipe extends Pipe {
	public ABOPipe(PipeTransport transport, PipeLogic logic, int itemID) {
		super(transport, logic, itemID);
	}

	@Override
	public String getTextureFile() {
		return ABO.texturePipes;
	}
}
