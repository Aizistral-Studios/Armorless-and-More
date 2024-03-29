package com.integral.armorless.handlers;

import java.util.Random;

import com.google.common.eventbus.Subscribe;
import com.integral.armorless.ArmorlessMod;
import com.integral.armorless.helpers.CrossbowHelper;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class ArmorlessEventHandler {

	public static final Random theySeeMeRollin = new Random();

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Stop event) {

		if (ArmorlessEventHandler.isEnigmaticLegacyLoaded())
			return;

		if (event.getItem().getItem() instanceof CrossbowItem && event.getEntityLiving() instanceof Player) {
			CrossbowItem crossbow = (CrossbowItem) event.getItem().getItem();
			ItemStack crossbowStack = event.getItem();

			/*
			 * Make sure that our crossbow actually has any of this mod's enchantments,
			 * since otherwise overriding it's behavior is unneccessary.
			 */

			if (!CrossbowHelper.hasCustomCrossbowEnchantments(crossbowStack))
				return;

			// Cancelling the event to prevent vanilla functionality from working
			event.setCanceled(true);

			/*
			 * Same code as in CrossbowItem#onPlayerStoppedUsing, but CrossbowItem#hasAmmo is
			 * replaced with altered method from CrossbowHelper, to enforce custom behavior.
			 */

			int i = crossbow.getUseDuration(crossbowStack) - event.getDuration();
			float f = CrossbowItem.getPowerForTime(i, crossbowStack);
			if (f >= 1.0F && !CrossbowItem.isCharged(crossbowStack) && CrossbowHelper.hasAmmo(event.getEntityLiving(), crossbowStack)) {
				CrossbowItem.setCharged(crossbowStack, true);
				SoundSource soundcategory = event.getEntityLiving() instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
				event.getEntityLiving().level.playSound((Player) null, event.getEntityLiving().getX(), event.getEntityLiving().getY(), event.getEntityLiving().getZ(), SoundEvents.CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (ArmorlessEventHandler.theySeeMeRollin.nextFloat() * 0.5F + 1.0F) + 0.2F);
			}

		}
	}

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event) {

		if (ArmorlessEventHandler.isEnigmaticLegacyLoaded())
			return;

		if (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickBlock || event instanceof PlayerInteractEvent.EntityInteract) {
			if (event.getItemStack().getItem() instanceof CrossbowItem) {
				ItemStack itemstack = event.getItemStack();

				/*
				 * Make sure that our crossbow actually has any of this mod's enchantments,
				 * since otherwise overriding it's behavior is unneccessary.
				 */

				if (!CrossbowHelper.hasCustomCrossbowEnchantments(itemstack))
					return;

				// Cancelling the event to prevent vanilla functionality from working
				event.setCanceled(true);

				/*
				 * Same code as in CrossbowItem#onItemRightClick, but CrossbowItem#fireProjectiles is
				 * replaced with altered method from CrossbowHelper, to enforce custom behavior.
				 */

				if (CrossbowItem.isCharged(itemstack)) {
					CrossbowHelper.fireProjectiles(event.getWorld(), event.getPlayer(), event.getHand(), itemstack, CrossbowItem.getShootingPower(itemstack), 1.0F);
					CrossbowItem.setCharged(itemstack, false);
					event.setCancellationResult(InteractionResult.CONSUME);
				} else if (!event.getPlayer().getProjectile(itemstack).isEmpty()) {
					if (!CrossbowItem.isCharged(itemstack)) {
						((CrossbowItem) Items.CROSSBOW).startSoundPlayed = false;
						((CrossbowItem) Items.CROSSBOW).midLoadSoundPlayed = false;
						event.getPlayer().startUsingItem(event.getHand());
					}
					event.setCancellationResult(InteractionResult.CONSUME);
				} else {
					event.setCancellationResult(InteractionResult.FAIL);
				}
			}
		}
	}

	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {

		if (ArmorlessEventHandler.isEnigmaticLegacyLoaded())
			return;

		if (event.getSource() != null && event.getSource().getDirectEntity() instanceof AbstractArrow) {
			AbstractArrow arrow = (AbstractArrow) event.getSource().getDirectEntity();

			for (String tag : arrow.getTags()) {

				if (tag.startsWith(CrossbowHelper.sharpshooterTagPrefix)) {

					/*
					 * Since our custom tag is just a prefix + enchantment level, we can remove
					 * that prefix from received String and freely parse remaining Integer.
					 */

					event.setAmount(6 + (3 * Integer.parseInt(tag.replace(CrossbowHelper.sharpshooterTagPrefix, ""))));
					break;
				}
			}
		}
	}

	public static boolean isEnigmaticLegacyLoaded() {
		return ModList.get().isLoaded("enigmaticlegacy");
	}

}
