/** 
 * Copyright (C) 2011 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import java.util.ArrayList;

import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.api.ILiquidContainer;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.factory.TileTank;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;
import net.minecraft.src.buildcraft.transport.PipeTransportLiquids;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

/**
 * @author Flow86
 *
 */
public class PipeLiquidsBalance extends Pipe {
	class Neighbor {
		public Neighbor(ILiquidContainer container, int xCoord, int yCoord, int zCoord) {
			c = container;
			x = xCoord;
			y = yCoord;
			z = zCoord;
			q = 0;
		}

		ILiquidContainer c;
		int x;
		int y;
		int z;
		int q;
	}

	private final int blockTexture = ABO.customTextures[12];

	public PipeLiquidsBalance(int itemID) {
		super(new PipeTransportLiquids(), new PipeLogicStone(), itemID);

		((PipeTransportLiquids)transport).flowRate = 100;
		((PipeTransportLiquids)transport).travelDelay = 1;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		doWork();
	}

	@Override
	public boolean isPipeConnected(TileEntity tile) {
		if(tile == null || !(tile instanceof ILiquidContainer) || tile instanceof TileGenericPipe)
			return false;
		
		return super.isPipeConnected(tile);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private Neighbor getLiquidContainerOfNeighbor(int x, int y, int z) {
		TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
		if (tile == null || !(tile instanceof ILiquidContainer))
			return null;

		return new Neighbor((ILiquidContainer) tile, x, y, z);
	}

	@Override
	public int getMainBlockTexture() {
		return blockTexture;
	}

	public void doWork() {
		ArrayList<Neighbor> neighbors = new ArrayList<Neighbor>();

		neighbors.add(getLiquidContainerOfNeighbor(xCoord + 1, yCoord, zCoord));
		neighbors.add(getLiquidContainerOfNeighbor(xCoord - 1, yCoord, zCoord));
		neighbors.add(getLiquidContainerOfNeighbor(xCoord, yCoord + 1, zCoord));
		neighbors.add(getLiquidContainerOfNeighbor(xCoord, yCoord - 1, zCoord));
		neighbors.add(getLiquidContainerOfNeighbor(xCoord, yCoord, zCoord + 1));
		neighbors.add(getLiquidContainerOfNeighbor(xCoord, yCoord, zCoord - 1));

		PipeTransportLiquids ltransport = (PipeTransportLiquids) transport;

		int liquidID = ltransport.getLiquidId();

		int avg = 0;
		int ns = 0;
		for (Neighbor n : neighbors) {
			if (n != null) {
				if (liquidID == 0 && n.c.getLiquidId() != 0) {
					ltransport.center.fill(0, true, (short) n.c.getLiquidId());
					liquidID = ltransport.getLiquidId();
				}

				if (liquidID == n.c.getLiquidId() || n.c.getLiquidId() == 0) {
					n.q += n.c.getLiquidQuantity();

					if(n.c instanceof TileTank) {
						for (int j = n.y - 1; j > 1; --j) {
							if (worldObj.getBlockTileEntity(n.x, j, n.z) instanceof TileTank) {
								n.q += ((TileTank)worldObj.getBlockTileEntity(n.x, j, n.z)).getLiquidQuantity();
							} else
								break;
						}
						for (int j = n.y + 1; j < 128; ++j) {
							if (worldObj.getBlockTileEntity(n.x, j, n.z) instanceof TileTank) {
								n.q += ((TileTank)worldObj.getBlockTileEntity(n.x, j, n.z)).getLiquidQuantity();
							} else
								break;
						}

					}
					
					avg += n.q;
					++ns;
				}
			}
		}

		if (ns > 0) {
			avg /= ns;
			
			// randomly suck liquid ;-)
			//java.util.Collections.shuffle(neighbors);

			//System.out.println("avg: " + avg);
			
			for (Neighbor n : neighbors) {
				if (n != null && liquidID == n.c.getLiquidId()) {
					int liquidToExtract = n.q - avg;

					if(liquidToExtract == 0)
						continue;

					//System.out.println("liquidToExtract: " + liquidToExtract);

					Orientations pos = Utils.get3dOrientation(new Position(xCoord, yCoord, zCoord), new Position(
							n.x, n.y, n.z));
					
					if (liquidToExtract > 0) {
						int extracted = n.c.empty(liquidToExtract > ltransport.flowRate ? ltransport.flowRate
								: liquidToExtract, false);
						extracted = ltransport.fill(pos, extracted, liquidID, true);
						n.c.empty(extracted, true);
					} else {
						liquidToExtract = -liquidToExtract;
						int extracted = ltransport.empty(liquidToExtract > ltransport.flowRate ? ltransport.flowRate
								: liquidToExtract, false);
						extracted = n.c.fill(pos.reverse(), extracted, liquidID, true);
						ltransport.empty(extracted, true);
					}
				}
			}
		}
	}
}
