/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.travis.wurstplusthree.hack.hacks.player;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

@Hack.Registration(name="Step", description="steps up things", category=Hack.Category.PLAYER)
public class Step
extends Hack {
    BooleanSetting vanilla = new BooleanSetting("Vanilla", (Boolean)false, (Hack)this);

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (!(!Step.mc.player.onGround || Step.mc.player.isInsideOfMaterial(Material.WATER) || Step.mc.player.isInsideOfMaterial(Material.LAVA) || !Step.mc.player.collidedVertically || Step.mc.player.fallDistance != 0.0f || Step.mc.gameSettings.keyBindJump.pressed || Step.mc.player.isOnLadder() || WurstplusThree.HACKS.ishackEnabled("Speed") || this.vanilla.getValue().booleanValue())) {
            double n = this.getN();
            if (n < 0.0 || n > 2.0) {
                return;
            }
            if (n == 2.0) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.42, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.78, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.63, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.51, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.9, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.21, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.45, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.43, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.0, Step.mc.player.posZ);
            }
            if (n == 1.5) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.41999998688698, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.7531999805212, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.00133597911214, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.16610926093821, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.24918707874468, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 1.1707870772188, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 1.0, Step.mc.player.posZ);
            }
            if (n == 1.0) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.41999998688698, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + 0.7531999805212, Step.mc.player.posZ, Step.mc.player.onGround));
                Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 1.0, Step.mc.player.posZ);
            }
        }
    }

    @Override
    public void onEnable() {
        if (this.vanilla.getValue().booleanValue()) {
            Step.mc.player.stepHeight = 2.0f;
        }
    }

    @Override
    public void onDisable() {
        Step.mc.player.stepHeight = 0.6f;
    }

    private double getN() {
        Step.mc.player.stepHeight = 0.5f;
        double max_y = -1.0;
        AxisAlignedBB grow = Step.mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
        if (!Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, grow.offset(0.0, 2.0, 0.0)).isEmpty()) {
            return 100.0;
        }
        for (AxisAlignedBB aabb : Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, grow)) {
            if (!(aabb.maxY > max_y)) continue;
            max_y = aabb.maxY;
        }
        return max_y - Step.mc.player.posY;
    }
}

