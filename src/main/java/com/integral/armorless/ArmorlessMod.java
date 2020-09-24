package com.integral.armorless;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.integral.armorless.enchantments.CeaselessEnchantment;
import com.integral.armorless.enchantments.SharpshooterEnchantment;
import com.integral.armorless.handlers.ArmorlessEventHandler;
import com.integral.armorless.items.PhantomIngot;
import com.integral.armorless.items.UnseenArmor;

import java.util.stream.Collectors;


@Mod("armorless")
public class ArmorlessMod {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "armorless";
	public static final String NAME = "Armorless & More";
	public static final String VERSION = "FORGE-1.15.2-v2.1.0";

	public static SharpshooterEnchantment sharpshooterEnchantment;
	public static CeaselessEnchantment ceaselessEnchantment;

	public static PhantomIngot phantomIngot;

	public static UnseenArmor unseenHelmet;
	public static UnseenArmor unseenChestplate;
	public static UnseenArmor unseenLeggings;
	public static UnseenArmor unseenBoots;

	public ArmorlessMod() {

		/*
		 * It's about good time to instantiate our items/enchantments/blocks/whatever,
		 * so that we can keep reference to them and register in registry events later.
		 */

		ArmorlessMod.sharpshooterEnchantment = new SharpshooterEnchantment();
		ArmorlessMod.ceaselessEnchantment = new CeaselessEnchantment();

		ArmorlessMod.phantomIngot = new PhantomIngot();

		ArmorlessMod.unseenHelmet = new UnseenArmor(EquipmentSlotType.HEAD);
		ArmorlessMod.unseenChestplate = new UnseenArmor(EquipmentSlotType.CHEST);
		ArmorlessMod.unseenLeggings = new UnseenArmor(EquipmentSlotType.LEGS);
		ArmorlessMod.unseenBoots = new UnseenArmor(EquipmentSlotType.FEET);

		/*
		 * Subscribing our setup methods to ModEventBus, so that they actually
		 * do something.
		 */

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerStarting);

		MinecraftForge.EVENT_BUS.register(this);

		/*
		 * We could do all event stuff in our main class to be honest, but whoever does that
		 * will be forever cursed, so let's register discrete event handler for that sake.
		 */

		ArmorlessEventHandler eventHandler = new ArmorlessEventHandler();

		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLJavaModLoadingContext.get().getModEventBus().register(eventHandler);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// NO-OP
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// NO-OP
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// NO-OP
	}

	private void processIMC(final InterModProcessEvent event) {
		// NO-OP
	}

	private void onServerStarting(final FMLServerStartingEvent event) {
		// NO-OP
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
			// NO-OP
		}

		@SubscribeEvent
		public static void onEnchantmentRegistry(final RegistryEvent.Register<Enchantment> event) {
			event.getRegistry().registerAll(
					ArmorlessMod.sharpshooterEnchantment,
					ArmorlessMod.ceaselessEnchantment);
		}

		@SubscribeEvent
		public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
			event.getRegistry().registerAll(
					ArmorlessMod.phantomIngot,
					ArmorlessMod.unseenHelmet,
					ArmorlessMod.unseenChestplate,
					ArmorlessMod.unseenLeggings,
					ArmorlessMod.unseenBoots);
		}
	}
}
