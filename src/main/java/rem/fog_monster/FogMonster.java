package rem.fog_monster;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import rem.fog_monster.common.registry.ModEntities;
import rem.fog_monster.common.registry.ModItems;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FogMonster.MODID)
public class FogMonster {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "fog_monster";

    public FogMonster() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registries
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        // Common setup + events
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onAttributes);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // no-op
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Put the spawn egg on the vanilla Spawn Eggs tab
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.FOG_MONSTER_SPAWN_EGG);
        }
    }

    private void onAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FOG_MONSTER.get(), rem.fog_monster.common.entity.FogMonsterEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // no-op
    }

    /**
     * Client-only mod-bus events live here so dedicated servers never load client classes.
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        // Renderer registration is in rem.fog_monster.client.ClientModBusEvents
    }
}
