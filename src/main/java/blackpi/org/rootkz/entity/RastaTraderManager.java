//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package blackpi.org.rootkz.entity;

import java.util.Optional;

import blackpi.org.rootkz.Rootkz;
import blackpi.org.rootkz.entities.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.rule.GameRules; // 1.21.11
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestTypes;
import net.minecraft.world.poi.PointOfInterestStorage.OccupationStatus;
import net.minecraft.world.spawner.SpecialSpawner;
import org.jetbrains.annotations.Nullable;

public class RastaTraderManager implements SpecialSpawner {
    private static final int DEFAULT_SPAWN_TIMER = 1200;
    public static final int DEFAULT_SPAWN_DELAY = 24000;
    private static final int DEFAULT_SPAWN_CHANCE = 25;
    //private static final int field_30635 = 10; // Que putas es?
    //private static final int field_30636 = 10; // ??
    private final Random random = Random.create();
    private final ServerWorldProperties properties;
    private int spawnTimer;
    private int spawnDelay;
    private int spawnChance;

    public RastaTraderManager(ServerWorldProperties properties) {
        this.properties = properties;
        this.spawnTimer = DEFAULT_SPAWN_TIMER;
        this.spawnDelay = DEFAULT_SPAWN_DELAY;
        this.spawnChance = DEFAULT_SPAWN_CHANCE;

        this.spawnDelay = properties.getWanderingTraderSpawnDelay();
        this.spawnChance = properties.getWanderingTraderSpawnChance();
        if (this.spawnDelay == 0 && this.spawnChance == 0) {
            this.spawnDelay = 24000;
            properties.setWanderingTraderSpawnDelay(this.spawnDelay);
            this.spawnChance = 25;
            properties.setWanderingTraderSpawnChance(this.spawnChance);
        }

    }

    @Override
    public void spawn(ServerWorld world, boolean spawnMonsters) {
        if (--this.spawnTimer > 0) {
            return;
        }

        this.spawnTimer = 1200;
        this.spawnDelay -= 1200;
        this.properties.setWanderingTraderSpawnDelay(this.spawnDelay);

        if (this.spawnDelay > 0) {
            return;
        }

        this.spawnDelay = 24000;

        if (!world.getGameRules().getValue(GameRules.DO_MOB_SPAWNING)) {
            return;
        }

        int i = this.spawnChance;
        this.spawnChance = MathHelper.clamp(this.spawnChance + 25, 25, 75);
        this.properties.setWanderingTraderSpawnChance(this.spawnChance);

        if (this.random.nextInt(100) > i) {
            return;
        }

        if (this.trySpawn(world)) {
            this.spawnChance = 25;
        }
    }

    private boolean trySpawn(ServerWorld world) {
        PlayerEntity playerEntity = world.getRandomAlivePlayer();
        if (playerEntity == null) {
            return true;
        } else if (this.random.nextInt(10) != 0) {
            return false;
        } else {
            BlockPos blockPos = playerEntity.getBlockPos();
            // int i = 48; ????
            PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
            Optional<BlockPos> optional = pointOfInterestStorage.getPosition((poiType) -> poiType.matchesKey(PointOfInterestTypes.MEETING), (pos) -> true, blockPos, 48, OccupationStatus.ANY);
            BlockPos blockPos2 = optional.orElse(blockPos);
            BlockPos blockPos3 = this.getNearbySpawnPos(world, blockPos2, 48);
            if (blockPos3 != null && this.doesNotSuffocateAt(world, blockPos3)) {
                if (world.getBiome(blockPos3).isIn(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
                    return false;
                }

                RastaEntity RastaEntity = ModEntities.RASTA.spawn(world, blockPos3, SpawnReason.EVENT);
                Rootkz.LOGGER.info("SPAWNEO UNO!");
                if (RastaEntity != null) {
                    for(int j = 0; j < 2; ++j) {
                        this.spawnLlama(world, RastaEntity);
                    }

                    this.properties.setWanderingTraderId(RastaEntity.getUuid());
                    RastaEntity.setDespawnDelay(48000);
                    RastaEntity.setWanderTarget(blockPos2);
                    RastaEntity.setPositionTarget(blockPos2, 16);
                    return true;
                }
            }

            return false;
        }
    }

    private void spawnLlama(ServerWorld world, RastaEntity wanderingTrader) {
        BlockPos blockPos = this.getNearbySpawnPos(world, wanderingTrader.getBlockPos(), 4);
        if (blockPos != null) {
            TraderLlamaEntity traderLlamaEntity = EntityType.TRADER_LLAMA.spawn(world, blockPos, SpawnReason.EVENT);
            if (traderLlamaEntity != null) {
                traderLlamaEntity.attachLeash(wanderingTrader, true);
            }
        }
    }

    @Nullable
    private BlockPos getNearbySpawnPos(ServerWorld world, BlockPos pos, int range) {
        BlockPos blockPos = null;
        SpawnLocation spawnLocation = SpawnRestriction.getLocation(EntityType.WANDERING_TRADER);

        for(int i = 0; i < 10; ++i) {
            int j = pos.getX() + this.random.nextInt(range * 2) - range;
            int k = pos.getZ() + this.random.nextInt(range * 2) - range;
            int l = world.getTopY(Type.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            if (spawnLocation.isSpawnPositionOk(world, blockPos2, EntityType.WANDERING_TRADER)) {
                blockPos = blockPos2;
                break;
            }
        }

        return blockPos;
    }

    private boolean doesNotSuffocateAt(ServerWorld world, BlockPos pos) {
        for(BlockPos blockPos : BlockPos.iterate(pos, pos.add(1, 2, 1))) {
            if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
