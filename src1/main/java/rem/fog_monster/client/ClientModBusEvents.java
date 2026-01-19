package rem.fog_monster.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.EntityRenderersEvent;
import rem.fog_monster.FogMonster;
import rem.fog_monster.common.registry.ModEntities;
import rem.fog_monster.client.render.FogMonsterRenderer;

@Mod.EventBusSubscriber(modid = FogMonster.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModBusEvents {
    private ClientModBusEvents() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FOG_MONSTER.get(), FogMonsterRenderer::new);
    }
}
