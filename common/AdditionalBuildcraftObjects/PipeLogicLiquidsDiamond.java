package AdditionalBuildcraftObjects;

import net.minecraft.src.ItemStack;
import buildcraft.api.API;
import buildcraft.api.Orientations;
import buildcraft.transport.PipeLogicDiamond;
import buildcraft.transport.PipeTransportLiquids;

public class PipeLogicLiquidsDiamond extends PipeLogicDiamond {

	@Override
	public boolean outputOpen(Orientations to) {
		PipeTransportLiquids transport = (PipeTransportLiquids)this.container.pipe.transport;

		if(transport.getLiquidId() == 0)
			return true;
	
		for (int slot = 0; slot < 9; ++slot) {
			ItemStack stack = getStackInSlot(to.ordinal() * 9 + slot);

			if(stack != null && API.getLiquidForBucket(stack.itemID) != 0 && transport.getLiquidId() == API.getLiquidForBucket(stack.itemID))
				return true;
		}

		return false;
	}
}
