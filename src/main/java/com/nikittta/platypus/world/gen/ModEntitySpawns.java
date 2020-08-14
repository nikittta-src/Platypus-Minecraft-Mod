package com.nikittta.platypus.world.gen;


import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Platypus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntitySpawns {

    public static Boolean isAppropriate(Biome biome){
        if ((biome.getCategory() == Biome.Category.SWAMP) | (biome.getCategory() == Biome.Category.RIVER) | (biome.getCategory() == Biome.Category.ICY)){
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void spawnEntities(FMLLoadCompleteEvent event){
        for (Biome biome : ForgeRegistries.BIOMES){
            if (isAppropriate(biome)){
                biome.getSpawns(EntityClassification.CREATURE)
                        .add(new Biome.SpawnListEntry(ModEntityTypes.PLATYPUS.get(), 6, 1, 3));
            }
        }
    }



}
