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

package AdditionalBuildcraftObjects;

public class ABOProxy {
	private static ABOProxy instance = null;
	
	private ABOProxy() {
	}
	
	public static ABOProxy instance()
	{
		if(instance == null)
			instance = new ABOProxy();
		
		return instance;
	}

	public static void registerItemInRenderer (int itemID) {
	}
	
	public static void preloadTexture(String texture) {
	}
}
