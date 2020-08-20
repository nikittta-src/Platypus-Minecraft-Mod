package com.nikittta.platypus.items;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Crayfish extends Item{

    public Crayfish() {
        super(new Item.Properties()
                .group(ItemGroup.FOOD)
                .food(new Food.Builder()
                        .hunger(2)
                        .saturation(2F)
                        .fastToEat()
                        .meat()
                        .build()
                )
        );
    }
}
