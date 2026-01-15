package blackpi.org.rootkz.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.function.Supplier;

public class WeedCropBlock extends CropBlock {

    public static final IntProperty AGE = Properties.AGE_7;

    // Shapes SEGURAS (nunca bloque completo, nunca 16px)
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            // Edad 0 – brote
            Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 2.0, 10.0),

            // Edad 1
            Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 4.0, 11.0),

            // Edad 2
            Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 6.0, 12.0),

            // Edad 3
            Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 8.0, 13.0),

            // Edad 4
            Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 10.0, 14.0),

            // Edad 5
            Block.createCuboidShape(1.5, 0.0, 1.5, 14.5, 12.0, 14.5),

            // Edad 6 (IMPORTANTE: < 16px)
            Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0),

            // Edad 7 – madura (con cogollo)
            VoxelShapes.union(
                    Block.createCuboidShape(1.5, 0.0, 1.5, 14.5, 10.0, 14.5),
                    Block.createCuboidShape(4.0, 10.0, 4.0, 12.0, 15.5, 12.0)
            )
    };

    private final Supplier<ItemConvertible> seedSupplier;

    public WeedCropBlock(Settings settings, Supplier<ItemConvertible> seedSupplier) {
        super(settings);
        this.seedSupplier = seedSupplier;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return seedSupplier.get();
    }

    // Shape visual (outline)
    @Override
    public VoxelShape getOutlineShape(
            BlockState state,
            BlockView world,
            BlockPos pos,
            ShapeContext context
    ) {
        return AGE_TO_SHAPE[state.get(getAgeProperty())];
    }

    // Colisión VACÍA (vanilla crops)
    @Override
    public VoxelShape getCollisionShape(
            BlockState state,
            BlockView world,
            BlockPos pos,
            ShapeContext context
    ) {
        return VoxelShapes.empty();
    }
}
