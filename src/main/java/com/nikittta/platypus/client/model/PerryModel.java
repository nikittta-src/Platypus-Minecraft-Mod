package com.nikittta.platypus.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nikittta.platypus.entities.PerryEntity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class PerryModel <T extends PerryEntity> extends PlatypusModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer body_rotation;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;
    private final ModelRenderer leg4;
    private final ModelRenderer tail;
    private final ModelRenderer tail_rotation;

    public PerryModel() {
        textureWidth = 64;
        textureHeight = 64;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 18.5F, -3.0F);
        head.setTextureOffset(24, 0).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
        head.setTextureOffset(0, 35).addBox(-3.0F, -7.0F, -4.0F, 6.0F, 2.0F, 4.0F, 0.0F, false);
        head.setTextureOffset(0, 42).addBox(-5.0F, -5.0F, -6.0F, 10.0F, 1.0F, 8.0F, 0.0F, false);
        head.setTextureOffset(0, 17).addBox(-3.0F, 0.0F, -9.0F, 6.0F, 1.0F, 4.0F, 0.0F, false);
        head.setTextureOffset(0, 22).addBox(-2.0F, 1.0F, -7.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 19.5F, 3.5F);


        body_rotation = new ModelRenderer(this);
        body_rotation.setRotationPoint(0.0F, 0.5F, 2.5F);
        body.addChild(body_rotation);
        setRotationAngle(body_rotation, 0.0F, 0.0F, 0.0F);
        body_rotation.setTextureOffset(0, 0).addBox(-3.0F, -6.0F, -5.5F, 6.0F, 11.0F, 6.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setRotationPoint(-2.0F, 22.5F, 7.0F);
        leg1.setTextureOffset(0, 31).addBox(-1.001F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        leg1.setTextureOffset(20, 27).addBox(-1.001F, 0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        leg2 = new ModelRenderer(this);
        leg2.setRotationPoint(2.0F, 22.5F, 7.0F);
        leg2.setTextureOffset(10, 27).addBox(-0.999F, 0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        leg2.setTextureOffset(30, 27).addBox(-1.001F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        leg3 = new ModelRenderer(this);
        leg3.setRotationPoint(-2.0F, 22.5F, 0.0F);
        leg3.setTextureOffset(0, 27).addBox(-1.001F, 0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        leg3.setTextureOffset(8, 31).addBox(-1.001F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        leg4 = new ModelRenderer(this);
        leg4.setRotationPoint(2.0F, 22.5F, 0.0F);
        leg4.setTextureOffset(30, 17).addBox(-0.999F, 0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        leg4.setTextureOffset(16, 31).addBox(-1.001F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, 16.0F, 10.0F);


        tail_rotation = new ModelRenderer(this);
        tail_rotation.setRotationPoint(0.0F, -0.5F, 1.5F);
        tail.addChild(tail_rotation);
        setRotationAngle(tail_rotation, 1.5708F, 0.0F, 0.0F);
        tail_rotation.setTextureOffset(20, 17).addBox(-2.0F, -3.5F, -2.5F, 4.0F, 9.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.leg1, this.leg2, this.leg3, this.leg4, this.body, this.tail);
    }

    @Override
    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {

        if (entityIn.func_233684_eK_()) {
            this.head.rotationPointY = 21.5f;
            this.body.rotationPointY = 21.5f;
            this.tail.rotationPointY = 18.0f;
            this.leg1.setRotationPoint(-4.0F, 22.5F, 7.0F);
            this.leg2.setRotationPoint(4.0F, 22.5F, 7.0F);
            this.leg3.setRotationPoint(-4.0F, 22.5F, 0.0F);
            this.leg4.setRotationPoint(4.0F, 22.5F, 0.0F);
        } else {
            this.head.rotationPointY = 18.5f;
            this.body.rotationPointY = 19.5f;
            this.tail.rotationPointY = 16.0f;
            this.leg1.setRotationPoint(-2.0F, 22.5F, 7.0F);
            this.leg2.setRotationPoint(2.0F, 22.5F, 7.0F);
            this.leg3.setRotationPoint(-2.0F, 22.5F, -1.0F);
            this.leg4.setRotationPoint(2.0F, 22.5F, -1.0F);
        }

        this.body.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.16F);
        this.tail_rotation.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.2F);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

