/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.player;

import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.PushEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;

@Hack.Registration(name="Velocity", description="Anti KB", category=Hack.Category.PLAYER, priority=HackPriority.Low)
public final class Velocity
extends Hack {
    public static Velocity INSTANCE;
    IntSetting v = new IntSetting("Vertical", 0, 0, 100, this);
    IntSetting h = new IntSetting("Horizontal", 0, 0, 100, this);
    BooleanSetting explosions = new BooleanSetting("Explosions", (Boolean)true, (Hack)this);
    BooleanSetting fishHook = new BooleanSetting("Fish Hook", (Boolean)true, (Hack)this);
    public BooleanSetting noPush = new BooleanSetting("No Push", (Boolean)true, (Hack)this);

    public Velocity() {
        INSTANCE = this;
    }

    @CommitEvent
    public void pushEvent(PushEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (!this.noPush.getValue().booleanValue()) {
            return;
        }
        if (event.getStage() == 0) {
            event.x *= 0.0;
            event.y = 0.0;
            event.z *= 0.0;
        }
        if (event.getStage() == 1) {
            event.setCancelled(true);
        }
        if (event.getStage() == 2 && event.entity == Velocity.mc.player) {
            event.setCancelled(true);
        }
    }

    @CommitEvent
    public void SPacket(PacketEvent.Receive event) {
        Entity entity;
        SPacketEntityVelocity packet;
        if (this.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus && this.fishHook.getValue().booleanValue() && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 31 && (entity = packet.getEntity((World)Minecraft.getMinecraft().world)) != null && entity instanceof EntityFishHook) {
            EntityFishHook fishHook = (EntityFishHook)entity;
            if (fishHook.caughtEntity == Minecraft.getMinecraft().player) {
                event.setCancelled(true);
            }
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && (packet = (SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
            if (this.h.getValue() == 0 && this.v.getValue() == 0) {
                event.setCancelled(true);
                return;
            }
            if (this.h.getValue() != 100) {
                packet.motionX = packet.motionX / 100 * this.h.getValue();
                packet.motionZ = packet.motionZ / 100 * this.h.getValue();
            }
            if (this.v.getValue() != 100) {
                packet.motionY = packet.motionY / 100 * this.v.getValue();
            }
        }
        if (event.getPacket() instanceof SPacketExplosion && this.explosions.getValue().booleanValue()) {
            packet = (SPacketExplosion)event.getPacket();
            if (this.h.getValue() == 0 && this.v.getValue() == 0) {
                packet.motionX *= 0.0f;
                packet.motionY *= 0.0f;
                packet.motionZ *= 0.0f;
                return;
            }
            if (this.h.getValue() != 100) {
                packet.motionX = packet.motionX / 100.0f * (float)this.h.getValue().intValue();
                packet.motionZ = packet.motionZ / 100.0f * (float)this.h.getValue().intValue();
            }
            if (this.v.getValue() != 100) {
                packet.motionY = packet.motionY / 100.0f * (float)this.v.getValue().intValue();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.format("H:%s%% V:%s%%", this.h.getValue(), this.v.getValue());
    }
}

