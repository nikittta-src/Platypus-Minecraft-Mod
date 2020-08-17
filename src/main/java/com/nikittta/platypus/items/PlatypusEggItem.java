package com.nikittta.platypus.items;

import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class PlatypusEggItem extends Item {

    public PlatypusEggItem() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemstack = context.getItem();
            BlockPos blockpos = context.getPos();
            Direction direction = context.getFace();
            BlockState blockstate = world.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {

                blockpos1 = blockpos;

            } else {

                blockpos1 = blockpos.offset(direction);

            }

            EntityType<?> entitytype = ModEntityTypes.PLATYPUS.get();
            if (new Random().nextInt(30) == 12){

                entitytype = ModEntityTypes.PERRY.get();

            }
            if (entitytype.spawn(world, itemstack, context.getPlayer(), blockpos1, SpawnReason.BREEDING, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {

                itemstack.shrink(1);

            }

            return ActionResultType.CONSUME;
        }
    }
}
