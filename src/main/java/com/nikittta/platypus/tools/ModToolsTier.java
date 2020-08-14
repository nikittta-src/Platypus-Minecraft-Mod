package com.nikittta.platypus.tools;

import com.nikittta.platypus.util.RegistryHandler;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ModToolsTier implements IItemTier {

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchatability;
    private final Supplier<Ingredient> repairMaterial;

    public ModToolsTier(){
        this.harvestLevel = 0;
        this.attackDamage = 0;
        this.efficiency = 6;
        this.enchatability = 12;
        this.maxUses = 33;
        this.repairMaterial = () -> {return Ingredient.fromItems(RegistryHandler.PLATYPUS_CLAW.get());};
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return enchatability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return repairMaterial.get();
    }
}
