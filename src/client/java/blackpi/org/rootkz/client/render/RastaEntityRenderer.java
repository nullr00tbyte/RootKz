package blackpi.org.rootkz.client.render;

import blackpi.org.rootkz.Rootkz;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.VillagerEntityRenderState;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.util.Identifier;



@Environment(EnvType.CLIENT)
public class RastaEntityRenderer extends MobEntityRenderer<WanderingTraderEntity, VillagerEntityRenderState, VillagerResemblingModel> {
    private static final Identifier TEXTURE = Identifier.of(Rootkz.MOD_ID,"textures/entity/rasta_man.png");

    public RastaEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new VillagerResemblingModel(context.getPart(EntityModelLayers.WANDERING_TRADER)), 0.5F);
        this.addFeature(new HeadFeatureRenderer<>(
                this,                               // FeatureRendererContext
                context.getEntityModels(),          // LoadedEntityModels
                context.getPlayerSkinCache()              // PlayerSkinCache
        ));
        this.addFeature(new VillagerHeldItemFeatureRenderer<>(this));
    }

    public Identifier getTexture(VillagerEntityRenderState villagerEntityRenderState) {
        return TEXTURE;
    }

    public VillagerEntityRenderState createRenderState() {
        return new VillagerEntityRenderState();
    }

    public void updateRenderState(WanderingTraderEntity wanderingTraderEntity, VillagerEntityRenderState villagerEntityRenderState, float f) {
        super.updateRenderState(wanderingTraderEntity, villagerEntityRenderState, f);
        ItemHolderEntityRenderState.update(wanderingTraderEntity, villagerEntityRenderState, this.itemModelResolver);
        villagerEntityRenderState.headRolling = wanderingTraderEntity.getHeadRollingTimeLeft() > 0;
    }
}
