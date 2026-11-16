package com.example.knifemod;

import net.fabricmc.api.ModInitializer;

public class KnifeMod implements ModInitializer {
	public static final String MOD_ID = "knifemod";

	@Override
	public void onInitialize() {
		ModItems.init();
	}
}
