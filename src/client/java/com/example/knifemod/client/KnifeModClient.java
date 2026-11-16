package com.example.knifemod.client;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class KnifeModClient implements ClientModInitializer {
	public static KeyBinding inspectKey;
	public static KeyBinding menuKey;

	@Override
	public void onInitializeClient() {
		KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("knifemod", "main"));
		inspectKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.knifemod.inspect", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, category));
		menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.knifemod.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, category));

		KnifeSelection.load();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) {
				InspectAnimation.stop();
				return;
			}

			while (menuKey.wasPressed()) {
				client.setScreen(new KnifeMenuScreen());
			}

			boolean canInspect = InspectAnimation.styleFor(client.player.getMainHandStack()) != null;
			InspectAnimation.tick(canInspect, client.player.getInventory().getSelectedSlot(),
					client.player.handSwinging);

			while (inspectKey.wasPressed()) {
				if (canInspect && !InspectAnimation.isActive()) {
					InspectAnimation.start();
					// client-only sound, nothing is sent to the server
					client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 0.4f, 1.7f);
				}
			}
		});
	}
}
