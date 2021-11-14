/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Trap", description="traps people", category=Hack.Category.COMBAT, priority=HackPriority.High)
public class Trap
extends Hack {
    EnumSetting mode = new EnumSetting("Mode", "Extra", Arrays.asList("Extra", "Face", "Normal", "Feet", "Sand"), this);
    IntSetting blocksPerTick = new IntSetting("Speed", 4, 0, 8, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)true, (Hack)this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);
    private final Vec3d[] offsetsDefault = new Vec3d[]{new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0)};
    private final Vec3d[] offsetsFace = new Vec3d[]{new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0)};
    private final Vec3d[] offsetsFeet = new Vec3d[]{new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0)};
    private final Vec3d[] offsetsExtra = new Vec3d[]{new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0)};
    private int offsetStep = 0;
    private int yLevel;
    private String lastTickTargetName = "";
    private boolean firstRun = true;

    @Override
    public void onEnable() {
        this.yLevel = (int)Math.round(Trap.mc.player.posY);
        this.firstRun = true;
        if (PlayerUtil.findObiInHotbar() == -1) {
            this.disable();
        }
        if (PlayerUtil.findSandInHotbar() == -1 && this.mode.is("Sand")) {
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        EntityPlayer closest_target = this.findClosestTarget();
        if (closest_target == null) {
            this.disable();
            return;
        }
        if ((int)Math.round(Trap.mc.player.posY) != this.yLevel) {
            this.disable();
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTickTargetName = closest_target.getName();
        } else if (!this.lastTickTargetName.equals(closest_target.getName())) {
            this.lastTickTargetName = closest_target.getName();
            this.offsetStep = 0;
        }
        ArrayList place_targets = new ArrayList();
        if (this.mode.is("Normal") || this.mode.is("Sand")) {
            Collections.addAll(place_targets, this.offsetsDefault);
        } else if (this.mode.is("Extra")) {
            Collections.addAll(place_targets, this.offsetsExtra);
        } else if (this.mode.is("Feet")) {
            Collections.addAll(place_targets, this.offsetsFeet);
        } else {
            Collections.addAll(place_targets, this.offsetsFace);
        }
        int blocks_placed = 0;
        while (blocks_placed < this.blocksPerTick.getValue()) {
            boolean shouldSandPlace = false;
            if (this.offsetStep >= place_targets.size()) {
                this.offsetStep = 0;
                break;
            }
            if (this.mode.is("Sand") && this.offsetStep == 16) {
                shouldSandPlace = true;
            }
            BlockPos offset_pos = new BlockPos((Vec3d)place_targets.get(this.offsetStep));
            BlockPos target_pos = new BlockPos(closest_target.getPositionVector()).down().add(offset_pos.getX(), offset_pos.getY(), offset_pos.getZ());
            boolean should_try_place = true;
            if (!Trap.mc.world.getBlockState(target_pos).getMaterial().isReplaceable()) {
                should_try_place = false;
            }
            for (Entity entity : Trap.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(target_pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                should_try_place = false;
                break;
            }
            if (should_try_place && BlockUtil.placeBlock(target_pos, shouldSandPlace ? PlayerUtil.findSandInHotbar() : PlayerUtil.findObiInHotbar(), (boolean)this.rotate.getValue(), (boolean)this.rotate.getValue(), this.swing)) {
                ++blocks_placed;
            }
            ++this.offsetStep;
        }
    }

    public EntityPlayer findClosestTarget() {
        if (Trap.mc.world.playerEntities.isEmpty()) {
            return null;
        }
        EntityPlayer closestTarget = null;
        for (EntityPlayer target : Trap.mc.world.playerEntities) {
            if (target == Trap.mc.player || !target.isEntityAlive() || WurstplusThree.FRIEND_MANAGER.isFriend(target.getName()) || target.getHealth() <= 0.0f || closestTarget != null && Trap.mc.player.getDistance((Entity)target) > Trap.mc.player.getDistance((Entity)closestTarget)) continue;
            closestTarget = target;
        }
        return closestTarget;
    }
}

