// Nrbmod.java (entry point)
package blackpi.org.rootkz;
import blackpi.org.rootkz.block.ModBlocks;
import blackpi.org.rootkz.effects.WeedEffect;
import blackpi.org.rootkz.entities.ModEntities;
import blackpi.org.rootkz.entity.RastaEntity;
import blackpi.org.rootkz.entity.RastaTraderManager;
import blackpi.org.rootkz.item.ModItems;
import blackpi.org.rootkz.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.ServerWorldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Rootkz implements ModInitializer {
    public static final String MOD_ID = "rootkz";
    public static final Logger LOGGER = LoggerFactory.getLogger(Rootkz.MOD_ID);
    private final Map<ServerWorld, RastaTraderManager> managerMap = new HashMap<>();
    public static final RegistryEntry<StatusEffect> WEED =
            Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Rootkz.MOD_ID, "weed"), new WeedEffect());


    @Override
    public void onInitialize() {
        ModSounds.initialize();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModEntities.register();
        LOGGER.info("Mod inicializado");

        FabricDefaultAttributeRegistry.register(ModEntities.RASTA, RastaEntity.createVillagerAttributes());
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!(world instanceof ServerWorld serverWorld)) return;

            RastaTraderManager manager = managerMap.computeIfAbsent(serverWorld, w -> {
                ServerWorldProperties props = (ServerWorldProperties) serverWorld.getLevelProperties();
                return new RastaTraderManager(props);
            });

            manager.spawn(serverWorld, true);
        });
    }
}
