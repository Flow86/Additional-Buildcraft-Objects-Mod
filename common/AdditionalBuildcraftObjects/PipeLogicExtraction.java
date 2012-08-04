/** 
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package AdditionalBuildcraftObjects;

import buildcraft.BuildCraftTransport;
import net.minecraft.src.TileEntity;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.TileGenericPipe;

/**
 * This pipe will always prefer to insert it's objects into another pipe over
 * one that is not a pipe.
 * 
 * @author Scott Chamberlain (Leftler)
 *         ported to BC > 2.2 by Flow86
 */
public class PipeLogicExtraction extends PipeLogicWood {
	@Override
	public boolean isPipeConnected(TileEntity tile) {
		Pipe pipe2 = null;

		if (tile instanceof TileGenericPipe) {
			pipe2 = ((TileGenericPipe) tile).pipe;
		}

		if (BuildCraftTransport.alwaysConnectPipes) {
			return super.isPipeConnected(tile);
		} else {
			return (pipe2 == null || (pipe2.logic instanceof PipeLogicExtraction || !(pipe2.logic instanceof PipeLogicWood))); // && super.isPipeConnected(tile);
		}
	}
}
