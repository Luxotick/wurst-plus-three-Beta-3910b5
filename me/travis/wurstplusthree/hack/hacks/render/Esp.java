/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.awt.Color;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Hack.Registration(name="Esp", description="draws box around shit", category=Hack.Category.RENDER, isListening=false)
public class Esp
extends Hack {
    BooleanSetting items = new BooleanSetting("Items", (Boolean)true, (Hack)this);
    BooleanSetting orbs = new BooleanSetting("Orbs", (Boolean)true, (Hack)this);
    BooleanSetting bottles = new BooleanSetting("Bottles", (Boolean)true, (Hack)this);
    BooleanSetting pearl = new BooleanSetting("Pearl", (Boolean)true, (Hack)this);
    BooleanSetting sources = new BooleanSetting("Sources", (Boolean)false, (Hack)this);
    IntSetting sourceRange = new IntSetting("Range", 7, 1, 25, this, s -> this.sources.getValue());
    ColourSetting colour = new ColourSetting("Colour", new Colour(255, 50, 50, 150), (Hack)this);
    ColourSetting colour2 = new ColourSetting("SourceColour", new Colour(100, 50, 200, 150), (Hack)this);

    @Override
    public void onRender3D(Render3DEvent event) {
        AxisAlignedBB bb;
        Vec3d interp;
        int i;
        if (this.nullCheck()) {
            return;
        }
        if (this.items.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : Esp.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityItem) || !(Esp.mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)this.colour.getValue().getRed() / 255.0f), (float)((float)this.colour.getValue().getGreen() / 255.0f), (float)((float)this.colour.getValue().getBlue() / 255.0f), (float)((float)this.colour.getValue().getAlpha() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.depthMask((boolean)true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb, new Color(this.colour.getValue().getRed(), this.colour.getValue().getGreen(), this.colour.getValue().getBlue(), this.colour.getValue().getAlpha()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.orbs.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : Esp.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityXPOrb) || !(Esp.mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)this.colour.getValue().getRed() / 255.0f), (float)((float)this.colour.getValue().getGreen() / 255.0f), (float)((float)this.colour.getValue().getBlue() / 255.0f), (float)((float)this.colour.getValue().getAlpha() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.depthMask((boolean)true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb, new Color(this.colour.getValue().getRed(), this.colour.getValue().getGreen(), this.colour.getValue().getBlue(), this.colour.getValue().getAlpha()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.pearl.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : Esp.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderPearl) || !(Esp.mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)this.colour.getValue().getRed() / 255.0f), (float)((float)this.colour.getValue().getGreen() / 255.0f), (float)((float)this.colour.getValue().getBlue() / 255.0f), (float)((float)this.colour.getValue().getAlpha() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.depthMask((boolean)true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb, new Color(this.colour.getValue().getRed(), this.colour.getValue().getGreen(), this.colour.getValue().getBlue(), this.colour.getValue().getAlpha()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.bottles.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : Esp.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityExpBottle) || !(Esp.mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)this.colour.getValue().getRed() / 255.0f), (float)((float)this.colour.getValue().getGreen() / 255.0f), (float)((float)this.colour.getValue().getBlue() / 255.0f), (float)((float)this.colour.getValue().getAlpha() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.depthMask((boolean)true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb, new Color(this.colour.getValue().getRed(), this.colour.getValue().getGreen(), this.colour.getValue().getBlue(), this.colour.getValue().getAlpha()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.sources.getValue().booleanValue()) {
            for (BlockPos pos : EntityUtil.getSphere(PlayerUtil.getPlayerPos(), this.sourceRange.getValue().intValue(), this.sourceRange.getValue(), false, true, 0)) {
                if (Esp.mc.world.getBlockState(pos).getBlock().getMetaFromState(Esp.mc.world.getBlockState(pos)) != 0 || !(Esp.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                RenderUtil.drawBoxESP(pos, this.colour2.getColor(), this.colour2.getColor(), 1.0f, true, true, false);
            }
        }
    }
}

