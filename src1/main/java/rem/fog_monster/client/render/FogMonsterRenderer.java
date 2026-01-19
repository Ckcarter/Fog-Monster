package rem.fog_monster.client.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import rem.fog_monster.FogMonster;
import rem.fog_monster.common.entity.FogMonsterEntity;

/**
 * Simple renderer reusing the vanilla Zombie model so you don't need a custom geo/model right now.
 */
public class FogMonsterRenderer extends HumanoidMobRenderer<FogMonsterEntity, HumanoidModel<FogMonsterEntity>> {

    private static final ResourceLocation TEX = new ResourceLocation(FogMonster.MODID, "textures/entity/fog_monster.png");

    public FogMonsterRenderer(EntityRendererProvider.Context ctx) {
        // Use the baked zombie humanoid layer, but render with the generic HumanoidModel so
        // our entity doesn't need to extend Zombie (ZombieModel has a generic bound).
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FogMonsterEntity entity) {
        return TEX;
    }
}
