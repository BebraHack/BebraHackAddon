package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.block.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class OldAnvil extends Module {
    public OldAnvil() {
        super(Addon.misc, "old-anvil", "Allows you to move while you burrowed with anvil. Tyrannys <3");
    }

    private final VoxelShape BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private final VoxelShape X_STEP_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, 4.0D, 14.0D);
    private final VoxelShape X_STEM_SHAPE = Block.createCuboidShape(0.0D, 5.0D, 2.0D, 16.0D, 10.0D, 14.0D);
    private final VoxelShape X_FACE_SHAPE = Block.createCuboidShape(0.0D, 10.0D, 2.0D, 16.0D, 16.0D, 14.0D);
    private final VoxelShape Z_STEP_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 4.0D, 16.0D);
    private final VoxelShape Z_STEM_SHAPE = Block.createCuboidShape(2.0D, 5.0D, 0.0D, 14.0D, 10.0D, 16.0D);
    private final VoxelShape Z_FACE_SHAPE = Block.createCuboidShape(2.0D, 10.0D, 0.0D, 14.0D, 16.0D, 16.0D);
    private final VoxelShape X_AXIS_SHAPE = VoxelShapes.union(BASE_SHAPE, X_STEP_SHAPE, X_STEM_SHAPE, X_FACE_SHAPE);
    private final VoxelShape Z_AXIS_SHAPE = VoxelShapes.union(BASE_SHAPE, Z_STEP_SHAPE, Z_STEM_SHAPE, Z_FACE_SHAPE);

    public VoxelShape voxelShape(BlockState state) {
        Direction direction = state.get(HorizontalFacingBlock.FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_SHAPE : Z_AXIS_SHAPE;
    }
}
