package AdditionalBuildcraftObjects;

import net.minecraft.src.ItemStack;
import buildcraft.api.BuildCraftAPI;
import buildcraft.api.Orientations;
import buildcraft.transport.PipeLogicDiamond;
import buildcraft.transport.PipeTransportLiquids;

/**
 * @author Flow86
 *
 */
public class PipeLogicLiquidsDiamond extends PipeLogicDiamond {

	@Override
	public boolean outputOpen(Orientations to) {
		PipeTransportLiquids transport = (PipeTransportLiquids)this.container.pipe.transport;

		if(transport.getLiquidId() == 0)
			return true;
	
		for (int slot = 0; slot < 9; ++slot) {
			ItemStack stack = getStackInSlot(to.ordinal() * 9 + slot);

			if(stack != null && BuildCraftAPI.getLiquidForFilledItem(stack) != 0 && transport.getLiquidId() == BuildCraftAPI.getLiquidForFilledItem(stack))
				return true;
		}

		return false;
	}
}
