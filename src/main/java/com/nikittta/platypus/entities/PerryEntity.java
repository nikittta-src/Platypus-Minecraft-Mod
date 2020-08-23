package com.nikittta.platypus.entities;

import com.nikittta.platypus.init.ModEntityTypes;
import com.nikittta.platypus.util.RegistryHandler;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

import java.util.Random;

public class PerryEntity extends PlatypusEntity {

    public PerryEntity(EntityType<? extends WolfEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 8.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new AvoidEntityGoal(this, DrownedEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(7, new PerryEntity.EggBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new TemptGoal(this, 1.0f, Ingredient.fromItems(Items.SALMON), false));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SalmonEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(8, new ResetAngerGoal<>(this, true));
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

        private class EggBreedGoal extends BreedGoal {
            public EggBreedGoal(PerryEntity animal, double speedIn) {
                super(animal, speedIn);
            }

            @Override
            protected void spawnBaby() {
                for (int i = 0; i < new Random().nextInt(3); i++ ) {
                    this.animal.entityDropItem(RegistryHandler.PLATYPUS_EGG.get());
                }
                this.animal.resetInLove();
            }
        }

    }
