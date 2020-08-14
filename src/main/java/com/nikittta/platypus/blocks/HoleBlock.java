package com.nikittta.platypus.blocks;

import com.nikittta.platypus.entities.PlatypusEntity;
import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;


// Platypuses were supposed to lay eggs, just like turtles do, but they lay them in holes.
// This block was gonna behave the way TurtleEggBlock does, but it appeared to be too
// difficult for me to implement it.

public class HoleBlock extends TurtleEggBlock {
    public HoleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        PlatypusEntity platypusEntity = ModEntityTypes.PLATYPUS.get().create(worldIn);
        platypusEntity.setGrowingAge(-24000);
        platypusEntity.setLocationAndAngles((double)pos.getX() + 0.3D * 0.2D, (double)pos.getY(), (double)pos.getZ() + 0.3D, 0.0F, 0.0F);
        worldIn.addEntity(platypusEntity);
    }
}