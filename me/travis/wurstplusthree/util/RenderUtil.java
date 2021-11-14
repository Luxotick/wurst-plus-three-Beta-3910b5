/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.util;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.RenderBuilder;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderUtil
implements Globals {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    public static ICamera camera = new Frustum();

    public static String getRandomFont() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);
        return fonts[random.nextInt(fonts.length)];
    }

    public static Color getContrastColor(Color color) {
        double y = (299.0f * (float)color.getRed() + 587.0f * (float)color.getGreen() + 114.0f * (float)color.getBlue()) / 1000.0f;
        return y >= 128.0 ? Color.black : Color.white;
    }

    public static void drawTriangleOutline(float x, float y, float size, float widthDiv, float heightDiv, float outlineWidth, int color) {
        boolean blend = GL11.glIsEnabled((int)3042);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)outlineWidth);
        RenderUtil.hexColor(color);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x - size / widthDiv), (double)(y - size));
        GL11.glVertex2d((double)x, (double)(y - size / heightDiv));
        GL11.glVertex2d((double)(x + size / widthDiv), (double)(y - size));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2848);
    }

    public static void hexColor(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.shadeModel((int)7425);
        GL11.glLineWidth((float)thickness);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x1, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GL11.glDisable((int)2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawOutlineLine(double left, double top, double right, double bottom, double width, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)((float)width));
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBox(BlockPos blockPos, double height, Colour color, int sides) {
        RenderUtil.drawBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0, height, 1.0, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, Colour color, int sides) {
        RenderUtil.drawBox(bb, check, height, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, Colour color, int alpha, int sides) {
        if (check) {
            RenderUtil.drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ, color, alpha, sides);
        } else {
            RenderUtil.drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX - bb.minX, height, bb.maxZ - bb.minZ, color, alpha, sides);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color secondColor, float lineWidth, boolean outline, boolean box, boolean air) {
        if (box) {
            RenderUtil.drawBox(pos, color, air);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondColor, lineWidth, air);
        }
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - RenderUtil.mc.getRenderManager().viewerPosX, bb.minY - RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX - RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY - RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha, Double height) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)lineWidth);
            if (box) {
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)boxAlpha / 255.0f));
            }
            if (outline) {
                RenderGlobal.drawBoundingBox((double)bb.minX, (double)bb.minY, (double)bb.minZ, (double)bb.maxX, (double)bb.maxY, (double)bb.maxZ, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color outLineColor, double lineWidth, boolean outline, boolean box, Float height) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + (double)height.floatValue(), (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)((float)lineWidth));
            if (box) {
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            if (outline) {
                RenderGlobal.drawBoundingBox((double)bb.minX, (double)bb.minY, (double)bb.minZ, (double)bb.maxX, (double)bb.maxY, (double)bb.maxZ, (float)((float)outLineColor.getRed() / 255.0f), (float)((float)outLineColor.getGreen() / 255.0f), (float)((float)outLineColor.getBlue() / 255.0f), (float)((float)outLineColor.getAlpha() / 255.0f));
            }
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
            RenderUtil.drawBlockOutline(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), color, linewidth);
        }
    }

    public static void drawBlockOutlineBB(AxisAlignedBB bb, Color color, float linewidth) {
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawBlockOutline(bb.grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), color, linewidth);
    }

    public static void drawBox(BlockPos pos, Color color, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
            AxisAlignedBB bb = iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z);
            camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
            if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.depthMask((boolean)true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public static void drawGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color endColor, float linewidth) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth, double height) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawGradientBlockOutline(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z).expand(0.0, height, 0.0), startColor, endColor, linewidth);
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height, boolean gradient, boolean invert) {
        if (gradient) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            RenderUtil.drawGradientBlockOutline(pos, invert ? endColor : color, invert ? color : endColor, linewidth, height);
            return;
        }
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            AxisAlignedBB blockAxis = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
            RenderUtil.drawBlockOutline(blockAxis.grow((double)0.002f), color, linewidth);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color secondColor, float lineWidth, boolean outline, boolean box, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
        if (box) {
            RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), height, gradientBox, invertGradientBox, gradientAlpha);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondColor, lineWidth, air, height, gradientOutline, invertGradientOutline);
        }
    }

    public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, double height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        AxisAlignedBB bb = iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z).expand(0.0, height, 0.0);
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        if (face == EnumFacing.DOWN) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.minY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.UP) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.maxY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.EAST) {
            x1 = bb.maxX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.WEST) {
            x1 = bb.minX;
            x2 = bb.minX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.SOUTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.maxZ;
            z2 = bb.maxZ;
        } else if (face == EnumFacing.NORTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.minZ;
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.depthMask((boolean)false);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
        } else if (face == EnumFacing.UP) {
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
        } else if (face == EnumFacing.DOWN) {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void drawGradientPlaneBB(AxisAlignedBB bb, EnumFacing face, Color startColor, Color endColor, double height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        switch (face) {
            case DOWN: {
                x1 = bb.minX;
                x2 = bb.maxX;
                y1 = bb.minY;
                y2 = bb.minY;
                z1 = bb.minZ;
                z2 = bb.maxZ;
                break;
            }
            case UP: {
                x1 = bb.minX;
                x2 = bb.maxX;
                y1 = bb.maxY;
                y2 = bb.maxY;
                z1 = bb.minZ;
                z2 = bb.maxZ;
                break;
            }
            case EAST: {
                x1 = bb.maxX;
                x2 = bb.maxX;
                y1 = bb.minY;
                y2 = bb.maxY;
                z1 = bb.minZ;
                z2 = bb.maxZ;
                break;
            }
            case WEST: {
                x1 = bb.minX;
                x2 = bb.minX;
                y1 = bb.minY;
                y2 = bb.maxY;
                z1 = bb.minZ;
                z2 = bb.maxZ;
                break;
            }
            case SOUTH: {
                x1 = bb.minX;
                x2 = bb.maxX;
                y1 = bb.minY;
                y2 = bb.maxY;
                z1 = bb.maxZ;
                z2 = bb.maxZ;
                break;
            }
            case NORTH: {
                x1 = bb.minX;
                x2 = bb.maxX;
                y1 = bb.minY;
                y2 = bb.maxY;
                z1 = bb.minZ;
                z2 = bb.minZ;
            }
        }
        if (face == EnumFacing.DOWN || face == EnumFacing.UP || face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            // empty if block
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.depthMask((boolean)false);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
        } else if (face == EnumFacing.UP) {
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y1, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x1, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z1).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
            builder.pos(x2, y2, z2).color(red1, green1, blue1, alpha1).endVertex();
        } else if (face == EnumFacing.DOWN) {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void drawOpenGradientBoxBB(AxisAlignedBB bb, Color startColor, Color endColor, double height) {
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        for (EnumFacing face : EnumFacing.values()) {
            RenderUtil.drawGradientPlaneBB(bb.grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), face, startColor, endColor, height);
        }
    }

    public static void drawOpenGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
        for (EnumFacing face : EnumFacing.values()) {
            if (face == EnumFacing.UP) continue;
            RenderUtil.drawGradientPlane(pos, face, startColor, endColor, height);
        }
    }

    public static void drawBox(BlockPos pos, Color color, double height, boolean gradient, boolean invert, int alpha) {
        if (gradient) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            RenderUtil.drawOpenGradientBox(pos, invert ? endColor : color, invert ? color : endColor, height);
            return;
        }
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBBBox(AxisAlignedBB BB, Colour colour, int alpha) {
        AxisAlignedBB bb = new AxisAlignedBB(BB.minX - RenderUtil.mc.getRenderManager().viewerPosX, BB.minY - RenderUtil.mc.getRenderManager().viewerPosY, BB.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, BB.maxX - RenderUtil.mc.getRenderManager().viewerPosX, BB.maxY - RenderUtil.mc.getRenderManager().viewerPosY, BB.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)colour.getRed() / 255.0f), (float)((float)colour.getGreen() / 255.0f), (float)((float)colour.getBlue() / 255.0f), (float)((float)alpha / 255.0f));
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBBOutline(AxisAlignedBB bb, Colour colour, int alpha) {
    }

    public void resetCaps() {
        glCapMap.forEach((arg_0, arg_1) -> this.setGlState(arg_0, arg_1));
    }

    public void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
    }

    public static void drawBox(double x, double y, double z, double w, double h, double d, Colour color, int alpha, int sides) {
        GlStateManager.disableAlpha();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        color.glColor();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.doVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides, false);
        tessellator.draw();
        GlStateManager.enableAlpha();
    }

    private static AxisAlignedBB getBoundingBox(BlockPos bp, double width, double height, double depth) {
        double x = bp.getX();
        double y = bp.getY();
        double z = bp.getZ();
        return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
    }

    public static AxisAlignedBB getBoundingBox(BlockPos blockPos) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        return axisAlignedBB;
    }

    public static void drawBoundingBox(AxisAlignedBB bb, double width, Colour color, int alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.glLineWidth((float)((float)width));
        color.glColor();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        tessellator.draw();
    }

    private static void colorVertex(double x, double y, double z, Colour color, int alpha, BufferBuilder bufferbuilder) {
        bufferbuilder.pos(x - RenderUtil.mc.getRenderManager().viewerPosX, y - RenderUtil.mc.getRenderManager().viewerPosY, z - RenderUtil.mc.getRenderManager().viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).endVertex();
    }

    private static void doVerticies(AxisAlignedBB axisAlignedBB, Colour color, int alpha, BufferBuilder bufferbuilder, int sides, boolean five) {
        if ((sides & 0x20) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 0x10) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 4) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 8) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 2) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 1) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
            }
        }
    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, Colour color, int sides) {
        RenderUtil.drawBoundingBoxWithSides(RenderUtil.getBoundingBox(blockPos, 1.0, 1.0, 1.0), width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, Colour color, int alpha, int sides) {
        RenderUtil.drawBoundingBoxWithSides(RenderUtil.getBoundingBox(blockPos, 1.0, 1.0, 1.0), width, color, alpha, sides);
    }

    public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, Colour color, int sides) {
        RenderUtil.drawBoundingBoxWithSides(axisAlignedBB, width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, Colour color, int alpha, int sides) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.glLineWidth((float)width);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.doVerticies(axisAlignedBB, color, alpha, bufferbuilder, sides, true);
        tessellator.draw();
    }

    public static void drawText(BlockPos pos, String text, boolean custom) {
        if (pos == null || text == null) {
            return;
        }
        GlStateManager.pushMatrix();
        RenderUtil.glBillboardDistanceScaled((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, (EntityPlayer)RenderUtil.mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate((double)(-((double)WurstplusThree.GUI_FONT_MANAGER.getTextWidth(text) / 2.0)), (double)0.0, (double)0.0);
        if (custom) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        } else {
            RenderUtil.mc.fontRenderer.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        }
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtil.glBillboard(x, y, z);
        int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = (float)distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale((float)scaleDistance, (float)scaleDistance, (float)scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        GlStateManager.translate((double)((double)x - RenderUtil.mc.getRenderManager().renderPosX), (double)((double)y - RenderUtil.mc.getRenderManager().renderPosY), (double)((double)z - RenderUtil.mc.getRenderManager().renderPosZ));
        GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-RenderUtil.mc.player.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)RenderUtil.mc.player.rotationPitch, (float)(RenderUtil.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)(-scale), (float)(-scale), (float)scale);
    }

    public static void drawGlowBox(BlockPos blockPos, double height, Float lineWidth, Color color, Color outlineColor) {
        RenderUtil.drawBoxESP(blockPos, outlineColor, lineWidth.floatValue(), true, false, outlineColor.getAlpha(), -1.0);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        RenderBuilder.glSetup();
        RenderBuilder.glPrepare();
        RenderUtil.drawSelectionGlowFilledBox(axisAlignedBB, height, new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
        RenderBuilder.glRestore();
        RenderBuilder.glRelease();
    }

    public static void drawSelectionGlowFilledBox(AxisAlignedBB axisAlignedBB, double height, Color startColor, Color endColor) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder2 = Tessellator.getInstance().getBuffer();
        BufferBuilder2.begin(7, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.addChainedGlowBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + height, axisAlignedBB.maxZ, startColor, endColor);
        tessellator.draw();
    }

    public static void addChainedGlowBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color startColor, Color endColor) {
        BufferBuilder BufferBuilder2 = Tessellator.getInstance().getBuffer();
        BufferBuilder2.pos(minX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(maxX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        BufferBuilder2.pos(minX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
    }

    public static void drawGlError(BlockPos pos, double height, double length, double width, Color color) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            Tessellator t = Tessellator.getInstance();
            BufferBuilder bufferbuilder = t.getBuffer();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            RenderUtil.drawCornerVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color);
            t.draw();
        }
    }

    public static void drawCorner(BlockPos pos, double height, double length, double width, Color color) {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        AxisAlignedBB bb = RenderUtil.getBoundingBox(pos);
        RenderUtil.drawCornerVertices(bb.minX, bb.minY, bb.minZ, bb.maxX + length, bb.maxY + height, bb.maxZ + width, color);
        t.draw();
    }

    public static void drawCornerVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY + 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY + 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY + 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY + 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY - 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY - 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY - 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY - 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void drawCircle(float x, float y, float z, float radius, Colour colour) {
        BlockPos pos = new BlockPos((double)x, (double)y, (double)z);
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        RenderUtil.drawCircleVertices(bb, radius, colour);
    }

    public static void drawColumn(float x, float y, float z, float radius, Colour colour, int amount, double height) {
        double Hincrement = height / (double)amount;
        float Rincrement = radius / (float)amount * (float)height;
        BlockPos pos = new BlockPos((double)x, (double)y, (double)z);
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            for (int i = 0; i <= amount; ++i) {
                bb = new AxisAlignedBB(bb.minX, bb.minY + Hincrement * (double)i, bb.minZ, bb.maxX, bb.maxY + Hincrement * (double)i, bb.maxZ);
                RenderUtil.drawCircleVertices(bb, Rincrement * (float)i, colour);
            }
        }
    }

    public static void drawCircleVertices(AxisAlignedBB bb, float radius, Colour colour) {
        float r = (float)colour.getRed() / 255.0f;
        float g = (float)colour.getGreen() / 255.0f;
        float b = (float)colour.getBlue() / 255.0f;
        float a = (float)colour.getAlpha() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)1.0f);
        for (int i = 0; i < 360; ++i) {
            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.getCenter().x + Math.sin((double)i * 3.1415926 / 180.0) * (double)radius, bb.minY, bb.getCenter().z + Math.cos((double)i * 3.1415926 / 180.0) * (double)radius).color(r, g, b, a).endVertex();
            buffer.pos(bb.getCenter().x + Math.sin((double)(i + 1) * 3.1415926 / 180.0) * (double)radius, bb.minY, bb.getCenter().z + Math.cos((double)(i + 1) * 3.1415926 / 180.0) * (double)radius).color(r, g, b, a).endVertex();
            tessellator.draw();
        }
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}

