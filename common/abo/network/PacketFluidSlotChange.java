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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import buildcraft.transport.TileGenericPipe;

public class PacketFluidSlotChange extends ABOPacket {

	private int slot;
	private FluidStack stack;

	public PacketFluidSlotChange(int xCoord, int yCoord, int zCoord, int slot, FluidStack stack) {
		super(ABOPacketIds.LiquidSlotChange, xCoord, yCoord, zCoord);
		this.slot = slot;
		this.stack = stack;
	}

	public PacketFluidSlotChange(DataInputStream data) throws IOException {
		readData(data);
	}

	@Override
	public void writeData(DataOutputStream data) throws IOException {
		super.writeData(data);

		data.writeInt(slot);

		if (stack == null)
			data.writeShort(0);
		else {
			NBTTagCompound nbt = new NBTTagCompound();
			stack.writeToNBT(nbt);

			byte[] compressed = CompressedStreamTools.compress(nbt);
			data.writeShort(compressed.length);
			data.write(compressed);
		}
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		super.readData(data);

		this.slot = data.readInt();

		short length = data.readShort();
		if (length == 0)
			stack = null;
		else {
			byte[] compressed = new byte[length];
			data.readFully(compressed);
			NBTTagCompound nbt = CompressedStreamTools.decompress(compressed);

			stack = FluidStack.loadFluidStackFromNBT(nbt);
		}
	}

	public void update(EntityPlayer player) {
		TileGenericPipe pipe = getPipe(player.worldObj, posX, posY, posZ);
		if (pipe == null || pipe.pipe == null)
			return;

		if (!(pipe.pipe instanceof IFluidSlotChange))
			return;

		((IFluidSlotChange) pipe.pipe).update(slot, stack);
	}
}
