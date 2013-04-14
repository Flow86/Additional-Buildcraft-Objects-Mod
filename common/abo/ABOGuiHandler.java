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

package abo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import abo.pipes.liquids.PipeLogicLiquidDiamond;
import abo.pipes.power.gui.ContainerPipeDiamondConductive;
import abo.pipes.power.gui.GuiPipeDiamondConductive;
import buildcraft.transport.TileGenericPipe;
import cpw.mods.fml.common.network.IGuiHandler;

public class ABOGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileGenericPipe))
			return null;

		TileGenericPipe pipe = (TileGenericPipe) tile;

		if (pipe.pipe == null)
			return null;

		switch (ID) {
		case ABOGuiIds.PIPE_DIAMOND_LIQUIDS:
			return new ContainerLiquidDiamondPipe(player.inventory, (PipeLogicLiquidDiamond) pipe.pipe.logic);

		case ABOGuiIds.PIPE_DIAMOND_CONDUCTIVE:
			return new ContainerPipeDiamondConductive(player.inventory, pipe);

		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileGenericPipe))
			return null;

		TileGenericPipe pipe = (TileGenericPipe) tile;

		if (pipe.pipe == null)
			return null;

		switch (ID) {
		case ABOGuiIds.PIPE_DIAMOND_LIQUIDS:
			return new GuiLiquidDiamondPipe(player.inventory, pipe);

		case ABOGuiIds.PIPE_DIAMOND_CONDUCTIVE:
			return new GuiPipeDiamondConductive(player.inventory, pipe);

		default:
			return null;
		}
	}
}
