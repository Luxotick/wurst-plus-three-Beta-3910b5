/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.hack.hacks.player.PlayerSpoofer;
import me.travis.wurstplusthree.hack.hacks.render.HandColour;
import me.travis.wurstplusthree.hack.hacks.render.Nametags;
import me.travis.wurstplusthree.hack.hacks.render.Pitbull;
import me.travis.wurstplusthree.util.SkinStorageManipulationer;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method={"renderEntityName"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if (Nametags.INSTANCE.isEnabled()) {
            info.cancel();
        }
    }

    @Inject(method={"renderRightArm"}, at={@At(value="FIELD", target="Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode=181)}, cancellable=true)
    public void renderRightArmBegin(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColour.INSTANCE.isEnabled()) {
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            GL11.glEnable((int)10754);
            OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
            Colour color = HandColour.INSTANCE.colour.getValue();
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        } else if (clientPlayer == Minecraft.getMinecraft().player && PlayerSpoofer.INSTANCE.isEnabled()) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(SkinStorageManipulationer.getTexture().toString()));
        }
    }

    @Inject(method={"renderRightArm"}, at={@At(value="RETURN")}, cancellable=true)
    public void renderRightArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColour.INSTANCE.isEnabled()) {
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
        } else if (clientPlayer == Minecraft.getMinecraft().player && PlayerSpoofer.INSTANCE.isEnabled()) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(SkinStorageManipulationer.getTexture().toString()));
        }
    }

    @Inject(method={"renderLeftArm"}, at={@At(value="FIELD", target="Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode=181)}, cancellable=true)
    public void renderLeftArmBegin(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColour.INSTANCE.isEnabled()) {
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            GL11.glEnable((int)10754);
            OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
            Colour color = HandColour.INSTANCE.colour.getValue();
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        } else if (clientPlayer == Minecraft.getMinecraft().player && PlayerSpoofer.INSTANCE.isEnabled()) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(SkinStorageManipulationer.getTexture().toString()));
        }
    }

    @Inject(method={"renderLeftArm"}, at={@At(value="RETURN")}, cancellable=true)
    public void renderLeftArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColour.INSTANCE.isEnabled()) {
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
        } else if (clientPlayer == Minecraft.getMinecraft().player && PlayerSpoofer.INSTANCE.isEnabled()) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(SkinStorageManipulationer.getTexture().toString()));
        }
    }

    @Overwrite
    public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
        if (PlayerSpoofer.INSTANCE.isEnabled() && entity == Minecraft.getMinecraft().player) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            return new ResourceLocation(SkinStorageManipulationer.getTexture().toString());
        }
        if (Pitbull.INSTANCE.isEnabled() && entity != Minecraft.getMinecraft().player) {
            GL11.glColor4f((float)((float)Pitbull.INSTANCE.texture.getColor().getRed() / 255.0f), (float)((float)Pitbull.INSTANCE.texture.getColor().getGreen() / 255.0f), (float)((float)Pitbull.INSTANCE.texture.getColor().getBlue() / 255.0f), (float)((float)Pitbull.INSTANCE.texture.getColor().getAlpha() / 255.0f));
            return new ResourceLocation("textures/pitbull.png");
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return entity.getLocationSkin();
    }
}

