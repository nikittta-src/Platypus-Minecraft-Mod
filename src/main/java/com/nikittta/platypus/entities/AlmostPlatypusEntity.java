package com.nikittta.platypus.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class AlmostPlatypusEntity extends TameableEntity implements IAngerable {
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(AlmostPlatypusEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(AlmostPlatypusEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_234232_bz_ = EntityDataManager.createKey(AlmostPlatypusEntity.class, DataSerializers.VARINT);
    public static final Predicate<LivingEntity> TARGET_ENTITIES = (p_213440_0_) -> {
        EntityType<?> entitytype = p_213440_0_.getType();
        return entitytype == EntityType.SHEEP || entitytype == EntityType.RABBIT || entitytype == EntityType.FOX;
    };
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private static final RangedInteger field_234230_bG_ = TickRangeConverter.convertRange(20, 39);
    private UUID field_234231_bH_;

    public AlmostPlatypusEntity(EntityType<? extends AlmostPlatypusEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new AlmostPlatypusEntity.AvoidEntityGoal(this, LlamaEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGET_ENTITIES));
        this.targetSelector.addGoal(6, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.addGoal(8, new ResetAngerGoal<>(this, true));
    }

    public static AttributeModifierMap.MutableAttribute func_234233_eS_() {
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.MOVEMENT_SPEED, 0.3F).func_233815_a_(Attributes.MAX_HEALTH, 8.0D).func_233815_a_(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(BEGGING, false);
        this.dataManager.register(COLLAR_COLOR, DyeColor.RED.getId());
        this.dataManager.register(field_234232_bz_, 0);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        this.func_233682_c_(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }

        this.func_241358_a_((ServerWorld)this.world, compound);
    }

    protected SoundEvent getAmbientSound() {
        if (this.func_233678_J__()) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        } else if (this.rand.nextInt(3) == 0) {
            return this.isTamed() && this.getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.25F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example
     */
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, (byte)8);
        }

        if (!this.world.isRemote) {
            this.func_241359_a_((ServerWorld)this.world, true);
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.headRotationCourseOld = this.headRotationCourse;
            if (this.isBegging()) {
                this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
            } else {
                this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
            }

            if (this.isInWaterRainOrBubbleColumn()) {
                this.isWet = true;
                this.isShaking = false;
                this.timeWolfIsShaking = 0.0F;
                this.prevTimeWolfIsShaking = 0.0F;
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.timeWolfIsShaking == 0.0F) {
                    this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }

                this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
                this.timeWolfIsShaking += 0.05F;
                if (this.prevTimeWolfIsShaking >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.prevTimeWolfIsShaking = 0.0F;
                    this.timeWolfIsShaking = 0.0F;
                }

                if (this.timeWolfIsShaking > 0.4F) {
                    float f = (float)this.getPosY();
                    int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                    Vector3d vector3d = this.getMotion();

                    for(int j = 0; j < i; ++j) {
                        float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + (double)f1, f + 0.8F, this.getPosZ() + (double)f2, vector3d.x, vector3d.y, vector3d.z);
                    }
                }
            }

        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause) {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
        super.onDeath(cause);
    }

    /**
     * True if the wolf is wet
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isWolfWet() {
        return this.isWet;
    }

    /**
     * Used when calculating the amount of shading to apply while the wolf is wet.
     */
    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float p_70915_1_) {
        return 0.75F + MathHelper.lerp(p_70915_1_, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.25F;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float f = (MathHelper.lerp(p_70923_1_, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + p_70923_2_) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float p_70917_1_) {
        return MathHelper.lerp(p_70917_1_, this.headRotationCourseOld, this.headRotationCourse) * 0.15F * (float)Math.PI;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed() {
        return this.func_233684_eK_() ? 20 : super.getVerticalFaceSpeed();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();
            this.func_233687_w_(false);
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.func_233637_b_(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            this.setHealth(20.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
        Item item = itemstack.getItem();
        if (this.world.isRemote) {
            boolean flag = this.isOwner(p_230254_1_) || this.isTamed() || item == Items.BONE && !this.isTamed() && !this.func_233678_J__();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        } else {
            if (this.isTamed()) {
                if (this.isBreedingItem(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    this.heal((float)item.getFood().getHealing());
                    return ActionResultType.SUCCESS;
                }

                if (!(item instanceof DyeItem)) {
                    ActionResultType actionresulttype = super.func_230254_b_(p_230254_1_, p_230254_2_);
                    if ((!actionresulttype.isSuccessOrConsume() || this.isChild()) && this.isOwner(p_230254_1_)) {
                        this.func_233687_w_(!this.func_233685_eM_());
                        this.isJumping = false;
                        this.navigator.clearPath();
                        this.setAttackTarget(null);
                        return ActionResultType.SUCCESS;
                    }

                    return actionresulttype;
                }

                DyeColor dyecolor = ((DyeItem)item).getDyeColor();
                if (dyecolor != this.getCollarColor()) {
                    this.setCollarColor(dyecolor);
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    return ActionResultType.SUCCESS;
                }
            } else if (item == Items.BONE && !this.func_233678_J__()) {
                if (!p_230254_1_.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }

                if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, p_230254_1_)) {
                    this.setTamedBy(p_230254_1_);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    this.func_233687_w_(true);
                    this.world.setEntityState(this, (byte)7);
                } else {
                    this.world.setEntityState(this, (byte)6);
                }

                return ActionResultType.SUCCESS;
            }

            return super.func_230254_b_(p_230254_1_, p_230254_2_);
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public float getTailRotation() {
        if (this.func_233678_J__()) {
            return 1.5393804F;
        } else {
            return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
        }
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat
     */
    public boolean isBreedingItem(ItemStack stack) {
        Item item = stack.getItem();
        return item.isFood() && item.getFood().isMeat();
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    public int func_230256_F__() {
        return this.dataManager.get(field_234232_bz_);
    }

    public void func_230260_a__(int p_230260_1_) {
        this.dataManager.set(field_234232_bz_, p_230260_1_);
    }

    public void func_230258_H__() {
        this.func_230260_a__(field_234230_bG_.func_233018_a_(this.rand));
    }

    @Nullable
    public UUID func_230257_G__() {
        return this.field_234231_bH_;
    }

    public void func_230259_a_(@Nullable UUID p_230259_1_) {
        this.field_234231_bH_ = p_230259_1_;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.dataManager.get(COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor collarcolor) {
        this.dataManager.set(COLLAR_COLOR, collarcolor.getId());
    }

    public AlmostPlatypusEntity createChild(AgeableEntity ageable) {
        return null;
    }

    public void setBegging(boolean beg) {
        this.dataManager.set(BEGGING, beg);
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(AnimalEntity otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(otherAnimal instanceof AlmostPlatypusEntity)) {
            return false;
        } else {
            AlmostPlatypusEntity wolfentity = (AlmostPlatypusEntity)otherAnimal;
            if (!wolfentity.isTamed()) {
                return false;
            } else if (wolfentity.func_233684_eK_()) {
                return false;
            } else {
                return this.isInLove() && wolfentity.isInLove();
            }
        }
    }

    public boolean isBegging() {
        return this.dataManager.get(BEGGING);
    }

    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof AlmostPlatypusEntity) {
                AlmostPlatypusEntity wolfentity = (AlmostPlatypusEntity)target;
                return !wolfentity.isTamed() || wolfentity.getOwner() != owner;
            } else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
                return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
                return false;
            } else {
                return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
            }
        } else {
            return false;
        }
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return !this.func_233678_J__() && super.canBeLeashedTo(player);
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d func_241205_ce_() {
        return new Vector3d(0.0D, 0.6F * this.getEyeHeight(), this.getWidth() * 0.4F);
    }

    class AvoidEntityGoal<T extends LivingEntity> extends net.minecraft.entity.ai.goal.AvoidEntityGoal<T> {
        private final AlmostPlatypusEntity wolf;

        public AvoidEntityGoal(AlmostPlatypusEntity wolfIn, Class<T> entityClassToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
            super(wolfIn, entityClassToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
            this.wolf = wolfIn;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (super.shouldExecute() && this.avoidTarget instanceof LlamaEntity) {
                return !this.wolf.isTamed() && this.avoidLlama((LlamaEntity)this.avoidTarget);
            } else {
                return false;
            }
        }

        private boolean avoidLlama(LlamaEntity llamaIn) {
            return llamaIn.getStrength() >= AlmostPlatypusEntity.this.rand.nextInt(5);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            AlmostPlatypusEntity.this.setAttackTarget(null);
            super.startExecuting();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            AlmostPlatypusEntity.this.setAttackTarget(null);
            super.tick();
        }
    }
}