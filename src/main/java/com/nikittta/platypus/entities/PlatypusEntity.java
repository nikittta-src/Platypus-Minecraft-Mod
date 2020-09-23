package com.nikittta.platypus.entities;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.goals.EggBreedGoal;
import com.nikittta.platypus.init.ModEntityTypes;
import com.nikittta.platypus.util.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PlatypusEntity extends AlmostPlatypusEntity {

    private boolean eggHit = false;

    @Nullable
    public PlayerEntity theOneWhoAttackedMyEgg = null;
    private boolean isAngry;

    public void notifyHit(PlayerEntity player){
        if (eggHit) {
            this.eggHit = false;
            this.theOneWhoAttackedMyEgg = player;
            Platypus.LOGGER.info("Someone just touched my egg, so I'll kill him");
        } else {
            this.eggHit = true;
        }

    }

    public boolean wasEggHit(){
        return this.eggHit;
    }

    private boolean hasEgg = false;
    @Nullable
    private BlockPos eggPos = null;

    public PlatypusEntity(EntityType<? extends AlmostPlatypusEntity> type, World worldIn) {
        super(type, worldIn);
        setTamed(false);
    }

    public BlockPos getEggPos(){
        return this.eggPos;
    }

    public void setEggPos(BlockPos _eggPos){
        this.eggPos = _eggPos;
    }

    public boolean hasEgg(){
        return this.hasEgg;
    }

    public void setHasEgg(boolean hasOrNot){
        this.hasEgg = hasOrNot;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (entityIn instanceof LivingEntity){
            if (entityIn instanceof MonsterEntity){
                ((MonsterEntity) entityIn).addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH));
            } else {
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 200));
            }
        }
        return super.attackEntityAsMob(entityIn);
    }

    @Override
    public boolean canSwim() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0,  Ingredient.fromItems(Items.SALMON, RegistryHandler.CRAYFISH.get()), false));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(this, DrownedEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new EggBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(8, new ResetAngerGoal<>(this, true));}

    public final CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.rand.nextBoolean() ? PLATYPUS_AMBIENT_SOUND : null;
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

                if (item == Items.GLASS_BOTTLE && this.getHealth() == this.getMaxHealth()){
                    ItemStack potion = new ItemStack(Items.POTION);
                    PotionUtils.addPotionToItemStack(potion, Potions.POISON);
                    player.addItemStackToInventory(potion);
                    itemstack.shrink(1);
                }

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
                    this.setAttackTarget(null);
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

    public void setAngry(boolean angry){
        this.isAngry = angry;
    }

    public boolean isAngry() {
        return isAngry;
    }
}
