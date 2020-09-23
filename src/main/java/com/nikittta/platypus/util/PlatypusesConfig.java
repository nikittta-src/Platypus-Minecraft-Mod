package com.nikittta.platypus.util;

import com.nikittta.platypus.Platypus;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Platypus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlatypusesConfig {

    public static class Common {

        public final ForgeConfigSpec.IntValue randomlySpawnPerry;

        public Common(ForgeConfigSpec.Builder builder){
            builder.push("Platypuses");

            randomlySpawnPerry = builder.comment("Perry the platypus spawn possibility")
            .worldRestart()
            .defineInRange("perry_spawn_chance", 5, 0, 100);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void configLoad(final ModConfig.Loading event){

    }

    @SubscribeEvent
    public static void configReLoad(final ModConfig.Reloading event){

    }

}
