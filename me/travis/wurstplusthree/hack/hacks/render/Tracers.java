/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.awt.Color;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Hack.Registration(name="Tracers", description="drawslines", category=Hack.Category.RENDER, isListening=false)
public class Tracers
extends Hack {
    DoubleSetting width = new DoubleSetting("Width", (Double)2.0, (Double)0.0, (Double)10.0, this);
    DoubleSetting range = new DoubleSetting("Range", (Double)100.0, (Double)0.0, (Double)500.0, this);
    BooleanSetting friends = new BooleanSetting("Friends", (Boolean)true, (Hack)this);

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.nullCheck()) {
            return;
        }
        GlStateManager.pushMatrix();
        Tracers.mc.world.loadedEntityList.forEach(entity -> {
            if (!(entity instanceof EntityPlayer) || entity == Tracers.mc.player) {
                return;
            }
            EntityPlayer player = (EntityPlayer)entity;
            if (!this.friends.getValue().booleanValue() && WurstplusThree.FRIEND_MANAGER.isFriend(player.getName())) {
                return;
            }
            if ((double)Tracers.mc.player.getDistance((Entity)player) > this.range.getValue()) {
                return;
            }
            float[] colour = this.getColorByDistance((Entity)entity);
            this.drawLineToEntity((Entity)entity, colour[0], colour[1], colour[2], colour[3]);
        });
        GlStateManager.popMatrix();
    }

    public double interpolate(double now, double then) {
        return then + (now - then) * (double)mc.getRenderPartialTicks();
    }

    public double[] interpolate(Entity entity) {
        double posX = this.interpolate(entity.posX, entity.lastTickPosX) - Tracers.mc.getRenderManager().renderPosX;
        double posY = this.interpolate(entity.posY, entity.lastTickPosY) - Tracers.mc.getRenderManager().renderPosY;
        double posZ = this.interpolate(entity.posZ, entity.lastTickPosZ) - Tracers.mc.getRenderManager().renderPosZ;
        return new double[]{posX, posY, posZ};
    }

    public void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
        double[] xyz = this.interpolate(e);
        this.drawLine(xyz[0], xyz[1], xyz[2], e.height, red, green, blue, opacity);
    }

    public void drawLine(double posx, double posy, double posz, double up, float red, float green, float blue, float opacity) {
        Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians(Tracers.mc.player.rotationPitch))).rotateYaw(-((float)Math.toRadians(Tracers.mc.player.rotationYaw)));
        this.drawLineFromPosToPos(eyes.x, eyes.y + (double)Tracers.mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    }

    public void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)this.width.getValue().floatValue());
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)opacity);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLoadIdentity();
        boolean bobbing = Tracers.mc.gameSettings.viewBobbing;
        Tracers.mc.gameSettings.viewBobbing = false;
        Tracers.mc.entityRenderer.orientCamera(mc.getRenderPartialTicks());
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)posx, (double)posy, (double)posz);
        GL11.glVertex3d((double)posx2, (double)posy2, (double)posz2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
        Tracers.mc.gameSettings.viewBobbing = bobbing;
    }

    public float[] getColorByDistance(Entity entity) {
        if (entity instanceof EntityPlayer && WurstplusThree.FRIEND_MANAGER.isFriend(entity.getName())) {
            return new float[]{0.0f, 0.5f, 1.0f, 1.0f};
        }
        Color col = new Color(Color.HSBtoRGB((float)(Math.max(0.0, Math.min(Tracers.mc.player.getDistanceSq(entity), 2500.0) / 2500.0) / 3.0), 1.0f, 0.8f) | 0xFF000000);
        return new float[]{(float)col.getRed() / 255.0f, (float)col.getGreen() / 255.0f, (float)col.getBlue() / 255.0f, 1.0f};
    }
}

