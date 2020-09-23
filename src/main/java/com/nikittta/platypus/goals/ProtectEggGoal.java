package com.nikittta.platypus.goals;

import com.nikittta.platypus.entities.PlatypusEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;

import java.util.List;

public class ProtectEggGoal extends Goal {

    private static final EntityPredicate entityPredicate = (new EntityPredicate()).setDistance(3.0D);

    private PlatypusEntity platypus = null;

    public ProtectEggGoal(PlatypusEntity platypus){
        this.platypus  = platypus;
    }

    @Override
    public boolean shouldExecute() {
        return platypus.hasEgg()
                && platypus.getEggPos() != null
                && platypus.wasEggHit();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return platypus.hasEgg()
                && platypus.getEggPos() != null;
    }

    @Override
    public void tick() {
        if (!this.platypus.getEggPos().withinDistance(this.platypus.getPositionVec(), 3)){
            this.platypus.getNavigator().tryMoveToXYZ(this.platypus.getEggPos().getX(),
                    this.platypus.getEggPos().getY(),
                    this.platypus.getEggPos().getZ(),
                    1.0
                    );
        } else {
            List<LivingEntity> hostileEntities = getNearbyHostileEntities();
            if (!hostileEntities.isEmpty()){
                this.platypus.setAngry(true);
            }
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.platypus.setAngry(false);
    }

    private List<LivingEntity> getNearbyHostileEntities(){

        List<LivingEntity> list = this.platypus.getEntityWorld().getTargettableEntitiesWithinAABB(LivingEntity.class,
                entityPredicate,
                this.platypus,
                this.platypus.getBoundingBox().grow(3.0D));

        List<LivingEntity> hostileEntities = List.of();

        for (LivingEntity entity : list){
            if (entity.equals(this.platypus.getOwner())){
                continue;
            }
            if (((TameableEntity)entity).getOwner().equals(this.platypus.getOwner())){
                continue;
            }
            if (entity instanceof PlatypusEntity){
                continue;
            }
            hostileEntities.add(entity);
        }

        return hostileEntities;
    }
}
