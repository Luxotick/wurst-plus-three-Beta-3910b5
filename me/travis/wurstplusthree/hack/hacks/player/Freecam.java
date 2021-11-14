/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketSetPassengers
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.player;

import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.PushEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.util.MathsUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Hack.Registration(name="Freecam", description="lets u see freely", category=Hack.Category.PLAYER, priority=HackPriority.Low)
public class Freecam
extends Hack {
    public DoubleSetting speed = new DoubleSetting("Speed", (Double)0.5, (Double)0.1, (Double)5.0, this);
    public BooleanSetting view = new BooleanSetting("View", (Boolean)false, (Hack)this);
    public BooleanSetting packet = new BooleanSetting("Packet", (Boolean)false, (Hack)this);
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        this.oldBoundingBox = Freecam.mc.player.getEntityBoundingBox();
        Freecam.mc.player.setEntityBoundingBox(new AxisAlignedBB(Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ, Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ));
        if (Freecam.mc.player.getRidingEntity() != null) {
            this.riding = Freecam.mc.player.getRidingEntity();
            Freecam.mc.player.dismountRidingEntity();
        }
        this.entity = new EntityOtherPlayerMP((World)Freecam.mc.world, Freecam.mc.session.getProfile());
        this.entity.copyLocationAndAnglesFrom((Entity)Freecam.mc.player);
        this.entity.rotationYaw = Freecam.mc.player.rotationYaw;
        this.entity.rotationYawHead = Freecam.mc.player.rotationYawHead;
        this.entity.inventory.copyInventory(Freecam.mc.player.inventory);
        Freecam.mc.world.addEntityToWorld(69420, (Entity)this.entity);
        this.position = Freecam.mc.player.getPositionVector();
        this.yaw = Freecam.mc.player.rotationYaw;
        this.pitch = Freecam.mc.player.rotationPitch;
        Freecam.mc.player.noClip = true;
    }

    @Override
    public void onDisable() {
        if (this.nullCheck()) {
            return;
        }
        Freecam.mc.player.setEntityBoundingBox(this.oldBoundingBox);
        if (this.riding != null) {
            Freecam.mc.player.startRiding(this.riding, true);
        }
        if (this.entity != null) {
            Freecam.mc.world.removeEntity((Entity)this.entity);
        }
        if (this.position != null) {
            Freecam.mc.player.setPosition(this.position.x, this.position.y, this.position.z);
        }
        Freecam.mc.player.rotationYaw = this.yaw;
        Freecam.mc.player.rotationPitch = this.pitch;
        Freecam.mc.player.noClip = false;
    }

    @Override
    public void onUpdate() {
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.setVelocity(0.0, 0.0, 0.0);
        Freecam.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        double[] dir = MathsUtil.directionSpeed(this.speed.getValue());
        if (Freecam.mc.player.movementInput.moveStrafe != 0.0f || Freecam.mc.player.movementInput.moveForward != 0.0f) {
            Freecam.mc.player.motionX = dir[0];
            Freecam.mc.player.motionZ = dir[1];
        } else {
            Freecam.mc.player.motionX = 0.0;
            Freecam.mc.player.motionZ = 0.0;
        }
        Freecam.mc.player.setSprinting(false);
        if (this.view.getValue().booleanValue() && !Freecam.mc.gameSettings.keyBindSneak.isKeyDown() && !Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY = this.speed.getValue() * -MathsUtil.degToRad(Freecam.mc.player.rotationPitch) * (double)Freecam.mc.player.movementInput.moveForward;
        }
        if (Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            EntityPlayerSP player = Freecam.mc.player;
            player.motionY += this.speed.getValue().doubleValue();
        }
        if (Freecam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            EntityPlayerSP player2 = Freecam.mc.player;
            player2.motionY -= this.speed.getValue().doubleValue();
        }
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPacketSend(PacketEvent.Send event) {
        if (this.packet.getValue().booleanValue()) {
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCancelled(true);
            }
        } else if (!(event.getPacket() instanceof CPacketUseEntity || event.getPacket() instanceof CPacketPlayerTryUseItem || event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock || event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketVehicleMove || event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketKeepAlive)) {
            event.setCancelled(true);
        }
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSetPassengers packet;
        Entity riding;
        if (event.getPacket() instanceof SPacketSetPassengers && (riding = Freecam.mc.world.getEntityByID((packet = (SPacketSetPassengers)event.getPacket()).getEntityId())) != null && riding == this.riding) {
            this.riding = null;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet2 = (SPacketPlayerPosLook)event.getPacket();
            if (this.packet.getValue().booleanValue()) {
                if (this.entity != null) {
                    this.entity.setPositionAndRotation(packet2.getX(), packet2.getY(), packet2.getZ(), packet2.getYaw(), packet2.getPitch());
                }
                this.position = new Vec3d(packet2.getX(), packet2.getY(), packet2.getZ());
                Freecam.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(packet2.getTeleportId()));
            }
            event.setCancelled(true);
        }
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPush(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCancelled(true);
        }
    }
}

