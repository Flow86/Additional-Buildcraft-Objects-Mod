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

package abo.proxy;

import net.minecraft.network.packet.Packet;
import net.minecraftforge.client.MinecraftForgeClient;
import buildcraft.transport.TransportProxyClient;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * @author Flow86
 * 
 */
public class ABOProxyClient extends ABOProxy {
	@Override
	public void registerPipe(int itemID) {
		super.registerPipe(itemID);

		MinecraftForgeClient.registerItemRenderer(itemID, TransportProxyClient.pipeItemRenderer);
	}

	@Override
	public void sendToServer(Packet packet) {
		FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(packet);
	}
}
