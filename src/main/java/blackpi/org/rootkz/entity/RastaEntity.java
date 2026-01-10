package blackpi.org.rootkz.entity;

import blackpi.org.rootkz.item.ModItems;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class RastaEntity extends WanderingTraderEntity {
    public RastaEntity(EntityType<? extends WanderingTraderEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void fillRecipes(ServerWorld world) {
        // Limpia las ofertas existentes llamando al método original
        super.fillRecipes(world);

        // Obtén la lista de ofertas y la limpias
        List<TradeOffer> offers = this.getOffers();
        offers.clear();

        // Agrega tus ofertas personalizadas
        offers.add(new TradeOffer(
                new TradedItem(Items.EMERALD, 25),
                new ItemStack(ModItems.WEED_SEED, 1),
                16, 2, 0.05f
        ));

        offers.add(new TradeOffer(
                new TradedItem(Items.EMERALD, 20),
                new ItemStack(ModItems.DUB_SONG_DISC, 1),
                16, 2, 0.05f
        ));

        offers.add(new TradeOffer(
                new TradedItem(Items.GOLD_INGOT, 5),
                new ItemStack(ModItems.JOINT, 10),
                16, 2, 0.05f
        ));

        offers.add(new TradeOffer(
                new TradedItem(Items.EMERALD, 1),
                new ItemStack(Items.PAPER, 10),
                16, 2, 0.05f
        ));
    }

    @Override
    protected void afterUsing(TradeOffer offer) {
        // Llama al método original para mantener el comportamiento de recompensas de experiencia
        super.afterUsing(offer);
    }

    public static DefaultAttributeContainer.Builder createVillagerAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MOVEMENT_SPEED, 0.5F)
                .add(EntityAttributes.MAX_HEALTH, 20.0F)
                .add(EntityAttributes.ATTACK_DAMAGE, 0.0F);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}