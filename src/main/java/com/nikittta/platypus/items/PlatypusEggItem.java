package com.nikittta.platypus.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class PlatypusEggItem extends BlockItem {

    public PlatypusEggItem(Block block) {
        super(block, new Item.Properties().group(ItemGroup.MISC));
    }
}
