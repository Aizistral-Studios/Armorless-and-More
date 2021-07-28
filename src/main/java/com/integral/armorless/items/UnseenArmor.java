package com.integral.armorless.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.integral.armorless.ArmorlessMod;
import com.integral.armorless.materials.ArmorlessMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class UnseenArmor extends ArmorItem {

	public UnseenArmor(EquipmentSlot slot) {
		super(ArmorlessMaterials.UNSEEN, slot,
				new Item.Properties()
				.durability(0)
				.stacksTo(1)
				.tab(CreativeModeTab.TAB_COMBAT)
				.setNoRepair()
				.rarity(Rarity.UNCOMMON));

		this.setRegistryName(new ResourceLocation(ArmorlessMod.MODID, "unseen_armor_"+ slot.getName()));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return super.getArmorTexture(stack, entity, slot, type);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return HashMultimap.create();
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

}
