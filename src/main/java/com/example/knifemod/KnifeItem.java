package com.example.knifemod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class KnifeItem extends Item {
	public final InspectStyle style;

	public KnifeItem(Settings settings, InspectStyle style) {
		super(settings);
		this.style = style;
	}

	/** Right-click flicks the knife: a quick click sound, handy for the switchblade and butterfly. */
	@Override
	public ActionResult use(World world, PlayerEntity player, Hand hand) {
		if (style != InspectStyle.CLASSIC) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.PLAYERS, 0.35f, 1.9f);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
