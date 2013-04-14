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

package abo.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import abo.ABO;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ABOPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet2, Player player) {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet2.data));
		try {
			int packetID = data.read();

			switch (packetID) {
			case ABOPacketIds.YesNoChange: {
				PacketYesNoChange yesNoPacket = new PacketYesNoChange(data);
				yesNoPacket.update((EntityPlayer) player);
				break;
			}
			case ABOPacketIds.LiquidSlotChange: {
				PacketLiquidSlotChange liquidSlotPacket = new PacketLiquidSlotChange(data);
				liquidSlotPacket.update((EntityPlayer) player);
				break;
			}
			default:
				ABO.aboLog.info("Packet: " + packetID);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
