package com.nikittta.platypus.client.render;

import com.nikittta.platypus.Platypus;
import com.nikittta.platypus.client.model.PerryModel;
import com.nikittta.platypus.client.model.PlatypusModel;
import com.nikittta.platypus.entities.PerryEntity;
import com.nikittta.platypus.entities.PlatypusEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class PerryRenderer extends MobRenderer<PerryEntity, PerryModel<PerryEntity>> {

    protected static final ResourceLocation PERRY = new ResourceLocation(Platypus.MOD_ID, "textures/entity/perry.png");

    public PerryRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PerryModel<>(), 0.3f);
    }

    @Override
    public ResourceLocation getEntityTexture(PerryEntity entity) {
        return PERRY;
    }

}
