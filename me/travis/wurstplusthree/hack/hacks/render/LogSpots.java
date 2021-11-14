/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.ConnectionEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.MathsUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;

@Hack.Registration(name="Logout Spots", description="shows ez logs", category=Hack.Category.RENDER, isListening=false)
public class LogSpots
extends Hack {
    ColourSetting colour = new ColourSetting("Colour", new Colour(255, 255, 255, 255), (Hack)this);
    IntSetting range = new IntSetting("Distance", 250, 0, 500, this);
    BooleanSetting announce = new BooleanSetting("Announce", (Boolean)false, (Hack)this);
    private final List<LogoutPos> spots = new CopyOnWriteArrayList<LogoutPos>();

    @Override
    public void onLogout() {
        this.spots.clear();
    }

    @Override
    public void onDisable() {
        this.spots.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onRender3D(Render3DEvent event) {
        if (!this.spots.isEmpty()) {
            List<LogoutPos> list = this.spots;
            synchronized (list) {
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        AxisAlignedBB bb = RenderUtil.interpolateAxis(spot.getEntity().getEntityBoundingBox());
                        RenderUtil.drawBlockOutline(bb, this.colour.getValue(), 2.0f);
                        double x = this.interpolate(spot.getEntity().lastTickPosX, spot.getEntity().posX, event.getPartialTicks()) - LogSpots.mc.getRenderManager().renderPosX;
                        double y = this.interpolate(spot.getEntity().lastTickPosY, spot.getEntity().posY, event.getPartialTicks()) - LogSpots.mc.getRenderManager().renderPosY;
                        double z = this.interpolate(spot.getEntity().lastTickPosZ, spot.getEntity().posZ, event.getPartialTicks()) - LogSpots.mc.getRenderManager().renderPosZ;
                        this.renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ(), spot.getHp(), spot.getTotem(), spot.getPops());
                    }
                });
            }
        }
    }

    @Override
    public void onUpdate() {
        this.spots.removeIf(spot -> LogSpots.mc.player.getDistanceSq((Entity)spot.getEntity()) >= MathsUtil.square(this.range.getValue().floatValue()));
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onConnection(ConnectionEvent event) {
        if (event.getStage() == 0) {
            UUID uuid = event.getUuid();
            EntityPlayer entity = LogSpots.mc.world.getPlayerEntityByUUID(uuid);
            if (entity != null && this.announce.getValue().booleanValue()) {
                ClientMessage.sendMessage("\u00a7a" + entity.getName() + " just logged in at (" + (int)entity.posX + ", " + (int)entity.posY + ", " + (int)entity.posZ + ")");
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
        } else if (event.getStage() == 1) {
            EntityPlayer entity = event.getEntity();
            UUID uuid = event.getUuid();
            String name = event.getName();
            if (this.announce.getValue().booleanValue()) {
                ClientMessage.sendMessage("\u00a7c" + event.getName() + " just logged out at (" + (int)entity.posX + ", " + (int)entity.posY + ", " + (int)entity.posZ + ")");
            }
            if (name != null && uuid != null) {
                this.spots.add(new LogoutPos(name, uuid, entity, Math.round(entity.getHealth() + entity.getAbsorptionAmount()), entity.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING, WurstplusThree.POP_MANAGER.getTotemPops(entity)));
            }
        }
    }

    private void renderNameTag(String name, double x, double yi, double z, float delta, double xPos, double yPos, double zPos, double hp, boolean hasTotem, int pops) {
        double y = yi + 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = name + " [" + (int)xPos + ", " + (int)yPos + ", " + (int)zPos + "]";
        String displayTag2 = "HP [" + hp + "] : Totem [" + hasTotem + "] : Pops [" + pops + "]";
        double distance = camera.getDistance(x + LogSpots.mc.getRenderManager().viewerPosX, y + LogSpots.mc.getRenderManager().viewerPosY, z + LogSpots.mc.getRenderManager().viewerPosZ);
        int width = WurstplusThree.GUI_FONT_MANAGER.getTextWidth(displayTag) / 2;
        int width2 = WurstplusThree.GUI_FONT_MANAGER.getTextWidth(displayTag2) / 2;
        double scale = 0.0012018;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)y + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-LogSpots.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)LogSpots.mc.getRenderManager().playerViewX, (float)(LogSpots.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        RenderUtil.drawRect(-width2 - 2, -(WurstplusThree.GUI_FONT_MANAGER.getTextHeight() + 1), (float)width2 + 2.0f, 14.0f, 0x55000000);
        GlStateManager.disableBlend();
        WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(displayTag, -width, -(WurstplusThree.GUI_FONT_MANAGER.getTextHeight() - 1), this.colour.getValue().getRGB());
        WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(displayTag2, -width2, -(WurstplusThree.GUI_FONT_MANAGER.getTextHeight() - 1 - 12), this.colour.getValue().getRGB());
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
        GlStateManager.popMatrix();
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    private static class LogoutPos {
        private final String name;
        private final EntityPlayer entity;
        private final boolean hasTotem;
        private final double x;
        private final double y;
        private final double z;
        private final double hp;
        private final int pops;

        public LogoutPos(String name, UUID uuid, EntityPlayer entity, double hp, boolean totem, int pops) {
            this.name = name;
            this.entity = entity;
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
            this.hp = hp;
            this.hasTotem = totem;
            this.pops = pops;
        }

        public String getName() {
            return this.name;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }

        public double getHp() {
            return this.hp;
        }

        public boolean getTotem() {
            return this.hasTotem;
        }

        public int getPops() {
            return this.pops;
        }
    }
}

