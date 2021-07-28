package com.integral.armorless.items;

import com.integral.armorless.ArmorlessMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class PhantomIngot extends Item {

	public PhantomIngot() {
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).rarity(Rarity.UNCOMMON).stacksTo(64));

		this.setRegistryName(new ResourceLocation(ArmorlessMod.MODID, "phantom_ingot"));
	}

}
