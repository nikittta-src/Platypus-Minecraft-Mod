package com.nikittta.platypus.client.render;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.client.model.PlatypusModel;
import com.nikittta.platypus.entities.PlatypusEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class PlatypusRenderer extends MobRenderer<PlatypusEntity, PlatypusModel<PlatypusEntity>> {

    protected static final ResourceLocation PERRY = new ResourceLocation(Platypus.MOD_ID, "textures/entity/perry.png");
    protected static final ResourceLocation PLATYPUS = new ResourceLocation(Platypus.MOD_ID, "textures/entity/platypus.png");

    public PlatypusRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PlatypusModel<>(), 0.3f);
    }

    @Override
    public ResourceLocation getEntityTexture(PlatypusEntity entity) {
        if (entity.isPerry()){
            return PERRY;
        }else{
            return PLATYPUS;
        }
    }
}
