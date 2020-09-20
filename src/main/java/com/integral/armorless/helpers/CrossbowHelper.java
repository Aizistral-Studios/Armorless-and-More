package com.integral.armorless.helpers;

import java.util.List;

import com.integral.armorless.ArmorlessMod;

import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CrossbowHelper {

	public static final String sharpshooterTagPrefix = ArmorlessMod.MODID+":sharpshot:";

	public static boolean hasCustomCrossbowEnchantments(ItemStack crossbowStack) {
		return (EnchantmentHelper.getEnchantmentLevel(ArmorlessMod.ceaselessEnchantment, crossbowStack) > 0 || EnchantmentHelper.getEnchantmentLevel(ArmorlessMod.sharpshooterEnchantment, crossbowStack) > 0);
	}

	public static boolean hasSharpshooterEnchantment(ItemStack crossbowStack) {
		return CrossbowHelper.getSharpshooterLevel(crossbowStack) > 0;
	}

	public static boolean hasCeaselessEnchantment(ItemStack crossbowStack) {
		return CrossbowHelper.getCeaselessLevel(crossbowStack) > 0;
	}

	public static int getSharpshooterLevel(ItemStack crossbowStack) {
		return EnchantmentHelper.getEnchantmentLevel(ArmorlessMod.sharpshooterEnchantment, crossbowStack);
	}

	public static int getCeaselessLevel(ItemStack crossbowStack) {
		return EnchantmentHelper.getEnchantmentLevel(ArmorlessMod.ceaselessEnchantment, crossbowStack);
	}

	public static boolean tryCharge(LivingEntity living, ItemStack crossbow, ItemStack ammo, boolean bonusCycles, boolean isCreative) {
		if (ammo.isEmpty()) {
			return false;
		} else {
			boolean creativeUsingArrows = isCreative && ammo.getItem() instanceof ArrowItem;
			ItemStack itemstack;
			if (!creativeUsingArrows && !isCreative && !bonusCycles) {
				itemstack = ammo.split(1);
				if (ammo.isEmpty() && living instanceof PlayerEntity) {
					((PlayerEntity) living).inventory.deleteStack(ammo);
				}
			} else {
				itemstack = ammo.copy();
			}

			CrossbowItem.addChargedProjectile(crossbow, itemstack);
			return true;
		}
	}

	public static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
		int requestedAmmo = 1;
		boolean isCreative = entityIn instanceof PlayerEntity && ((PlayerEntity) entityIn).abilities.isCreativeMode;
		ItemStack itemstack = entityIn.findAmmo(stack);
		ItemStack itemstack1 = itemstack.copy();

		for (int k = 0; k < requestedAmmo; ++k) {
			if (k > 0) {
				itemstack = itemstack1.copy();
			}

			if (itemstack.isEmpty() && isCreative) {
				itemstack = new ItemStack(Items.ARROW);
				itemstack1 = itemstack.copy();
			}

			if (!CrossbowHelper.tryCharge(entityIn, stack, (itemstack.getItem().getClass() == ArrowItem.class && CrossbowHelper.hasCeaselessEnchantment(stack)) ? itemstack.copy() : itemstack, k > 0, isCreative)) {
				return false;
			}
		}

		return true;
	}

	public static void fireProjectiles(World worldIn, LivingEntity shooter, Hand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
		List<ItemStack> list = CrossbowItem.getChargedProjectiles(stack);
		float[] afloat = CrossbowItem.getRandomSoundPitches(shooter.getRNG());

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemstack = list.get(i);
			boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity) shooter).abilities.isCreativeMode;
			if (!itemstack.isEmpty()) {
				if (i == 0) {
					CrossbowHelper.fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
				} else if (i == 1) {
					CrossbowHelper.fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F);
				} else if (i == 2) {
					CrossbowHelper.fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F);
				}
			}
		}

		CrossbowItem.fireProjectilesAfter(worldIn, shooter, stack);
	}

	private static void fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
		if (!worldIn.isRemote) {
			boolean isFireworks = projectile.getItem() == Items.FIREWORK_ROCKET;
			IProjectile iprojectile;
			if (isFireworks) {
				iprojectile = new FireworkRocketEntity(worldIn, projectile, shooter.getPosX(), shooter.getPosYEye() - 0.15F, shooter.getPosZ(), true);
			} else {
				iprojectile = CrossbowItem.createArrow(worldIn, shooter, crossbow, projectile);
				if (isCreativeMode || projectileAngle != 0.0F || CrossbowHelper.hasSharpshooterEnchantment(crossbow)) {
					((AbstractArrowEntity) iprojectile).pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;

					if (CrossbowHelper.hasSharpshooterEnchantment(crossbow)) {
						((AbstractArrowEntity) iprojectile).addTag(CrossbowHelper.sharpshooterTagPrefix + CrossbowHelper.getSharpshooterLevel(crossbow));
					}
				}
			}

			if (shooter instanceof ICrossbowUser) {
				ICrossbowUser icrossbowuser = (ICrossbowUser) shooter;
				icrossbowuser.shoot(icrossbowuser.getAttackTarget(), crossbow, iprojectile, projectileAngle);
			} else {
				Vec3d vec3d1 = shooter.getUpVector(1.0F);
				Quaternion quaternion = new Quaternion(new Vector3f(vec3d1), projectileAngle, true);
				Vec3d vec3d = shooter.getLook(1.0F);
				Vector3f vector3f = new Vector3f(vec3d);
				vector3f.transform(quaternion);
				iprojectile.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), velocity, inaccuracy);
			}

			crossbow.damageItem(isFireworks ? 3 : 1, shooter, (player) -> {
				player.sendBreakAnimation(handIn);
			});
			worldIn.addEntity((Entity) iprojectile);
			worldIn.playSound((PlayerEntity) null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
		}
	}

}
