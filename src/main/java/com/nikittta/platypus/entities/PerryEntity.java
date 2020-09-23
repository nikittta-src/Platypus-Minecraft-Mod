package com.nikittta.platypus.entities;

import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class PerryEntity extends PlatypusEntity {

    public PerryEntity(EntityType<? extends PlatypusEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    @Override
    public PerryEntity createChild(AgeableEntity ageable) {
        PerryEntity perryEntity = ModEntityTypes.PERRY.get().create(this.world);
        if (this.getOwnerId() != null) {
            perryEntity.setTamed(true);
            perryEntity.setOwnerId(this.getOwnerId());
        }

        return perryEntity;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) { return 20 + this.world.rand.nextInt(30); }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.MAX_HEALTH, 16D)
                .func_233815_a_(Attributes.MOVEMENT_SPEED, 0.4D)
                .func_233815_a_(Attributes.ATTACK_DAMAGE, 3D)
                .func_233815_a_(Attributes.FOLLOW_RANGE, 6D)
                .func_233815_a_(Attributes.ATTACK_SPEED, 3D);
    }

    }
