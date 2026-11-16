package com.example.knifemod.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.knifemod.InspectStyle;
import com.example.knifemod.client.InspectAnimation;
import com.example.knifemod.client.KnifeSelection;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	/**
	 * Parameters are captured with @Local instead of spelling out the whole method
	 * descriptor, because the renderer's signature changed in 1.21.9+ and tends to
	 * change again. Only the method name has to stay the same.
	 */
	/** Shows the knife picked in the J menu in place of a sword in your first-person hand. Visual only. */
	@ModifyVariable(method = "renderFirstPersonItem", at = @At("HEAD"), argsOnly = true)
	private ItemStack knifemod$swapModel(ItemStack item) {
		Item chosen = KnifeSelection.get();
		if (chosen != null && item.isIn(ItemTags.SWORDS)) {
			return new ItemStack(chosen);
		}
		return item;
	}

	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
	private void knifemod$inspect(CallbackInfo ci,
			@Local(argsOnly = true, ordinal = 0) float tickProgress,
			@Local(argsOnly = true) Hand hand,
			@Local(argsOnly = true) ItemStack item,
			@Local(argsOnly = true) MatrixStack matrices) {
		if (hand != Hand.MAIN_HAND || !InspectAnimation.isActive()) {
			return;
		}
		InspectStyle style = InspectAnimation.styleFor(item);
		if (style != null) {
			InspectAnimation.apply(matrices, tickProgress, style);
		}
	}
}
