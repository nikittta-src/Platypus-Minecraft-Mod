package com.nikittta.platypus;

import com.nikittta.platypus.entities.PerryEntity;
import com.nikittta.platypus.entities.PlatypusEntity;
import com.nikittta.platypus.init.ModEntityTypes;
import com.nikittta.platypus.util.RegistryHandler;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("platypus")
public class Platypus
{
    public static final Logger LOGGER = LogManager.getLogger();
    public  static final String MOD_ID = "platypus";


    public Platypus() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModEntityTypes.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityTypes.PLATYPUS.get(), PlatypusEntity.setCustomAttributes().func_233813_a_());
            GlobalEntityTypeAttributes.put(ModEntityTypes.PERRY.get(), PerryEntity.setCustomAttributes().func_233813_a_());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) { }
}
