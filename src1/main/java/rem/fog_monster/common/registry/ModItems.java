package rem.fog_monster.common.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.fog_monster.FogMonster;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FogMonster.MODID);

    /**
     * Simple spawn egg so you can test quickly (Creative tab: Spawn Eggs).
     */
    public static final RegistryObject<Item> FOG_MONSTER_SPAWN_EGG = ITEMS.register(
            "fog_monster_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FOG_MONSTER, 0x2A2A2A, 0xB7B7B7, new Item.Properties())
    );
}
