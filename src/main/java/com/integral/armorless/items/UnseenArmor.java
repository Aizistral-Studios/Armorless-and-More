package com.integral.armorless.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.integral.armorless.ArmorlessMod;
import com.integral.armorless.materials.ArmorlessMaterials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;;

public class UnseenArmor extends ArmorItem {

	public UnseenArmor(EquipmentSlotType slot) {
		super(ArmorlessMaterials.UNSEEN, slot,
				new Item.Properties()
				.durability(0)
				.stacksTo(1)
				.tab(ItemGroup.TAB_COMBAT)
				.setNoRepair()
				.rarity(Rarity.UNCOMMON));

		this.setRegistryName(new ResourceLocation(ArmorlessMod.MODID, "unseen_armor_"+ slot.getName()));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return super.getArmorTexture(stack, entity, slot, type);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		return HashMultimap.create();
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

}
