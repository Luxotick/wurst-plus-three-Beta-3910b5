/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.player;

import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.PushEvent;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Phase", description="bots use this", category=Hack.Category.PLAYER, priority=HackPriority.Highest)
public class Phase
extends Hack {
    DoubleSetting loops = new DoubleSetting("Loops", (Double)0.5, (Double)0.0, (Double)5.0, this);
    BooleanSetting bypass = new BooleanSetting("Bypass", (Boolean)true, (Hack)this);
    private final Set<CPacketPlayer> packets = new ConcurrentSet();
    private final Map<Integer, IDtime> teleportmap = new ConcurrentHashMap<Integer, IDtime>();
    private int flightCounter = 0;
    private int teleportID = 0;

    @CommitEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        double speed;
        if (event.getStage() == 1) {
            return;
        }
        Phase.mc.player.setVelocity(0.0, 0.0, 0.0);
        boolean checkCollisionBoxes = this.checkHitBoxes();
        double d = Phase.mc.player.movementInput.jump && (checkCollisionBoxes || !Phase.isMoving()) ? (!checkCollisionBoxes ? (this.resetCounter(10) ? -0.032 : 0.062) : 0.062) : (Phase.mc.player.movementInput.sneak ? -0.062 : (!checkCollisionBoxes ? (this.resetCounter(4) ? -0.04 : 0.0) : (speed = 0.0)));
        if (checkCollisionBoxes && Phase.isMoving() && speed != 0.0) {
            double antiFactor = 2.5;
            speed /= antiFactor;
        }
        double[] strafing = this.getMotion(checkCollisionBoxes ? 0.031 : 0.26);
        double loops = this.bypass.getValue() != false ? this.loops.getValue() : 0.0;
        int i = 1;
        while ((double)i < loops + 1.0) {
            double extraFactor = 1.0;
            Phase.mc.player.motionX = strafing[0] * (double)i * extraFactor;
            Phase.mc.player.motionY = speed * (double)i;
            Phase.mc.player.motionZ = strafing[1] * (double)i * extraFactor;
            this.sendPackets(Phase.mc.player.motionX, Phase.mc.player.motionY, Phase.mc.player.motionZ);
            ++i;
        }
    }

    @CommitEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && !this.packets.remove(event.getPacket())) {
            event.setCancelled(true);
        }
    }

    @CommitEvent
    public void onPushOutOfBlocks(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCancelled(true);
        }
    }

    @CommitEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && !this.nullCheck()) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            if (Phase.mc.player.isEntityAlive() && Phase.mc.world.isBlockLoaded(new BlockPos(Phase.mc.player.posX, Phase.mc.player.posY, Phase.mc.player.posZ), false) && !(Phase.mc.currentScreen instanceof GuiDownloadTerrain)) {
                this.teleportmap.remove(packet.getTeleportId());
            }
            this.teleportID = packet.getTeleportId();
        }
    }

    private boolean resetCounter(int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = Phase.mc.player.movementInput.moveForward;
        float moveStrafe = Phase.mc.player.movementInput.moveStrafe;
        float rotationYaw = Phase.mc.player.prevRotationYaw + (Phase.mc.player.rotationYaw - Phase.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private void sendPackets(double x, double y, double z) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = Phase.mc.player.getPositionVector().add(vec);
        Vec3d outOfBoundsVec = this.outOfBoundsVec(position);
        this.packetSender((CPacketPlayer)new CPacketPlayer.Position(position.x, position.y, position.z, Phase.mc.player.onGround));
        this.packetSender((CPacketPlayer)new CPacketPlayer.Position(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, Phase.mc.player.onGround));
        this.teleportPacket(position);
    }

    private void teleportPacket(Vec3d pos) {
        Phase.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(++this.teleportID));
        this.teleportmap.put(this.teleportID, new IDtime(pos, new Timer()));
    }

    private Vec3d outOfBoundsVec(Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        this.packets.add(packet);
        Phase.mc.player.connection.sendPacket((Packet)packet);
    }

    private boolean checkHitBoxes() {
        return !Phase.mc.world.getCollisionBoxes((Entity)Phase.mc.player, Phase.mc.player.getEntityBoundingBox().expand(-0.0, -0.1, -0.0)).isEmpty();
    }

    public static boolean isMoving() {
        return (double)Phase.mc.player.moveForward != 0.0 || (double)Phase.mc.player.moveStrafing != 0.0;
    }

    public static class IDtime {
        private final Vec3d pos;
        private final Timer timer;

        public IDtime(Vec3d pos, Timer timer) {
            this.pos = pos;
            this.timer = timer;
            this.timer.reset();
        }

        public Vec3d getPos() {
            return this.pos;
        }

        public Timer getTimer() {
            return this.timer;
        }
    }
}

