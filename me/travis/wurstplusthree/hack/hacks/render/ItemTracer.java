/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.entity.projectile.EntityPotion
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.ColorUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Hack.Registration(name="Item Tracer", category=Hack.Category.RENDER, description="Draws shit", priority=HackPriority.Lowest)
public class ItemTracer
extends Hack {
    ParentSetting pearl = new ParentSetting("Pearl", this);
    BooleanSetting chat = new BooleanSetting("Chat", (Boolean)true, this.pearl);
    BooleanSetting render = new BooleanSetting("Render", (Boolean)true, this.pearl);
    DoubleSetting aliveTime = new DoubleSetting("Alive Time", (Double)5.0, (Double)0.0, (Double)20.0, this.pearl);
    DoubleSetting thick = new DoubleSetting("Thick", (Double)3.0, (Double)0.0, (Double)10.0, this.pearl);
    IntSetting rDelay = new IntSetting("RDelay", 120, 0, 360, this.pearl);
    ColourSetting color = new ColourSetting("Color", new Colour(255, 255, 255), this.pearl);
    ParentSetting misc = new ParentSetting("Misc", this);
    BooleanSetting arrows = new BooleanSetting("Arrows", (Boolean)false, this.misc);
    BooleanSetting exp = new BooleanSetting("Exp", (Boolean)false, this.misc);
    BooleanSetting pots = new BooleanSetting("Pots", (Boolean)false, this.misc);
    private final HashMap<UUID, List<Vec3d>> poses = new HashMap();
    private final HashMap<UUID, Double> time = new HashMap();

    @Override
    public void onUpdate() {
        List<Vec3d> v;
        UUID toRemove = null;
        for (UUID uuid : this.time.keySet()) {
            if (this.time.get(uuid) <= 0.0) {
                this.poses.remove(uuid);
                toRemove = uuid;
                continue;
            }
            this.time.replace(uuid, this.time.get(uuid) - 0.05);
        }
        if (toRemove != null) {
            this.time.remove(toRemove);
            toRemove = null;
        }
        if (this.arrows.getValue().booleanValue() || this.exp.getValue().booleanValue() || this.pots.getValue().booleanValue()) {
            for (Entity e : ItemTracer.mc.world.getLoadedEntityList()) {
                if (!(e instanceof EntityArrow) && !(e instanceof EntityExpBottle) && !(e instanceof EntityPotion)) continue;
                if (!this.poses.containsKey(e.getUniqueID())) {
                    this.poses.put(e.getUniqueID(), new ArrayList<Vec3d>(Collections.singletonList(e.getPositionVector())));
                    this.time.put(e.getUniqueID(), 0.05);
                    continue;
                }
                this.time.replace(e.getUniqueID(), 0.05);
                v = this.poses.get(e.getUniqueID());
                v.add(e.getPositionVector());
            }
        }
        for (Entity e : ItemTracer.mc.world.getLoadedEntityList()) {
            if (!(e instanceof EntityEnderPearl)) continue;
            if (!this.poses.containsKey(e.getUniqueID())) {
                if (this.chat.getValue().booleanValue()) {
                    for (EntityPlayer entityPlayer : ItemTracer.mc.world.playerEntities) {
                        if (!(entityPlayer.getDistance(e) < 4.0f)) continue;
                        if (entityPlayer.getName().equals(ItemTracer.mc.player.getName())) continue;
                        ClientMessage.sendMessage(entityPlayer.getName() + " just threw a pearl!");
                        break;
                    }
                }
                this.poses.put(e.getUniqueID(), new ArrayList<Vec3d>(Collections.singletonList(e.getPositionVector())));
                this.time.put(e.getUniqueID(), this.aliveTime.getValue());
                continue;
            }
            this.time.replace(e.getUniqueID(), this.aliveTime.getValue());
            v = this.poses.get(e.getUniqueID());
            v.add(e.getPositionVector());
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!this.render.getValue().booleanValue() && !this.poses.isEmpty()) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)this.thick.getAsFloat().floatValue());
        for (UUID uuid : this.poses.keySet()) {
            if (this.poses.get(uuid).size() <= 2) continue;
            int delay = 0;
            GL11.glBegin((int)1);
            for (int i = 1; i < this.poses.get(uuid).size(); ++i) {
                Colour c = this.color.getRainbow() ? ColorUtil.releasedDynamicRainbow(delay += this.rDelay.getValue().intValue(), this.color.getValue().getAlpha()) : this.color.getValue();
                GL11.glColor4d((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f), (double)((float)c.getAlpha() / 255.0f));
                List<Vec3d> pos = this.poses.get(uuid);
                GL11.glVertex3d((double)(pos.get((int)i).x - ItemTracer.mc.getRenderManager().viewerPosX), (double)(pos.get((int)i).y - ItemTracer.mc.getRenderManager().viewerPosY), (double)(pos.get((int)i).z - ItemTracer.mc.getRenderManager().viewerPosZ));
                GL11.glVertex3d((double)(pos.get((int)(i - 1)).x - ItemTracer.mc.getRenderManager().viewerPosX), (double)(pos.get((int)(i - 1)).y - ItemTracer.mc.getRenderManager().viewerPosY), (double)(pos.get((int)(i - 1)).z - ItemTracer.mc.getRenderManager().viewerPosZ));
            }
            GL11.glEnd();
        }
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }
}

