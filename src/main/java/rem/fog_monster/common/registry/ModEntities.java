package rem.fog_monster.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.fog_monster.FogMonster;
import rem.fog_monster.common.entity.FogMonsterEntity;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FogMonster.MODID);

    public static final RegistryObject<EntityType<FogMonsterEntity>> FOG_MONSTER = ENTITIES.register(
            "fog_monster",
            () -> EntityType.Builder.of(FogMonsterEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f) // humanoid-ish
                    .build(new ResourceLocation(FogMonster.MODID, "fog_monster").toString())
    );
}
