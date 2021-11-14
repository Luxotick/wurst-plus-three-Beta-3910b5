/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.Timer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.lang.reflect.Field;
import java.util.Arrays;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.hack.hacks.combat.CrystalAura;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.MappingUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Burrow", description="fills ur lower-half with a block", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public class Burrow
extends Hack {
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)true, (Hack)this);
    EnumSetting type = new EnumSetting("Type", "Packet", Arrays.asList("Packet", "Normal"), this);
    EnumSetting block = new EnumSetting("Block", "All", Arrays.asList("All", "EChest", "Chest", "WhiteList"), this);
    DoubleSetting force = new DoubleSetting("Force", (Double)1.5, (Double)-5.0, (Double)10.0, this);
    BooleanSetting instant = new BooleanSetting("Instant", (Boolean)true, (Hack)this, s -> this.type.is("Normal"));
    BooleanSetting center = new BooleanSetting("Center", (Boolean)false, (Hack)this);
    BooleanSetting bypass = new BooleanSetting("Bypass", (Boolean)false, (Hack)this);
    int swapBlock = -1;
    Vec3d centerBlock = Vec3d.ZERO;
    BlockPos oldPos;
    Block blockW = Blocks.OBSIDIAN;
    boolean flag;

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        this.flag = false;
        if (CrystalAura.INSTANCE.isEnabled()) {
            this.flag = true;
            CrystalAura.INSTANCE.disable();
        }
        Burrow.mc.player.motionX = 0.0;
        Burrow.mc.player.motionZ = 0.0;
        this.centerBlock = this.getCenter(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        if (this.centerBlock != Vec3d.ZERO && this.center.getValue().booleanValue()) {
            double x_diff = Math.abs(this.centerBlock.x - Burrow.mc.player.posX);
            double z_diff = Math.abs(this.centerBlock.z - Burrow.mc.player.posZ);
            if (x_diff <= 0.1 && z_diff <= 0.1) {
                this.centerBlock = Vec3d.ZERO;
            } else {
                double motion_x = this.centerBlock.x - Burrow.mc.player.posX;
                double motion_z = this.centerBlock.z - Burrow.mc.player.posZ;
                Burrow.mc.player.motionX = motion_x / 2.0;
                Burrow.mc.player.motionZ = motion_z / 2.0;
            }
        }
        this.oldPos = PlayerUtil.getPlayerPos();
        switch (this.block.getValue()) {
            case "All": {
                this.swapBlock = PlayerUtil.findObiInHotbar();
                break;
            }
            case "EChest": {
                this.swapBlock = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
                break;
            }
            case "Chest": {
                this.swapBlock = InventoryUtil.findHotbarBlock(BlockChest.class);
                break;
            }
            case "WhiteList": {
                this.swapBlock = InventoryUtil.findHotbarBlock(this.blockW.getClass());
            }
        }
        if (this.swapBlock == -1) {
            this.disable();
            return;
        }
        if (this.instant.getValue().booleanValue()) {
            this.setTimer(50.0f);
        }
        if (this.type.is("Normal")) {
            Burrow.mc.player.jump();
        }
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.type.is("Normal")) {
            if (Burrow.mc.player.posY > (double)this.oldPos.getY() + 1.04) {
                int old = Burrow.mc.player.inventory.currentItem;
                this.switchToSlot(this.swapBlock);
                BlockUtil.placeBlock(this.oldPos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), true, false);
                this.switchToSlot(old);
                Burrow.mc.player.motionY = (Double)this.force.value;
                this.disable();
            }
        } else {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821, Burrow.mc.player.posZ, true));
            int old = Burrow.mc.player.inventory.currentItem;
            this.switchToSlot(this.swapBlock);
            BlockUtil.placeBlock(this.oldPos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), true, false);
            this.switchToSlot(old);
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.force.getValue(), Burrow.mc.player.posZ, false));
            if (this.bypass.getValue().booleanValue() && !Burrow.mc.player.isSneaking()) {
                Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                Burrow.mc.player.setSneaking(true);
                Burrow.mc.playerController.updateController();
                Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                Burrow.mc.player.setSneaking(false);
                Burrow.mc.playerController.updateController();
            }
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (this.instant.getValue().booleanValue() && !this.nullCheck()) {
            this.setTimer(1.0f);
        }
        if (this.flag) {
            CrystalAura.INSTANCE.enable();
        }
    }

    private void switchToSlot(int slot) {
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        Burrow.mc.player.inventory.currentItem = slot;
        Burrow.mc.playerController.updateController();
    }

    private void setTimer(float value) {
        try {
            Field timer = Minecraft.class.getDeclaredField(MappingUtil.timer);
            timer.setAccessible(true);
            Field tickLength = Timer.class.getDeclaredField(MappingUtil.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get((Object)mc), 50.0f / value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5;
        return new Vec3d(x, y, z);
    }

    @Override
    public String getDisplayInfo() {
        return this.type.getValue();
    }

    public void setBlock(Block b) {
        this.blockW = b;
    }

    public Block getBlock() {
        return this.blockW;
    }
}

