/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.mixin.mixins;

import java.awt.Color;
import me.travis.wurstplusthree.hack.hacks.render.Chams;
import me.travis.wurstplusthree.util.ColorUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase {
    @Shadow
    protected ModelBase mainModel;

    @Inject(method={"renderModel"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V")}, cancellable=true)
    private void renderModel(EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo info) {
        Chams chams = Chams.INSTANCE;
        if (!chams.isEnabled()) {
            return;
        }
        if (entityLivingBase instanceof EntityOtherPlayerMP && chams.players.getValue() != false && !chams.pops.containsKey(entityLivingBase.getEntityId()) || entityLivingBase instanceof EntityPlayerSP && chams.players.getValue() != false && chams.local.getValue() != false || (EntityUtil.isPassiveMob((Entity)entityLivingBase) || EntityUtil.isNeutralMob((Entity)entityLivingBase)) && chams.mobs.getValue() != false || EntityUtil.isHostileMob((Entity)entityLivingBase) && chams.monsters.getValue().booleanValue()) {
            if (!chams.texture.getValue().booleanValue()) {
                info.cancel();
            }
            if (chams.transparent.getValue().booleanValue()) {
                GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            if (!chams.texture.getValue().booleanValue() && !chams.mode.is("Shine")) {
                GL11.glDisable((int)3553);
            }
            if (chams.blend.getValue().booleanValue()) {
                GL11.glEnable((int)3042);
            }
            if (chams.lighting.getValue().booleanValue()) {
                GL11.glDisable((int)2896);
            }
            if (chams.depth.getValue().booleanValue()) {
                GL11.glDepthMask((boolean)false);
            }
            if (chams.mode.is("Wire") || chams.mode.is("WireModel")) {
                GL11.glPolygonMode((int)1032, (int)6913);
            } else if (chams.mode.is("Normal")) {
                GL11.glPolygonMode((int)1032, (int)6914);
            }
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)((float)chams.width.getValue().doubleValue()));
            if (chams.xqz.getValue().booleanValue()) {
                if (entityLivingBase instanceof EntityOtherPlayerMP || entityLivingBase instanceof EntityPlayerSP) {
                    ColorUtil.setColor(chams.xqzColorPlayer.getValue());
                } else if (EntityUtil.isPassiveMob((Entity)entityLivingBase) || EntityUtil.isNeutralMob((Entity)entityLivingBase)) {
                    ColorUtil.setColor(chams.xqzColorPlayerMobs.getValue());
                } else {
                    ColorUtil.setColor(chams.xqzColorMonster.getValue());
                }
                GL11.glDisable((int)2929);
                GL11.glEnable((int)10754);
                this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                if (chams.mode.is("WireModel")) {
                    GL11.glPolygonMode((int)1032, (int)6914);
                    this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                    GL11.glPolygonMode((int)1032, (int)6913);
                }
                GL11.glEnable((int)2929);
                if (entityLivingBase instanceof EntityOtherPlayerMP || entityLivingBase instanceof EntityPlayerSP) {
                    ColorUtil.setColor(chams.highlightColorPlayer.getValue());
                } else if (EntityUtil.isPassiveMob((Entity)entityLivingBase) || EntityUtil.isNeutralMob((Entity)entityLivingBase)) {
                    ColorUtil.setColor(chams.highlightColorMobs.getValue());
                } else {
                    ColorUtil.setColor(chams.highlightColorMonster.getValue());
                }
                this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                if (chams.mode.is("WireModel")) {
                    GL11.glPolygonMode((int)1032, (int)6914);
                    this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                    GL11.glPolygonMode((int)1032, (int)6913);
                }
            } else {
                GL11.glDisable((int)2929);
                GL11.glEnable((int)10754);
                if (entityLivingBase instanceof EntityOtherPlayerMP || entityLivingBase instanceof EntityPlayerSP) {
                    ColorUtil.setColor(chams.highlightColorPlayer.getValue());
                } else if (EntityUtil.isPassiveMob((Entity)entityLivingBase) || EntityUtil.isNeutralMob((Entity)entityLivingBase)) {
                    ColorUtil.setColor(chams.highlightColorMobs.getValue());
                } else {
                    ColorUtil.setColor(chams.highlightColorMonster.getValue());
                }
                this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                if (chams.mode.is("WireModel")) {
                    GL11.glPolygonMode((int)1032, (int)6914);
                    this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                }
                GL11.glEnable((int)2929);
            }
            if (chams.lighting.getValue().booleanValue()) {
                GL11.glEnable((int)2896);
            }
            if (chams.depth.getValue().booleanValue()) {
                GL11.glDepthMask((boolean)true);
            }
            if (chams.blend.getValue().booleanValue()) {
                GL11.glDisable((int)3042);
            }
            if (!chams.texture.getValue().booleanValue() && !chams.mode.is("Shine")) {
                GL11.glEnable((int)3553);
            }
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        } else if (chams.pops.containsKey(entityLivingBase.getEntityId())) {
            if (chams.pops.get(entityLivingBase.getEntityId()) == 0) {
                Minecraft.getMinecraft().world.removeEntityFromWorld(entityLivingBase.entityId);
            } else if (chams.pops.get(entityLivingBase.getEntityId()) < 0) {
                if (chams.pops.get(entityLivingBase.getEntityId()) < -5) {
                    chams.pops.remove(entityLivingBase.getEntityId());
                }
                return;
            }
            info.cancel();
            GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)10754);
            GL11.glPolygonMode((int)1032, (int)6914);
            ColorUtil.setColor(new Color(chams.popChamsColors.getValue().getRed(), chams.popChamsColors.getValue().getGreen(), chams.popChamsColors.getValue().getBlue(), chams.pops.get(entityLivingBase.getEntityId())));
            this.mainModel.render((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glEnable((int)2929);
            GL11.glEnable((int)3553);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
            chams.pops.computeIfPresent(entityLivingBase.getEntityId(), (key, oldValue) -> oldValue - 1);
        }
    }
}

