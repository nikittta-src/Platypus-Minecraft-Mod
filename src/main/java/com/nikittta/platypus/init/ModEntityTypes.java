package com.nikittta.platypus.init;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.entities.PlatypusEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Platypus.MOD_ID);

    public static final RegistryObject<EntityType<PlatypusEntity>> PLATYPUS = ENTITY_TYPES.register("platypus",
            () -> EntityType.Builder
                    .create(PlatypusEntity::new, EntityClassification.CREATURE)
                    .size(1, 1)
                    .build(new ResourceLocation(Platypus.MOD_ID, "platypus").toString()));

}
