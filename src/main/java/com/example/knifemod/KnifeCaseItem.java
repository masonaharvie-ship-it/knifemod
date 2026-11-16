package com.example.knifemod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/** Right-click to open and get a random knife, like a CS:GO case. */
public class KnifeCaseItem extends Item {
	public KnifeCaseItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!world.isClient()) {
			Item pick = ModItems.KNIVES.get(world.random.nextInt(ModItems.KNIVES.size()));
			ItemStack out = new ItemStack(pick);
			stack.decrement(1);
			player.getInventory().offerOrDrop(out);
			player.sendMessage(Text.literal("★ You unboxed: ").append(out.getName()), true);
			world.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5f, 1.4f);
		}
		return ActionResult.SUCCESS;
	}
}
