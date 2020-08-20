package com.nikittta.platypus.entities;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.init.ModEntityTypes;
import com.nikittta.platypus.util.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class PlatypusEntity extends WolfEntity {

    //Platypus constructor or something
    public PlatypusEntity(EntityType<? extends WolfEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.setTamed(false);
    }

    @Override
    public boolean canSwim() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 8.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new AvoidEntityGoal(this, DrownedEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(7, new PlatypusEntity.EggBreedGoal(this, 1.0D));
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

    public final CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return PLATYPUS_AMBIENT_SOUND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return PLATYPUS_AMBIENT_SOUND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PLATYPUS_AMBIENT_SOUND;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {

        if (otherAnimal == this) {

            return false;

        } else if (!this.isTamed()) {

            return false;

        } else if (!(otherAnimal instanceof PlatypusEntity)) {

            return false;

        } else {

            PlatypusEntity platypusEntity = (PlatypusEntity)otherAnimal;

            if (!platypusEntity.isTamed()) {

                return false;

            } else if (platypusEntity.func_233684_eK_()) {

                return false;

            } else {

                return this.isInLove() && platypusEntity.isInLove();

            }
        }
    }

    public static final ResourceLocation PLATYPUS_AMBIENT_SOUND_RESOURCE = new ResourceLocation(Platypus.MOD_ID, "platypus_ambient");
    public static final SoundEvent PLATYPUS_AMBIENT_SOUND = new SoundEvent(PLATYPUS_AMBIENT_SOUND_RESOURCE);

    @Nullable
    @Override
    public PlatypusEntity createChild(AgeableEntity ageable) {
        PlatypusEntity platypusEntity = ModEntityTypes.PLATYPUS.get().create(this.world);

        if (this.getOwnerId() != null) {

            platypusEntity.setTamed(true);
            platypusEntity.setOwnerId(this.getOwnerId());

        }

        return platypusEntity;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) { return 10 + this.world.rand.nextInt(5); }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.MAX_HEALTH, 16D)
                .func_233815_a_(Attributes.MOVEMENT_SPEED, 0.25D)
                .func_233815_a_(Attributes.ATTACK_DAMAGE, 0.5D)
                .func_233815_a_(Attributes.FOLLOW_RANGE, 5D)
                .func_233815_a_(Attributes.ATTACK_SPEED, 4D);
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        Item item = itemstack.getItem();
        boolean isCrayfish = item == RegistryHandler.CRAYFISH.get();

        if (this.world.isRemote) {

            boolean flag = this.isOwner(player) || this.isTamed() || isCrayfish && !this.isTamed() && !this.func_233678_J__();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;

        } else {
            if (this.isTamed()) {

                if (isCrayfish && this.getHealth() < this.getMaxHealth()) {

                    if (!player.abilities.isCreativeMode) {

                        itemstack.shrink(1);

                    }

                    this.heal((float) item.getFood().getHealing());
                    return ActionResultType.SUCCESS;

                } else if (isCrayfish && this.getHealth() == this.getMaxHealth() && !this.isInLove()){

                    this.setInLove(player);

                }

                } else if (isCrayfish && !this.func_233678_J__()) {

                    if (!player.abilities.isCreativeMode) {

                        itemstack.shrink(1);

                    }

                    if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {

                        this.setTamedBy(player);
                        this.navigator.clearPath();
                        this.setAttackTarget((LivingEntity) null);
                        this.func_233687_w_(true);
                        this.world.setEntityState(this, (byte) 7);

                    } else {

                        this.world.setEntityState(this, (byte) 6);

                    }

                    return ActionResultType.SUCCESS;
                }

                return super.func_230254_b_(player, hand);

            }
        }



        private class EggBreedGoal extends BreedGoal {
            public EggBreedGoal(PlatypusEntity animal, double speedIn) {
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
