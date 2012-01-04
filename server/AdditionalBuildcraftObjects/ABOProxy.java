package net.minecraft.src.AdditionalBuildcraftObjects;


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
