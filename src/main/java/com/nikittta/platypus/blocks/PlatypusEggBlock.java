package com.nikittta.platypus.blocks;

import com.nikittta.platypus.entities.PlatypusEntity;
import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.stream.Stream;

public class PlatypusEggBlock extends Block {

    protected static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(6, 8, 6, 10, 9, 10),
            Block.makeCuboidShape(5, 6, 5, 11, 8, 11),
            Block.makeCuboidShape(4, 1, 4, 12, 6, 12),
            Block.makeCuboidShape(5, 0, 5, 11, 1, 11)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public PlatypusEggBlock() {
        super(Properties.create(Material.DRAGON_EGG)
                        .harvestLevel(0)
                        .noDrops()
                        .sound(SoundType.STONE)
                        .tickRandomly()
                        .zeroHardnessAndResistance()
            );
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {

                worldIn.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldIn.removeBlock(pos, false);

                worldIn.playEvent(2001, pos, Block.getStateId(state));
                PlatypusEntity platypusEntity = ModEntityTypes.PLATYPUS.get().create(worldIn);
                if (random.nextInt(20) == 8){
                    platypusEntity = ModEntityTypes.PERRY.get().create(worldIn);
                }
                platypusEntity.setGrowingAge(-24000);
                platypusEntity.setLocationAndAngles((double)pos.getX() + 0.3D + (double)1 * 0.2D, pos.getY(), (double)pos.getZ() + 0.3D, 0.0F, 0.0F);
                worldIn.addEntity(platypusEntity);

    }

}
