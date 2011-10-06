/** 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of the my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.AdditionalBuildcraftObjects;

import java.util.ArrayList;

import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.api.IPowerReceptor;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.api.PowerProvider;
import net.minecraft.src.buildcraft.core.ILiquidContainer;
import net.minecraft.src.buildcraft.core.RedstonePowerProvider;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.factory.TileTank;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;
import net.minecraft.src.buildcraft.transport.PipeTransportLiquids;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

public class PipeLiquidsBalance extends Pipe implements IPowerReceptor {
	PowerProvider powerProvider;

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

	private final int blockTexture = 5 * 16 + 0;

	public PipeLiquidsBalance(int itemID) {
		super(new PipeTransportLiquids(2, 80), new PipeLogicStone(), itemID);

		powerProvider = new RedstonePowerProvider();
		powerProvider.configure(0, 1, 1, 1, 1);
		powerProvider.configurePowerPerdition(1, 1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
			powerProvider.receiveEnergy(1);
		}
	}

	@Override
	public boolean isPipeConnected(TileEntity tile) {
		if(tile == null || !(tile instanceof ILiquidContainer) || tile instanceof TileGenericPipe)
			return false;
		
		return super.isPipeConnected(tile);
	}

	/**
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 * @return
	 */
	private Neighbor getLiquidContainerOfNeighbor(int xCoord, int yCoord, int zCoord) {
		TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
		if (tile == null || !(tile instanceof ILiquidContainer))
			return null;

		return new Neighbor((ILiquidContainer) tile, xCoord, yCoord, zCoord);
	}

	@Override
	public int getBlockTexture() {
		return blockTexture;
	}

	@Override
	public void setPowerProvider(PowerProvider provider) {
		// powerProvider = provider;
	}

	@Override
	public PowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() {
		if (powerProvider.useEnergy(1, 1, false) < 1)
			return;

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

					//System.out.println("liquidToExtract: " + liquidToExtract);

					Orientations pos = Utils.get2dOrientation(new Position(xCoord, yCoord, zCoord), new Position(
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
						extracted = n.c.fill(pos, extracted, liquidID, true);
						ltransport.empty(extracted, true);
					}
				}
			}
		}
	}

	@Override
	public int powerRequest() {
		return getPowerProvider().maxEnergyReceived;
	}
}
