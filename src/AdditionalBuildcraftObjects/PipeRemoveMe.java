package net.minecraft.src.AdditionalBuildcraftObjects;

import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicGold;
import net.minecraft.src.buildcraft.transport.PipeTransportLiquids;

public class PipeRemoveMe extends Pipe {

	public PipeRemoveMe(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicGold(), itemID);
	}

	@Override
	public void initialize() {
		// remove me :)
		worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord, zCoord, 0, 0);
	}
}
