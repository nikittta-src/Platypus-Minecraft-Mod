package com.nikittta.platypus.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class PlatypusSword extends SwordItem {
    public PlatypusSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties p_i48460_4_) {
        super(tier, attackDamageIn, attackSpeedIn, p_i48460_4_);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (p_220045_0_) -> {
            p_220045_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        if (target.isPotionApplicable(new EffectInstance(Effects.POISON, 200))){
            target.addPotionEffect(new EffectInstance(Effects.POISON, 200));
        }else{
            target.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200));
        }

        return true;
    }
}
