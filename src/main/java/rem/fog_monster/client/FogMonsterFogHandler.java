package rem.fog_monster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rem.fog_monster.common.entity.FogMonsterEntity;

/**
 * Client-side fog controller.
 *
 * When a Fog Monster is nearby (and especially when it has you targeted), dense fog rolls in.
 * This is renderer-only and therefore safe for multiplayer.
 */
@Mod.EventBusSubscriber(modid = rem.fog_monster.FogMonster.MODID, value = Dist.CLIENT)
public final class FogMonsterFogHandler {
    private FogMonsterFogHandler() {}

    /** 0..1 strength, smoothed so fog doesn't pop. */
    private static float fogStrength = 0.0f;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) {
            fogStrength = 0.0f;
            return;
        }

        double closest = Double.MAX_VALUE;

        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof FogMonsterEntity monster)) continue;

            // Make the effect much stronger if the monster is actively hunting *you*.
            boolean huntingYou = monster.getTarget() == player;

            double d = monster.distanceTo(player);
            if (huntingYou) d *= 0.65; // effectively “closer” when targeted

            if (d < closest) closest = d;
        }

        // --- Tuning (dense horror preset) ---
        // Fog begins earlier and ramps to full density fast.
        float target = 0.0f;
        if (closest < 80.0) {
            // Full fog around ~4 blocks; still noticeable by ~20 blocks.
            target = 1.2f - (float) (closest / 80.0f);
            target = Mth.clamp(target, 0.0f, 1.0f);
        }

        // Heavier smoothing (pushes in faster, still no popping).
        fogStrength = Mth.lerp(0.12f, fogStrength, target);
    }

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        if (fogStrength <= 0.01f) return;

        // Vanilla fog distances can vary; we “pull in” the far plane.
        // Strongest fog ends up ~2.5 blocks.
        float far = Mth.lerp(fogStrength, event.getFarPlaneDistance(), 2.5f);

        event.setNearPlaneDistance(0.05f);
        event.setFarPlaneDistance(far);

        // We cancel so our distances take effect.
        event.setCanceled(true);
    }
}
