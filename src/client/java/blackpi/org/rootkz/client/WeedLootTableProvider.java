package blackpi.org.rootkz.client;

import blackpi.org.rootkz.block.ModBlocks;
import blackpi.org.rootkz.block.WeedCropBlock;
import blackpi.org.rootkz.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class WeedLootTableProvider extends FabricBlockLootTableProvider {
    protected WeedLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Loot cuando el cultivo está maduro (AGE == 7)
        LootPool.Builder maturePool = LootPool.builder()
                .conditionally(BlockStatePropertyLootCondition.builder(ModBlocks.WEED_CROP)
                        .properties(StatePredicate.Builder.create()
                                .exactMatch(WeedCropBlock.AGE, 7)))
                .with(ItemEntry.builder(ModItems.BUD)
                        .apply(SetCountLootFunction.builder(
                                ConstantLootNumberProvider.create(5))))
                .with(ItemEntry.builder(ModItems.WEED_SEED)
                        .apply(SetCountLootFunction.builder(
                                ConstantLootNumberProvider.create(2))));

        // Loot por defecto (para cualquier otro estado de crecimiento)
        LootPool.Builder defaultSeedPool = LootPool.builder()
                .with(ItemEntry.builder(ModItems.WEED_SEED));

        // Loot table completa
        LootTable.Builder tableBuilder = LootTable.builder()
                .pool(maturePool)
                .pool(defaultSeedPool);

        // Asignar el loot table al bloque
        addDrop(ModBlocks.WEED_CROP, tableBuilder);
    }




}