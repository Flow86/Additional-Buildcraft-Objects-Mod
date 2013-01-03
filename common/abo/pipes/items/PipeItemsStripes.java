/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.pipes.items;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.transport.IPipedItem;
import buildcraft.core.EntityPassiveItem;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.BlockUtil;
import buildcraft.core.utils.Utils;
import buildcraft.transport.EntityData;
import buildcraft.transport.IItemTravelingHook;
import buildcraft.transport.PipeTransportItems;

public class PipeItemsStripes extends ABOPipe implements IItemTravelingHook, IPowerReceptor {

	private static final int powerToBreakABlock = 50;
	private IPowerProvider powerProvider;

	public PipeItemsStripes(int itemID) {
		super(new PipeTransportItems(), new PipeLogicStripes(), itemID);

		((PipeTransportItems) transport).travelHook = this;

		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		powerProvider.configure(25, 0, powerToBreakABlock / 2, powerToBreakABlock, powerToBreakABlock * 10);
		// powerProvider.configurePowerPerdition(1, 1);
	}

	@Override
	public int getTextureIndex(ForgeDirection direction) {
		return 14 * 16 + 0;
	}

	@Override
	public void doWork() {
		if (powerProvider.useEnergy(powerToBreakABlock, powerToBreakABlock, true) == powerToBreakABlock) {
			ForgeDirection o = getOpenOrientation();

			if (o != ForgeDirection.UNKNOWN) {
				Position p = new Position(xCoord, yCoord, zCoord, o);
				p.moveForwards(1.0);

				List<ItemStack> stacks = BlockUtil.getItemStackFromBlock(worldObj, (int) p.x, (int) p.y, (int) p.z);

				if (stacks != null)
					for (ItemStack s : stacks)
						if (s != null) {
							IPipedItem newItem = new EntityPassiveItem(worldObj, xCoord + 0.5, yCoord + Utils.getPipeFloorOf(s), zCoord + 0.5, s);

							this.container.entityEntering(newItem, o.getOpposite());
						}

				worldObj.setBlock((int) p.x, (int) p.y, (int) p.z, 0);
			}
		}

	}

	@Override
	public void drop(PipeTransportItems pipe, EntityData data) {
		Position p = new Position(xCoord, yCoord, zCoord, data.output);
		p.moveForwards(1.0);

		/*
		 * if (convertPipe(pipe, data))
		 * if(CoreProxy.proxy.isSimulating(worldObj))
		 * BuildCraftTransport.pipeItemsStipes.onItemUseFirst(new
		 * ItemStack(BuildCraftTransport.pipeItemsStipes),
		 * CoreProxy.proxy.getBuildCraftPlayer(worldObj), worldObj, (int) p.x,
		 * (int) p.y - 1, (int) p.z, 1); else
		 */
		if (worldObj.getBlockId((int) p.x, (int) p.y, (int) p.z) == 0)
			data.item
					.getItemStack()
					.getItem()
					.onItemUse(data.item.getItemStack(), CoreProxy.proxy.getBuildCraftPlayer(worldObj), worldObj, (int) p.x, (int) p.y - 1, (int) p.z, 1, 0.0f,
							0.0f, 0.0f);
		else
			data.item
					.getItemStack()
					.getItem()
					.onItemUse(data.item.getItemStack(), CoreProxy.proxy.getBuildCraftPlayer(worldObj), worldObj, (int) p.x, (int) p.y, (int) p.z, 1, 0.0f,
							0.0f, 0.0f);
	}

	@Override
	public void centerReached(PipeTransportItems pipe, EntityData data) {
		// convertPipe(pipe, data); removed because it is buggy - it does not
		// sync the pipe change
	}

	/*
	 * @SuppressWarnings("unchecked") public boolean
	 * convertPipe(PipeTransportItems pipe, EntityData data) {
	 * 
	 * if (data.item.getItemStack().getItem() instanceof ItemPipe) { if
	 * (!(data.item.getItemStack().itemID ==
	 * BuildCraftTransport.pipeItemsStipes.shiftedIndex)) {
	 * 
	 * Pipe newPipe =
	 * BlockGenericPipe.createPipe(data.item.getItemStack().itemID);
	 * if(newPipe.transport instanceof PipeTransportItems) {
	 * newPipe.setTile(this.container); this.container.pipe = newPipe;
	 * ((PipeTransportItems) newPipe.transport).travelingEntities =
	 * (TreeMap<Integer, EntityData>) pipe.travelingEntities .clone();
	 * 
	 * 
	 * 
	 * int blockID = BuildCraftTransport.genericPipeBlock.blockID; if
	 * (BlockGenericPipe.placePipe(newPipe, worldObj, xCoord, yCoord, zCoord,
	 * blockID, 0)) {
	 * 
	 * //Block.blocksList[blockID].onBlockPlacedBy(worldObj, xCoord, yCoord,
	 * zCoord, entityplayer);
	 * 
	 * data.item.getItemStack().stackSize--; } if
	 * (data.item.getItemStack().stackSize <= 0){ ((PipeTransportItems)
	 * newPipe.transport).travelingEntities.remove(data.item.getEntityId());
	 * 
	 * pipe.scheduleRemoval(data.item); }
	 * 
	 * 
	 * return true; } } } return false; }
	 */

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public int powerRequest() {
		return getPowerProvider().getMaxEnergyReceived();
	}

	@Override
	public void endReached(PipeTransportItems pipe, EntityData data, TileEntity tile) {

	}
}
