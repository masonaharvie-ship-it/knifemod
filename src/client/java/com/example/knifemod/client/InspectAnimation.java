package com.example.knifemod.client;

import com.example.knifemod.InspectStyle;
import com.example.knifemod.KnifeItem;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

/** Client-side state and math for the first-person inspect animation. */
public final class InspectAnimation {
	public static final int DURATION = 48; // ticks (2.4 seconds)

	private static int ticks = -1;
	private static int lastSlot = -1;

	private InspectAnimation() {}

	/**
	 * Which style to play for a held stack, or null if it can't be inspected.
	 * Knives use their own style; vanilla swords get a flip so the animation also
	 * works on servers where the knife items don't exist. Purely visual, no packets.
	 */
	public static InspectStyle styleFor(ItemStack stack) {
		if (stack.getItem() instanceof KnifeItem knife) {
			return knife.style;
		}
		if (stack.isIn(ItemTags.SWORDS)) {
			// a sword shown as the knife picked in the J menu uses that knife's style
			if (KnifeSelection.get() instanceof KnifeItem chosen) {
				return chosen.style;
			}
			return InspectStyle.FLIP;
		}
		return null;
	}

	public static boolean isActive() {
		return ticks >= 0;
	}

	public static void start() {
		ticks = 0;
	}

	public static void stop() {
		ticks = -1;
	}

	/** Called every client tick. Cancels the animation if the knife is put away or you swing. */
	public static void tick(boolean holdingKnife, int selectedSlot, boolean swinging) {
		if (!holdingKnife || selectedSlot != lastSlot || swinging) {
			ticks = -1;
		}
		lastSlot = selectedSlot;
		if (ticks >= 0) {
			ticks++;
			if (ticks > DURATION) {
				ticks = -1;
			}
		}
	}

	private static float smooth(float x) {
		x = Math.max(0f, Math.min(1f, x));
		return x * x * (3f - 2f * x);
	}

	/** Applies the inspect transform to the matrix stack before the held item is drawn. */
	public static void apply(MatrixStack matrices, float tickDelta, InspectStyle style) {
		float p = Math.min(1f, (ticks + tickDelta) / DURATION);

		// envelope: raise the knife into view, hold it, lower it again
		float e = Math.min(1f, (float) Math.sin(Math.PI * p) * 1.6f);
		// the spin itself happens in the middle of the animation
		float spin = smooth((p - 0.15f) / 0.7f);

		float rx, ry, rz;
		switch (style) {
			case KARAMBIT -> { // ring-finger flip: two full turns on the roll axis
				rz = spin * 720f;
				rx = -25f * e;
				ry = 25f * e;
			}
			case BUTTERFLY -> { // balisong-style: roll plus a fast yaw spin
				rz = spin * 360f;
				ry = spin * 720f;
				rx = -20f * e;
			}
			case FLIP -> { // forward roll with a wrist twist
				rx = -25f * e - spin * 360f;
				ry = 30f * e;
				rz = 10f * e * (float) Math.sin(spin * Math.PI * 2);
			}
			case SWITCH -> { // flick open, tilt, flick back
				rx = 40f * (float) Math.sin(spin * Math.PI * 2) - 15f * e;
				ry = -spin * 360f;
				rz = 180f * spin;
			}
			default -> { // classic: turn the blade to the light
				rx = -30f * e;
				ry = 70f * (float) Math.sin(p * Math.PI * 2);
				rz = 15f * (float) Math.sin(p * Math.PI * 2);
			}
		}

		// bring the knife toward the centre of the screen
		matrices.translate(-0.25f * e, 0.12f * e, 0.15f * e);

		// rotate around roughly where the main-hand item sits on screen
		final float px = 0.56f, py = -0.5f, pz = -0.72f;
		matrices.translate(px, py, pz);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rx));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ry));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rz));
		matrices.translate(-px, -py, -pz);
	}
}
