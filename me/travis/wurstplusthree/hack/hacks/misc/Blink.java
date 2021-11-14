/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketTabComplete
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.MathsUtil;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Hack.Registration(name="Blink", description="allows u to move without being seen", category=Hack.Category.MISC, isListening=false)
public class Blink
extends Hack {
    public static Blink INSTANCE;
    public EnumSetting mode = new EnumSetting("mode", "Manual", Arrays.asList("Manual", "Time", "Distance", "Packets"), this);
    public BooleanSetting cPacketPlayer = new BooleanSetting("CPacketPlayer", (Boolean)true, (Hack)this);
    public IntSetting timeLimit = new IntSetting("TimeLimit", 20, 1, 500, this, s -> this.mode.is("Time"));
    public IntSetting packetLimit = new IntSetting("PacketLimit", 20, 1, 500, this, s -> this.mode.is("Packets"));
    public DoubleSetting distance = new DoubleSetting("Distance", (Double)10.0, (Double)1.0, (Double)100.0, this, s -> this.mode.is("Distance"));
    private final Timer timer = new Timer();
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue();
    private EntityOtherPlayerMP entity;
    private int packetsCanceled = 0;
    private BlockPos startPos = null;

    public Blink() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (!this.nullCheck()) {
            this.entity = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.session.getProfile());
            this.entity.copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.entity.rotationYaw = Blink.mc.player.rotationYaw;
            this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Blink.mc.player.inventory);
            Blink.mc.world.addEntityToWorld(6942069, (Entity)this.entity);
            this.startPos = Blink.mc.player.getPosition();
        } else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck() || this.mode.is("Time") && this.timer.passedS(this.timeLimit.getValue().intValue()) || this.mode.is("Distance") && this.startPos != null && Blink.mc.player.getDistanceSq(this.startPos) >= MathsUtil.square(this.distance.getValue().floatValue()) || this.mode.is("Packets") && this.packetsCanceled >= this.packetLimit.getValue()) {
            this.disable();
        }
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @CommitEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getStage() == 0 && Blink.mc.world != null && !mc.isSingleplayer()) {
            Object packet = event.getPacket();
            if (this.cPacketPlayer.getValue().booleanValue() && packet instanceof CPacketPlayer) {
                event.setCancelled(true);
                this.packets.add((Packet)packet);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue().booleanValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add((Packet)packet);
                event.setCancelled(true);
                ++this.packetsCanceled;
            }
        }
    }

    @Override
    public void onDisable() {
        if (!this.nullCheck() && this.entity != null) {
            Blink.mc.world.removeEntity((Entity)this.entity);
            while (!this.packets.isEmpty()) {
                Blink.mc.player.connection.sendPacket(this.packets.poll());
            }
        }
        this.startPos = null;
    }
}

