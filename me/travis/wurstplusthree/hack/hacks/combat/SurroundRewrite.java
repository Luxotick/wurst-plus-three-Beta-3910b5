/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.ArrayList;
import java.util.Arrays;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

@Hack.Registration(name="Surround Rewrite", description="Hole maker", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public class SurroundRewrite
extends Hack {
    BooleanSetting smart = new BooleanSetting("Smart", (Boolean)true, (Hack)this);
    BooleanSetting center = new BooleanSetting("Center", (Boolean)true, (Hack)this);
    BooleanSetting rot = new BooleanSetting("Rotate", (Boolean)false, (Hack)this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);
    private BlockPos startPos;
    private ArrayList<BlockPos> retryPos;
    private static final BlockPos[] surroundPos = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0), new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            return;
        }
        this.startPos = PlayerUtil.getPlayerPos();
        this.retryPos = new ArrayList();
        if (this.center.getValue().booleanValue()) {
            double y = SurroundRewrite.mc.player.getPosition().getY();
            double x = SurroundRewrite.mc.player.getPosition().getX();
            double z = SurroundRewrite.mc.player.getPosition().getZ();
            Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
            Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
            Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
            Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
            if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                x = (double)SurroundRewrite.mc.player.getPosition().getX() + 0.5;
                z = (double)SurroundRewrite.mc.player.getPosition().getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                x = (double)SurroundRewrite.mc.player.getPosition().getX() + 0.5;
                z = (double)SurroundRewrite.mc.player.getPosition().getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                x = (double)SurroundRewrite.mc.player.getPosition().getX() - 0.5;
                z = (double)SurroundRewrite.mc.player.getPosition().getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                x = (double)SurroundRewrite.mc.player.getPosition().getX() - 0.5;
                z = (double)SurroundRewrite.mc.player.getPosition().getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
        }
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onTick() {
        if (this.nullCheck()) {
            this.toggle();
            return;
        }
        if (this.startPos != null && !this.startPos.equals((Object)PlayerUtil.getPlayerPos())) {
            this.toggle();
            return;
        }
        if (!this.retryPos.isEmpty() && this.retryPos.size() < surroundPos.length && this.smart.getValue().booleanValue()) {
            for (BlockPos pos : this.retryPos) {
                BlockPos newPos = this.addPos(pos);
                if (BlockUtil.isPositionPlaceable(newPos, false) < 2) continue;
                int slot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                if (slot == -1) {
                    this.toggle();
                }
                if (!BlockUtil.placeBlock(newPos, slot, (boolean)this.rot.getValue(), (boolean)this.rot.getValue(), this.swing)) continue;
                this.retryPos.remove((Object)newPos);
            }
            return;
        }
        for (BlockPos pos : surroundPos) {
            BlockPos newPos = this.addPos(pos);
            if (BlockUtil.isPositionPlaceable(newPos, false) < 2) continue;
            int slot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            if (slot == -1) {
                this.toggle();
            }
            if (BlockUtil.placeBlock(newPos, slot, (boolean)this.rot.getValue(), (boolean)this.rot.getValue(), this.swing)) continue;
            this.retryPos.add(newPos);
        }
    }

    private BlockPos addPos(@NotNull BlockPos pos) {
        BlockPos pPos = PlayerUtil.getPlayerPos(0.2);
        return new BlockPos(pPos.getX() + pos.getX(), pPos.getY() + pos.getY(), pPos.getZ() + pos.getZ());
    }

    private double getDst(Vec3d vec) {
        return SurroundRewrite.mc.player.getPositionVector().distanceTo(vec);
    }

    private void centerPlayer(double x, double y, double z) {
        SurroundRewrite.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
        SurroundRewrite.mc.player.setPosition(x, y, z);
    }
}

