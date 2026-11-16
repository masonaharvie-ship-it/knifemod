package com.example.knifemod.client;

import com.example.knifemod.ModItems;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/** Menu opened with J: pick which knife is shown when you hold a sword. */
public class KnifeMenuScreen extends Screen {
	public KnifeMenuScreen() {
		super(Text.literal("Choose your knife"));
	}

	@Override
	protected void init() {
		final int cols = 3, w = 140, h = 20, gap = 4;
		int startX = width / 2 - (cols * w + (cols - 1) * gap) / 2;
		int startY = 52;

		int i = 0;
		for (Item knife : ModItems.KNIVES) {
			int x = startX + (i % cols) * (w + gap);
			int y = startY + (i / cols) * (h + gap);
			addDrawableChild(ButtonWidget.builder(new ItemStack(knife).getName(), b -> {
				KnifeSelection.set(knife);
				close();
			}).dimensions(x, y, w, h).build());
			i++;
		}

		int rows = (ModItems.KNIVES.size() + cols - 1) / cols;
		int resetY = startY + rows * (h + gap) + 8;
		addDrawableChild(ButtonWidget.builder(Text.literal("Default (no knife)"), b -> {
			KnifeSelection.set(null);
			close();
		}).dimensions(width / 2 - 70, resetY, 140, h).build());
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFFFF);
		Item sel = KnifeSelection.get();
		Text current = sel == null ? Text.literal("Current: default") : Text.literal("Current: ").append(new ItemStack(sel).getName());
		context.drawCenteredTextWithShadow(textRenderer, current, width / 2, 36, 0xFFAAAAAA);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
