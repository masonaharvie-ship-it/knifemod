package com.example.knifemod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class ModItems {
	/** Every knife, used by the case to pick a random one. */
	public static final List<Item> KNIVES = new ArrayList<>();

	// id, damage bonus (on top of fist's 1.0), attack speed modifier (base is 4.0), inspect style
	public static final Item KARAMBIT       = knife("karambit",       3.5, -0.8, InspectStyle.KARAMBIT);
	public static final Item BUTTERFLY      = knife("butterfly_knife", 3.0, -0.6, InspectStyle.BUTTERFLY);
	public static final Item SWITCHBLADE    = knife("switchblade",    3.0, -0.7, InspectStyle.SWITCH);
	public static final Item FLIP_KNIFE     = knife("flip_knife",     3.0, -0.8, InspectStyle.FLIP);
	public static final Item BAYONET        = knife("bayonet",        3.5, -1.0, InspectStyle.CLASSIC);
	public static final Item M9_BAYONET     = knife("m9_bayonet",     4.0, -1.1, InspectStyle.CLASSIC);
	public static final Item HUNTSMAN       = knife("huntsman_knife", 3.5, -1.0, InspectStyle.CLASSIC);
	public static final Item GUT_KNIFE      = knife("gut_knife",      3.0, -0.7, InspectStyle.FLIP);
	public static final Item BOWIE          = knife("bowie_knife",    4.5, -1.4, InspectStyle.CLASSIC);
	public static final Item FALCHION       = knife("falchion_knife", 3.5, -0.9, InspectStyle.KARAMBIT);
	public static final Item NAVAJA         = knife("navaja_knife",   2.5, -0.6, InspectStyle.SWITCH);
	public static final Item STILETTO       = knife("stiletto_knife", 3.0, -0.6, InspectStyle.SWITCH);
	public static final Item TALON          = knife("talon_knife",    3.5, -0.8, InspectStyle.BUTTERFLY);
	public static final Item URSUS          = knife("ursus_knife",    3.5, -0.9, InspectStyle.FLIP);
	public static final Item SKELETON       = knife("skeleton_knife", 3.5, -0.8, InspectStyle.KARAMBIT);
	public static final Item KUKRI          = knife("kukri_knife",    4.0, -1.2, InspectStyle.CLASSIC);

	public static final Item KNIFE_CASE = register("knife_case",
			s -> new KnifeCaseItem(s), new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON));

	private static Item knife(String id, double damage, double speed, InspectStyle style) {
		AttributeModifiersComponent attrs = AttributeModifiersComponent.builder()
				.add(EntityAttributes.ATTACK_DAMAGE,
						new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, damage,
								EntityAttributeModifier.Operation.ADD_VALUE),
						AttributeModifierSlot.MAINHAND)
				.add(EntityAttributes.ATTACK_SPEED,
						new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, speed,
								EntityAttributeModifier.Operation.ADD_VALUE),
						AttributeModifierSlot.MAINHAND)
				.build();
		Item item = register(id, s -> new KnifeItem(s, style),
				new Item.Settings().maxCount(1).rarity(Rarity.RARE).attributeModifiers(attrs));
		KNIVES.add(item);
		return item;
	}

	private static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(KnifeMod.MOD_ID, id));
		return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
	}

	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			KNIVES.forEach(entries::add);
			entries.add(KNIFE_CASE);
		});
	}
}
