package com.integral.armorless.helpers;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.integral.armorless.ArmorlessMod;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CrossbowHelper {

	public static final String sharpshooterTagPrefix = ArmorlessMod.MODID+":sharpshot:";

	public static boolean hasCustomCrossbowEnchantments(ItemStack crossbowStack) {
		return (EnchantmentHelper.getItemEnchantmentLevel(ArmorlessMod.ceaselessEnchantment, crossbowStack) > 0 || EnchantmentHelper.getItemEnchantmentLevel(ArmorlessMod.sharpshooterEnchantment, crossbowStack) > 0);
	}

	public static boolean hasSharpshooterEnchantment(ItemStack crossbowStack) {
		return CrossbowHelper.getSharpshooterLevel(crossbowStack) > 0;
	}

	public static boolean hasCeaselessEnchantment(ItemStack crossbowStack) {
		return CrossbowHelper.getCeaselessLevel(crossbowStack) > 0;
	}

	public static int getSharpshooterLevel(ItemStack crossbowStack) {
		return EnchantmentHelper.getItemEnchantmentLevel(ArmorlessMod.sharpshooterEnchantment, crossbowStack);
	}

	public static int getCeaselessLevel(ItemStack crossbowStack) {
		return EnchantmentHelper.getItemEnchantmentLevel(ArmorlessMod.ceaselessEnchantment, crossbowStack);
	}

	public static boolean tryCharge(LivingEntity living, ItemStack crossbow, ItemStack ammo, boolean bonusCycles, boolean isCreative) {
		if (ammo.isEmpty())
			return false;
		else {
			boolean creativeUsingArrows = isCreative && ammo.getItem() instanceof ArrowItem;
			ItemStack itemstack;
			if (!creativeUsingArrows && !isCreative && !bonusCycles) {
				itemstack = ammo.split(1);
				if (ammo.isEmpty() && living instanceof Player) {
					((Player) living).getInventory().removeItem(ammo);
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
		boolean isCreative = entityIn instanceof Player && ((Player) entityIn).getAbilities().instabuild;
		ItemStack itemstack = entityIn.getProjectile(stack);
		ItemStack itemstack1 = itemstack.copy();

		for (int k = 0; k < requestedAmmo; ++k) {
			if (k > 0) {
				itemstack = itemstack1.copy();
			}

			if (itemstack.isEmpty() && isCreative) {
				itemstack = new ItemStack(Items.ARROW);
				itemstack1 = itemstack.copy();
			}

			if (!CrossbowHelper.tryCharge(entityIn, stack, (itemstack.getItem().getClass() == ArrowItem.class && CrossbowHelper.hasCeaselessEnchantment(stack)) ? itemstack.copy() : itemstack, k > 0, isCreative))
				return false;
		}

		return true;
	}

	public static void fireProjectiles(Level worldIn, LivingEntity shooter, InteractionHand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
		List<ItemStack> list = CrossbowItem.getChargedProjectiles(stack);
		float[] afloat = CrossbowItem.getShotPitches(shooter.getRandom());

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemstack = list.get(i);
			boolean flag = shooter instanceof Player && ((Player) shooter).getAbilities().instabuild;
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

		CrossbowItem.onCrossbowShot(worldIn, shooter, stack);
	}

	private static void fireProjectile(Level worldIn, LivingEntity shooter, InteractionHand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
		if (!worldIn.isClientSide) {
			boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
			Projectile projectileentity;
			if (flag) {
				projectileentity = new FireworkRocketEntity(worldIn, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15F, shooter.getZ(), true);
			} else {
				projectileentity = CrossbowItem.getArrow(worldIn, shooter, crossbow, projectile);
				if (isCreativeMode || projectileAngle != 0.0F || CrossbowHelper.hasSharpshooterEnchantment(crossbow)) {
					((AbstractArrow) projectileentity).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

					if (CrossbowHelper.hasSharpshooterEnchantment(crossbow)) {
						((AbstractArrow) projectileentity).addTag(CrossbowHelper.sharpshooterTagPrefix + CrossbowHelper.getSharpshooterLevel(crossbow));
					}
				}
			}

			if (shooter instanceof CrossbowAttackMob) {
				CrossbowAttackMob icrossbowuser = (CrossbowAttackMob)shooter;
				icrossbowuser.shootCrossbowProjectile(icrossbowuser.getTarget(), crossbow, projectileentity, projectileAngle);
			} else {
				Vec3 vector3d1 = shooter.getUpVector(1.0F);
				Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
				Vec3 vector3d = shooter.getViewVector(1.0F);
				Vector3f vector3f = new Vector3f(vector3d);
				vector3f.transform(quaternion);
				projectileentity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
			}

			crossbow.hurtAndBreak(flag ? 3 : 1, shooter, (p_220017_1_) -> {
				p_220017_1_.broadcastBreakEvent(handIn);
			});
			worldIn.addFreshEntity(projectileentity);
			worldIn.playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
		}
	}

}