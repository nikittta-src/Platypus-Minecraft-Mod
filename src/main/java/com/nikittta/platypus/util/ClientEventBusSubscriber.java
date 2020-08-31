package com.nikittta.platypus.util;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.client.render.PerryRenderer;
import com.nikittta.platypus.client.render.PlatypusRenderer;
import com.nikittta.platypus.entities.PlatypusEntity;
import com.nikittta.platypus.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof WolfEntity && !(entity instanceof PlatypusEntity)){

        }
    }

}
