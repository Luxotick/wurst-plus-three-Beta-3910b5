/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Surround", description="surrounds u", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public class Surround
extends Hack {
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)true, (Hack)this);
    BooleanSetting hybrid = new BooleanSetting("Hybrid", (Boolean)true, (Hack)this);
    BooleanSetting packet = new BooleanSetting("Packet", (Boolean)true, (Hack)this);
    BooleanSetting center = new BooleanSetting("Center", (Boolean)true, (Hack)this);
    BooleanSetting blockHead = new BooleanSetting("Block Face", (Boolean)false, (Hack)this);
    IntSetting tickForPlace = new IntSetting("Blocks Per Tick", 2, 1, 8, this);
    IntSetting timeoutTicks = new IntSetting("Timeout Ticks", 20, 0, 50, this);
    private int yLevel = 0;
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private BlockPos startPos;
    private boolean didPlace = false;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private boolean offHand = false;
    private int ticksPassed = 0;

    @Override
    public void onEnable() {
        if (Surround.mc.player != null) {
            this.yLevel = (int)Math.round(Surround.mc.player.posY);
            this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
            if (this.center.getValue().booleanValue()) {
                double y = Surround.mc.player.getPosition().getY();
                double x = Surround.mc.player.getPosition().getX();
                double z = Surround.mc.player.getPosition().getZ();
                Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
                Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
                Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
                Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
                if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                    x = (double)Surround.mc.player.getPosition().getX() + 0.5;
                    z = (double)Surround.mc.player.getPosition().getZ() + 0.5;
                    this.centerPlayer(x, y, z);
                }
                if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                    x = (double)Surround.mc.player.getPosition().getX() + 0.5;
                    z = (double)Surround.mc.player.getPosition().getZ() - 0.5;
                    this.centerPlayer(x, y, z);
                }
                if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                    x = (double)Surround.mc.player.getPosition().getX() - 0.5;
                    z = (double)Surround.mc.player.getPosition().getZ() - 0.5;
                    this.centerPlayer(x, y, z);
                }
                if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                    x = (double)Surround.mc.player.getPosition().getX() - 0.5;
                    z = (double)Surround.mc.player.getPosition().getZ() + 0.5;
                    this.centerPlayer(x, y, z);
                }
            }
            this.ticksPassed = 0;
            this.retries.clear();
            this.retryTimer.reset();
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
    }

    @Override
    public void onTick() {
        if ((int)Math.round(Surround.mc.player.posY) != this.yLevel && this.hybrid.getValue().booleanValue()) {
            this.disable();
            return;
        }
        this.doFeetPlace();
        ++this.ticksPassed;
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtil.isSafe((Entity)Surround.mc.player, 0, true)) {
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true), true, false, false);
        } else if (!EntityUtil.isSafe((Entity)Surround.mc.player, -1, false)) {
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, -1, false), false, false, true);
        }
        if (this.blockHead.getValue().booleanValue()) {
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 1, false), false, false, false);
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d extendingBlock;
                array[i] = extendingBlock = iterator.next();
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, true), true, false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true)) {
                if (!vec3d.equals((Object)pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        block5: for (Vec3d vec3d : vec3ds) {
            boolean gotHelp = true;
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get((Object)position) == null || this.retries.get((Object)position) < 4) {
                        this.placeBlock(position);
                        this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
                        this.retryTimer.reset();
                        continue block5;
                    }
                    if (Surround.mc.player.motionX != 0.0 || Surround.mc.player.motionZ != 0.0 || isExtending || this.extenders >= 1) continue block5;
                    this.placeBlocks(Surround.mc.player.getPositionVector().add(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(Surround.mc.player.getPositionVector().add(vec3d), 0, true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    continue block5;
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block5;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) continue block5;
                    return true;
                }
            }
        }
        return false;
    }

    private double getDst(Vec3d vec) {
        return Surround.mc.player.getPositionVector().distanceTo(vec);
    }

    private void centerPlayer(double x, double y, double z) {
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
        Surround.mc.player.setPosition(x, y, z);
    }

    private boolean check() {
        if (this.nullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
            return true;
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        int obbySlot1 = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot1 == -1 && !this.offHand && echestSlot == -1) {
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != obbySlot1 && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player))) {
            this.disable();
            return true;
        }
        return this.ticksPassed > this.timeoutTicks.getValue() && this.hybrid.getValue() == false;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.tickForPlace.getValue()) {
            int originalSlot = Surround.mc.player.inventory.currentItem;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            Surround.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            Surround.mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), (boolean)this.packet.getValue(), this.isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            this.didPlace = true;
            ++this.placements;
        }
    }

    public Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5;
        return new Vec3d(x, y, z);
    }
}

