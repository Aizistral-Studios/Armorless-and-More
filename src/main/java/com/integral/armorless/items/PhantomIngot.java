package com.integral.armorless.items;

import com.integral.armorless.ArmorlessMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class PhantomIngot extends Item {

	public PhantomIngot() {
		super(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON).maxStackSize(64));

		this.setRegistryName(new ResourceLocation(ArmorlessMod.MODID, "phantom_ingot"));
	}

}
