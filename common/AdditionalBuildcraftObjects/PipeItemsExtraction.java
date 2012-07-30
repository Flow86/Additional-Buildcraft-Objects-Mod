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

import net.minecraft.src.ModLoader;
import net.minecraft.src.buildcraft.api.IPowerReceptor;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsWood;

/**
 * This pipe will always prefer to insert it's objects into another pipe over
 * one that is not a pipe.
 * 
 * @author Scott Chamberlain (Leftler)
 *         ported to BC > 2.2 by Flow86
 */
public class PipeItemsExtraction extends PipeItemsWood implements IPowerReceptor {
	private final int baseTexture =  ABO.customTextures[17];
	private final int sideTexture =  ABO.customTextures[18];
	private int nextTexture = baseTexture;
	
	public PipeItemsExtraction(int itemID) {
		super(itemID);
		
		((PipeTransportItems) transport).allowBouncing = true;
		
		// THIS IS DAMN UGLY - but I have no other chance to change logic and transport afterwards :/
		try {
			ModLoader.setPrivateValue(Pipe.class, this, "logic", new PipeLogicExtraction());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
		}

		try {
			ModLoader.setPrivateValue(Pipe.class, this, "transport", new PipeTransportItemsExtraction());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
		}
	}

	@Override
	public void prepareTextureFor(Orientations connection) {
		if (connection == Orientations.Unknown) {
			nextTexture = baseTexture;
		} else {
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			if (metadata == connection.ordinal()) {
				nextTexture = sideTexture;
			} else {
				nextTexture = baseTexture;
			}
		}
	}
	
	@Override
	public int getMainBlockTexture() {
		return nextTexture;
	}
}
