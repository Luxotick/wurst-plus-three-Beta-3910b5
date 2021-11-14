/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.util;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.ColorUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.NotificationRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtil2D {
    NotificationRender notificationRender = new NotificationRender(0, 0);
    public static RenderUtil2D INSTANCE;
    private static final BufferBuilder bufferbuilder;
    private static final Tessellator tessellator;
    public static int deltaTime;

    public RenderUtil2D() {
        INSTANCE = this;
    }

    public int getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(int v) {
        deltaTime = v;
    }

    public static void drawNotification(String text, int x, int y, Colour colour) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int length = WurstplusThree.GUI_FONT_MANAGER.getTextWidth(text);
        int height = WurstplusThree.GUI_FONT_MANAGER.getTextHeight();
        RenderUtil2D.drawRectMC(x, y - 2, x + length + 2, y + height + 2, colour.hashCode());
        int deltaTime = INSTANCE.getDeltaTime();
        float SEQUENCES = 250.0f;
        if (RenderUtil2D.INSTANCE.notificationRender.animationVal < (float)x) {
            RenderUtil2D.INSTANCE.notificationRender.animationVal += (float)x * ((float)deltaTime / 250.0f);
        }
        RenderUtil2D.INSTANCE.notificationRender.animationVal = RenderUtil2D.constrainToRange(RenderUtil2D.INSTANCE.notificationRender.animationVal, 0.0f, x);
        float newY = (float)sr.getScaledWidth() - RenderUtil2D.INSTANCE.notificationRender.animationVal - 2.0f;
        RenderUtil2D.INSTANCE.notificationRender.x = (int)newY;
    }

    private static float constrainToRange(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static void drawHLine(int x, int y, int length, int color) {
        Gui.drawRect((int)x, (int)y, (int)(x + length), (int)(y + 1), (int)color);
    }

    public static void drawVLine(int x, int y, int length, int color) {
        Gui.drawRect((int)x, (int)y, (int)(x + 1), (int)(y + length), (int)color);
    }

    public static void drawHLineG(int x, int y, int length, int color, int color2) {
        RenderUtil2D.drawSidewaysGradientRect(x, y, x + length, y + 1, color, color2);
    }

    public static void drawVLineG(int x, int y, int length, int color, int color2) {
        RenderUtil2D.drawGradientRect(x, y, x + 1, y + length, color, color2, false);
    }

    public static void drawRectMC(int startX, int startY, int endX, int endY, int color) {
        Gui.drawRect((int)startX, (int)startY, (int)endX, (int)endY, (int)color);
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor, boolean hovered) {
        if (hovered) {
            startColor = ColorUtil.shadeColour(startColor, -20);
            endColor = ColorUtil.shadeColour(endColor, -20);
        }
        float c = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float c1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float c2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float c3 = (float)(startColor & 0xFF) / 255.0f;
        float c4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float c5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float c6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float c7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawBorderedRect(int left, double top, int right, double bottom, int borderWidth, int insideColor, int borderColor, boolean hover) {
        if (hover) {
            insideColor = ColorUtil.shadeColour(insideColor, -20);
            borderColor = ColorUtil.shadeColour(borderColor, -20);
        }
        RenderUtil2D.drawRectBase(left + borderWidth, top + (double)borderWidth, right - borderWidth, bottom - (double)borderWidth, insideColor);
        RenderUtil2D.drawRectBase(left, top + (double)borderWidth, left + borderWidth, bottom - (double)borderWidth, borderColor);
        RenderUtil2D.drawRectBase(right - borderWidth, top + (double)borderWidth, right, bottom - (double)borderWidth, borderColor);
        RenderUtil2D.drawRectBase(left, top, right, top + (double)borderWidth, borderColor);
        RenderUtil2D.drawRectBase(left, bottom - (double)borderWidth, right, bottom, borderColor);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)9);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)pickerX, (float)pickerY);
        GL11.glVertex2f((float)pickerX, (float)(pickerY + pickerHeight));
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight));
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)pickerY);
        GL11.glEnd();
        GL11.glDisable((int)3008);
        GL11.glBegin((int)9);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)pickerX, (float)pickerY);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)pickerX, (float)(pickerY + pickerHeight));
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight));
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)pickerY);
        GL11.glEnd();
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color((float)(endColor >> 24 & 0xFF) / 255.0f, (float)(endColor >> 16 & 0xFF) / 255.0f, (float)(endColor >> 8 & 0xFF) / 255.0f, (float)(endColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color((float)(startColor >> 16 & 0xFF) / 255.0f, (float)(startColor >> 8 & 0xFF) / 255.0f, (float)(startColor & 0xFF) / 255.0f, (float)(startColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color((float)(startColor >> 16 & 0xFF) / 255.0f, (float)(startColor >> 8 & 0xFF) / 255.0f, (float)(startColor & 0xFF) / 255.0f, (float)(startColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color((float)(endColor >> 24 & 0xFF) / 255.0f, (float)(endColor >> 16 & 0xFF) / 255.0f, (float)(endColor >> 8 & 0xFF) / 255.0f, (float)(endColor >> 24 & 0xFF) / 255.0f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)9);
            GL11.glColor4f((float)((float)(startColor >> 16 & 0xFF) / 255.0f), (float)((float)(startColor >> 8 & 0xFF) / 255.0f), (float)((float)(startColor & 0xFF) / 255.0f), (float)((float)(startColor >> 24 & 0xFF) / 255.0f));
            GL11.glVertex2f((float)minX, (float)minY);
            GL11.glVertex2f((float)minX, (float)maxY);
            GL11.glColor4f((float)((float)(endColor >> 16 & 0xFF) / 255.0f), (float)((float)(endColor >> 8 & 0xFF) / 255.0f), (float)((float)(endColor & 0xFF) / 255.0f), (float)((float)(endColor >> 24 & 0xFF) / 255.0f));
            GL11.glVertex2f((float)maxX, (float)maxY);
            GL11.glVertex2f((float)maxX, (float)minY);
            GL11.glEnd();
            GL11.glShadeModel((int)7424);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
        } else {
            RenderUtil2D.drawGradientRect(minX, minY, maxX, maxY, startColor, endColor, false);
        }
    }

    public static void drawRectBase(int left, double top, double right, double bottom, int color) {
        double side;
        if ((double)left < right) {
            side = left;
            left = (int)right;
            right = (int)side;
        }
        if (top < bottom) {
            side = top;
            top = bottom;
            bottom = side;
        }
        GlStateManager.enableBlend();
        GL11.glDisable((int)3042);
        GL11.glDisable((int)3008);
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)((float)(color >> 24 & 0xFF) / 255.0f));
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos((double)left, top, 0.0).endVertex();
        tessellator.draw();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3008);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRectBase(int left, double top, int right, double bottom, int color) {
        double side;
        if (left < right) {
            side = left;
            left = right;
            right = (int)side;
        }
        if (top < bottom) {
            side = top;
            top = bottom;
            bottom = side;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)((float)(color >> 24 & 0xFF) / 255.0f));
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, bottom, 0.0).endVertex();
        bufferbuilder.pos((double)right, bottom, 0.0).endVertex();
        bufferbuilder.pos((double)right, top, 0.0).endVertex();
        bufferbuilder.pos((double)left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawSidewaysGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float c = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float c1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float c2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float c3 = (float)(startColor & 0xFF) / 255.0f;
        float c4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float c5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float c6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float c7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(c1, c2, c3, c).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawImageToScreen(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, ResourceLocation rsc) {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(rsc);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
            GL11.glDisable((int)3042);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void drawEntityOnScreen(float x, float y, float scale, EntityLivingBase entityLivingBase) {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableColorMaterial();
        GlStateManager.translate((float)x, (float)y, (float)50.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-((float)Math.atan(entityLivingBase.rotationPitch / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity((Entity)entityLivingBase, 0.0, 0.0, 0.0, entityLivingBase.rotationYaw, 1.0f, false);
        rendermanager.setRenderShadow(true);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.disableDepth();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.popMatrix();
    }

    static {
        bufferbuilder = Tessellator.getInstance().getBuffer();
        tessellator = Tessellator.getInstance();
    }
}

