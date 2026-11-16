package com.example.knifemod.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.example.knifemod.ModItems;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

/** The knife the player picked in the J menu. Saved to config/knifemod.txt. Client-only. */
public final class KnifeSelection {
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("knifemod.txt");
	private static Item selected = null;

	private KnifeSelection() {}

	/** The chosen knife, or null if the player wants the normal item model. */
	public static Item get() {
		return selected;
	}

	public static void set(Item knife) {
		selected = knife;
		save();
	}

	public static void load() {
		try {
			if (Files.exists(FILE)) {
				String id = Files.readString(FILE).trim();
				for (Item knife : ModItems.KNIVES) {
					if (Registries.ITEM.getId(knife).getPath().equals(id)) {
						selected = knife;
					}
				}
			}
		} catch (IOException ignored) {
		}
	}

	private static void save() {
		try {
			Files.writeString(FILE, selected == null ? "" : Registries.ITEM.getId(selected).getPath());
		} catch (IOException ignored) {
		}
	}
}
