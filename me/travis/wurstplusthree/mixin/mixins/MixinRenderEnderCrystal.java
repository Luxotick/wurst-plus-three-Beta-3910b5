/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.mixin.mixins;

import java.awt.Color;
import me.travis.wurstplusthree.event.events.RenderEntityModelEvent;
import me.travis.wurstplusthree.hack.hacks.render.CrystalRender;
import me.travis.wurstplusthree.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
    @Shadow
    @Final
    private ModelBase modelEnderCrystalNoBase;
    @Shadow
    @Final
    private ModelBase modelEnderCrystal;
    private static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (CrystalRender.INSTANCE.isEnabled()) {
            GlStateManager.scale((float)CrystalRender.INSTANCE.scale.getValue().floatValue(), (float)CrystalRender.INSTANCE.scale.getValue().floatValue(), (float)CrystalRender.INSTANCE.scale.getValue().floatValue());
        }
        if (CrystalRender.INSTANCE.isEnabled() && CrystalRender.INSTANCE.wireframe.getValue().booleanValue()) {
            RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            CrystalRender.INSTANCE.onRenderModel(event);
        }
        if (CrystalRender.INSTANCE.isEnabled() && CrystalRender.INSTANCE.chams.getValue().booleanValue()) {
            Color visibleColor;
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            if (CrystalRender.INSTANCE.xqz.getValue().booleanValue() && CrystalRender.INSTANCE.throughwalls.getValue().booleanValue()) {
                Color hiddenColor = EntityUtil.getColor(entity, CrystalRender.INSTANCE.hiddenColour.getValue().getRed(), CrystalRender.INSTANCE.hiddenColour.getValue().getGreen(), CrystalRender.INSTANCE.hiddenColour.getValue().getBlue(), CrystalRender.INSTANCE.hiddenColour.getValue().getAlpha(), true);
                visibleColor = EntityUtil.getColor(entity, CrystalRender.INSTANCE.colour.getValue().getRed(), CrystalRender.INSTANCE.colour.getValue().getGreen(), CrystalRender.INSTANCE.colour.getValue().getBlue(), CrystalRender.INSTANCE.colour.getValue().getAlpha(), true);
                if (CrystalRender.INSTANCE.throughwalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)hiddenColor.getRed() / 255.0f), (float)((float)hiddenColor.getGreen() / 255.0f), (float)((float)hiddenColor.getBlue() / 255.0f), (float)((float)visibleColor.getAlpha() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalRender.INSTANCE.throughwalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
                GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)visibleColor.getAlpha() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                visibleColor = EntityUtil.getColor(entity, CrystalRender.INSTANCE.colour.getValue().getRed(), CrystalRender.INSTANCE.colour.getValue().getGreen(), CrystalRender.INSTANCE.colour.getValue().getBlue(), CrystalRender.INSTANCE.colour.getValue().getAlpha(), true);
                if (CrystalRender.INSTANCE.throughwalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)visibleColor.getAlpha() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalRender.INSTANCE.throughwalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
            }
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
            if (CrystalRender.INSTANCE.glint.getValue().booleanValue()) {
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GlStateManager.enableAlpha();
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)0.13f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.disableAlpha();
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
            }
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (CrystalRender.INSTANCE.isEnabled()) {
            GlStateManager.scale((float)(1.0f / CrystalRender.INSTANCE.scale.getValue().floatValue()), (float)(1.0f / CrystalRender.INSTANCE.scale.getValue().floatValue()), (float)(1.0f / CrystalRender.INSTANCE.scale.getValue().floatValue()));
        }
    }

    @Inject(method={"doRender"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V")})
    public void renderEffect(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (CrystalRender.INSTANCE.effect.getValue().booleanValue()) {
            float f = (float)entity.ticksExisted + partialTicks;
            float r = (float)entity.innerRotation + partialTicks;
            float r1 = MathHelper.sin((float)(r * 0.2f)) / 2.0f + 0.5f;
            r1 = r1 * r1 + r1;
            Minecraft.getMinecraft().renderEngine.bindTexture(ENCHANTED_ITEM_GLINT_RES);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc((int)514);
            GlStateManager.depthMask((boolean)false);
            GlStateManager.color((float)0.5f, (float)0.5f, (float)0.5f, (float)1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
                GlStateManager.color((float)0.38f, (float)0.19f, (float)0.608f, (float)1.0f);
                GlStateManager.matrixMode((int)5890);
                GlStateManager.loadIdentity();
                GlStateManager.scale((float)0.33333334f, (float)0.33333334f, (float)0.33333334f);
                GlStateManager.rotate((float)(30.0f - (float)i * 60.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.translate((float)0.0f, (float)(f * (0.001f + (float)i * 0.003f) * 20.0f), (float)0.0f);
                GlStateManager.matrixMode((int)5888);
                GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            }
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.depthFunc((int)515);
            GlStateManager.disableBlend();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }
    }
}

