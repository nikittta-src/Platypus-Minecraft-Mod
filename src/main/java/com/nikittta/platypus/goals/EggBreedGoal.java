package com.nikittta.platypus.goals;

import com.nikittta.platypus.entities.PlatypusEntity;
import com.nikittta.platypus.util.RegistryHandler;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class EggBreedGoal extends BreedGoal {
    public EggBreedGoal(PlatypusEntity animal, double speedIn) {
        super(animal, speedIn);
    }

    @Override
    protected void spawnBaby() {
        BlockPos eggPos = this.animal.func_233580_cy_().add(1,0,0);
        world.playSound(null, this.animal.func_233580_cy_(), SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.rand.nextFloat() * 0.2F);
        world.setBlockState(eggPos, RegistryHandler.PLATYPUS_EGG.get().getDefaultState());
        world.getBlockState(eggPos).getBlock();
        ((PlatypusEntity)animal).setHasEgg(true);
        ((PlatypusEntity)animal).setEggPos(eggPos);
        this.animal.resetInLove();
    }
}
