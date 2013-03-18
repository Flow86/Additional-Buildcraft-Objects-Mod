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

package abo.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.core.utils.StringUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Flow86
 * 
 */
public class ABOItem extends Item {

	private String iconName;

	public ABOItem(int itemID) {
		super(itemID);
		setCreativeTab(CreativeTabBuildCraft.tabBuildCraft);
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return StringUtil.localize(getUnlocalizedName(itemstack));
	}

	@Override
	public Item setUnlocalizedName(String name) {
		iconName = name;
		return super.setUnlocalizedName(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister) {
		this.iconIndex = iconRegister.func_94245_a("abo:" + iconName);
	}
}
