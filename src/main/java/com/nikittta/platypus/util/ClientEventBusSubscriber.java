package com.nikittta.platypus.util;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.client.render.PerryRenderer;
import com.nikittta.platypus.client.render.PlatypusRenderer;
import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Platypus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PLATYPUS.get(), PlatypusRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PERRY.get(),  PerryRenderer::new);
    }

}
