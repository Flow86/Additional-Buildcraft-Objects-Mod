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

import net.minecraftforge.client.MinecraftForgeClient;
import buildcraft.transport.TransportProxyClient;

public class ABOProxyClient extends ABOProxy {

	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
	}

	@Override
	public void registerRenderers() {
		super.registerRenderers();

		MinecraftForgeClient.registerItemRenderer(ABO.pipeItemsRoundRobin.shiftedIndex, TransportProxyClient.pipeItemRenderer);
		MinecraftForgeClient.registerItemRenderer(ABO.pipeItemsCrossover.shiftedIndex, TransportProxyClient.pipeItemRenderer);
	}
}
