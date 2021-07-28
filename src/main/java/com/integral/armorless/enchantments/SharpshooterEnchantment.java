package com.integral.armorless.enchantments;

import com.integral.armorless.ArmorlessMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class SharpshooterEnchantment extends Enchantment {
	public SharpshooterEnchantment(final EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.CROSSBOW, slots);
		this.setRegistryName(new ResourceLocation(ArmorlessMod.MODID, "sharpshooter"));
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	protected boolean checkCompatibility(final Enchantment ench) {
		return ench != Enchantments.MULTISHOT && ench != Enchantments.PIERCING;
	}

	@Override
	public boolean canApplyAtEnchantingTable(final ItemStack stack) {
		return stack.getItem() instanceof CrossbowItem;
	}

	@Override
	public boolean isTreasureOnly() {
		return false;
	}

	@Override
	public boolean isCurse() {
		return false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
}
