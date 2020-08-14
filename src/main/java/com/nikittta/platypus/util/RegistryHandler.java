package com.nikittta.platypus.util;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.items.ItemBase;
import com.nikittta.platypus.items.PlatypusEggItem;
import com.nikittta.platypus.tools.ModToolsTier;
import com.nikittta.platypus.tools.PlatypusSword;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Platypus.MOD_ID);

    public static void init(){

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> PLATYPUS_CLAW = ITEMS.register("platypus_claw", ItemBase::new);

    public static final RegistryObject<Item> PLATYPUS_EGG = ITEMS.register("platypus_egg", PlatypusEggItem::new);

    public static final RegistryObject<SwordItem> PLATYPUS_SWORD = ITEMS.register("platypus_sword",
            () -> new PlatypusSword(new ModToolsTier(), 0, -1, new Item.Properties().group(ItemGroup.COMBAT)));
}
