# Knife Mod (Fabric, Minecraft 1.21.11)

CS:GO-style knives with a flashy first-person inspect animation.

## What's in it
- 16 knives: Karambit, Butterfly Knife, Switchblade, Flip Knife, Bayonet, M9 Bayonet, Huntsman, Gut, Bowie, Falchion, Navaja, Stiletto, Talon, Ursus, Skeleton, Kukri. Find them in the Combat creative tab.
- Knife Case: right-click to open it and get a random knife. Recipe: iron ingots in a ring around a chest.
- Knife menu: press J to pick a knife. While holding any vanilla sword, your first-person hand shows that knife instead (client-side only; your choice is saved in `config/knifemod.txt`). Pick "Default" to go back to the normal sword.
- Inspect animation: hold a knife (or any vanilla sword) and press Y. Both keys are rebindable under Controls > Knife Mod. Each knife has its own style:
  - Karambit / Falchion / Skeleton: ring-finger double spin
  - Butterfly / Talon: balisong-style roll plus fast yaw spin
  - Flip knife / Gut / Ursus / vanilla swords: forward roll with a wrist twist
  - Switchblade / Navaja / Stiletto: flick open, tilt, flick back
  - Everything else: slow classic turn
- Swinging or switching slots cancels the inspect.

## Using it on multiplayer servers
- The knives and the case only exist with this mod installed on the server too. On a normal server you can't get them, so they do nothing there.
- The inspect animation is purely client-side: it only moves your own hand model, sends no packets and gives no gameplay advantage. That is why it also works on vanilla swords.
- Server rules differ and staff have final say. Check the server's modification rules, or ask staff, before running any mod on a server you care about.

## Build
You need JDK 21 and Gradle 8.14+ (or run `gradle wrapper` once to generate the wrapper).

    gradle build

The mod jar appears in `build/libs/knifemod-1.0.0.jar`. Put it in your `.minecraft/mods` folder with Fabric Loader 0.18.1+ and Fabric API for 1.21.11.

To try it from your IDE: `gradle runClient`.

## Notes
- Versions in `gradle.properties` were looked up (Fabric API 0.141.3, loader 0.18.1, Loom 1.14); the yarn mappings string `1.21.11+build.4` is from memory. If Gradle can't find it, copy the current value from https://fabricmc.net/develop.
- The textures are generated 16x16 placeholders. Replace the PNGs in `src/main/resources/assets/knifemod/textures/item/` with your own art.
- The inspect animation is a mixin into `HeldItemRenderer.renderFirstPersonItem`, capturing its arguments with `@Local` so the exact signature doesn't matter. If Minecraft fails to start with a mixin error, that mixin is the first place to look.
- Tweak animation feel in `InspectAnimation.java` (duration, angles, pivot) and stats in `ModItems.java`.
