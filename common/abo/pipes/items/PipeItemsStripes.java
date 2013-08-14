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
import abo.PipeIconProvider;
import abo.pipes.ABOPipe;
import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.BlockUtil;
import buildcraft.transport.IItemTravelingHook;
import buildcraft.transport.PipeConnectionBans;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TravelingItem;
import buildcraft.transport.pipes.PipeItemsObsidian;

public class PipeItemsStripes extends ABOPipe<PipeTransportItems> implements IItemTravelingHook, IPowerReceptor {

	private static final float powerToBreakABlock = 50.0f;
	protected PowerHandler powerHandler;

	public PipeItemsStripes(int itemID) {
		super(new PipeTransportItems(), itemID);

		PipeConnectionBans.banConnection(PipeItemsObsidian.class, PipeItemsStripes.class);

		transport.travelHook = this;

		powerHandler = new PowerHandler(this, Type.MACHINE);
		powerHandler.configure(0.0f, powerToBreakABlock / 2.0f, powerToBreakABlock, powerToBreakABlock * 10);
		powerHandler.configurePowerPerdition(0, 0);
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		return PipeIconProvider.PipeItemsStripes;
	}

	@Override
	public void doWork(PowerHandler workProvider) {
		if (workProvider.useEnergy(powerToBreakABlock, powerToBreakABlock, true) == powerToBreakABlock) {
			ForgeDirection o = getOpenOrientation();

			if (o != ForgeDirection.UNKNOWN) {
				Position p = new Position(container.xCoord, container.yCoord, container.zCoord, o);
				p.moveForwards(1.0);

				List<ItemStack> stacks = BlockUtil.getItemStackFromBlock(container.worldObj, (int) p.x, (int) p.y, (int) p.z);

				if (stacks != null) {
					for (ItemStack s : stacks) {
						if (s != null) {
							this.container.injectItem(s, true, o.getOpposite());
						}
					}
				}

				container.worldObj.setBlockToAir((int) p.x, (int) p.y, (int) p.z);
			}
		}

	}

	@Override
	public void drop(PipeTransportItems pipe, TravelingItem item) {
		Position p = new Position(container.xCoord, container.yCoord, container.zCoord, item.output);
		p.moveForwards(1.0);

		/*
		 * if (convertPipe(pipe, data)) if(CoreProxy.proxy.isSimulating(worldObj)) BuildCraftTransport.pipeItemsStipes.onItemUseFirst(new ItemStack(BuildCraftTransport.pipeItemsStipes),
		 * CoreProxy.proxy.getBuildCraftPlayer(worldObj), worldObj, (int) p.x, (int) p.y - 1, (int) p.z, 1); else
		 */
		if (container.worldObj.isAirBlock((int) p.x, (int) p.y, (int) p.z))
			item.getItemStack().getItem()
					.onItemUse(item.getItemStack(), CoreProxy.proxy.getBuildCraftPlayer(container.worldObj), container.worldObj, (int) p.x, (int) p.y - 1, (int) p.z, 1, 0.0f, 0.0f, 0.0f);
		else
			item.getItemStack().getItem()
					.onItemUse(item.getItemStack(), CoreProxy.proxy.getBuildCraftPlayer(container.worldObj), container.worldObj, (int) p.x, (int) p.y, (int) p.z, 1, 0.0f, 0.0f, 0.0f);
	}

	@Override
	public void centerReached(PipeTransportItems pipe, TravelingItem item) {
		// convertPipe(pipe, data); removed because it is buggy - it does not
		// sync the pipe change
	}

	/*
	 * @SuppressWarnings("unchecked") public boolean convertPipe(PipeTransportItems pipe, EntityData data) {
	 * 
	 * if (data.item.getItemStack().getItem() instanceof ItemPipe) { if (!(data.item.getItemStack().itemID == BuildCraftTransport.pipeItemsStipes.shiftedIndex)) {
	 * 
	 * Pipe newPipe = BlockGenericPipe.createPipe(data.item.getItemStack().itemID); if(newPipe.transport instanceof PipeTransportItems) { newPipe.setTile(this.container); this.container.pipe =
	 * newPipe; ((PipeTransportItems) newPipe.transport).travelingEntities = (TreeMap<Integer, EntityData>) pipe.travelingEntities .clone();
	 * 
	 * 
	 * 
	 * int blockID = BuildCraftTransport.genericPipeBlock.blockID; if (BlockGenericPipe.placePipe(newPipe, worldObj, xCoord, yCoord, zCoord, blockID, 0)) {
	 * 
	 * //Block.blocksList[blockID].onBlockPlacedBy(worldObj, xCoord, yCoord, zCoord, entityplayer);
	 * 
	 * data.item.getItemStack().stackSize--; } if (data.item.getItemStack().stackSize <= 0){ ((PipeTransportItems) newPipe.transport).travelingEntities.remove(data.item.getEntityId());
	 * 
	 * pipe.scheduleRemoval(data.item); }
	 * 
	 * 
	 * return true; } } } return false; }
	 */

	@Override
	public boolean endReached(PipeTransportItems pipe, TravelingItem data, TileEntity tile) {
		return false;
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return powerHandler.getPowerReceiver();
	}
}
