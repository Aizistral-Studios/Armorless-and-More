package com.integral.armorless.materials;

import net.minecraft.item.Items;
import com.integral.armorless.ArmorlessMod;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import java.util.function.Supplier;

public enum ArmorlessMaterials implements IArmorMaterial {
    UNSEEN(ArmorlessMod.MODID + ":unseen", 0, new int[] { 0, 0, 0, 0 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F, () -> {
    	return Ingredient.fromItems(Items.IRON_INGOT);
    });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyValue<Ingredient> repairMaterial;

    ArmorlessMaterials(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getDurability(EquipmentSlotType slot) {
    	int durability = ArmorlessMaterials.MAX_DAMAGE_ARRAY[slot.getIndex()] * this.maxDamageFactor;
        return durability;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot) {
        return this.damageReductionAmountArray[slot.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

	@Override
	public float getKnockbackResistance() {
		return 0;
	}
}
