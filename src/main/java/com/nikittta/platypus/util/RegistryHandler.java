package com.nikittta.platypus.util;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.init.ModEntityTypes;
import com.nikittta.platypus.items.Crayfish;
import com.nikittta.platypus.items.ModSpawnEggItem;
import com.nikittta.platypus.items.PlatypusEggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Platypus.MOD_ID);

    public static void init(){

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Crayfish> CRAYFISH = ITEMS.register("crayfish", Crayfish::new);

    public static final RegistryObject<Item> PLATYPUS_EGG = ITEMS.register("platypus_egg", PlatypusEggItem::new);

    public static final RegistryObject<ModSpawnEggItem> PLATYPUS_SPAWN_EGG = ITEMS.register("platypus_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.PLATYPUS,
                    0xffd59e, 0x694b24, new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<ModSpawnEggItem> PERRY_SPAWN_EGG = ITEMS.register("perry_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.PERRY,
                    0x289395, 0xff6714, new Item.Properties().group(ItemGroup.MISC)));

}
