/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.HoleUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Hack.Registration(name="Hole Fill", description="fills holes", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public class HoleFill
extends Hack {
    IntSetting range = new IntSetting("Range", 3, 1, 6, this);
    IntSetting holesPerSecond = new IntSetting("HPS", 3, 1, 6, this);
    EnumSetting fillMode = new EnumSetting("Mode", "Normal", Arrays.asList("Normal", "Smart", "Auto"), this);
    IntSetting smartRange = new IntSetting("AutoRange", 2, 1, 5, this);
    BooleanSetting doubleHoles = new BooleanSetting("DoubleFill", (Boolean)false, (Hack)this);
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)true, (Hack)this);
    BooleanSetting toggle = new BooleanSetting("Toggle", (Boolean)false, (Hack)this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);

    @Override
    public void onUpdate() {
        List<BlockPos> holes = this.findHoles();
        BlockPos posToFill = null;
        if (holes.isEmpty() && this.toggle.getValue().booleanValue()) {
            this.disable();
            return;
        }
        for (int i = 0; i < this.holesPerSecond.getValue(); ++i) {
            double bestDistance = 10.0;
            for (BlockPos pos : new ArrayList<BlockPos>(holes)) {
                BlockUtil.ValidResult result = BlockUtil.valid(pos);
                if (result != BlockUtil.ValidResult.Ok) {
                    holes.remove((Object)pos);
                    continue;
                }
                if (!this.fillMode.is("Normal")) {
                    for (EntityPlayer target : HoleFill.mc.world.playerEntities) {
                        double distance = target.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                        if (target == HoleFill.mc.player || WurstplusThree.FRIEND_MANAGER.isFriend(HoleFill.mc.player.getName()) || distance > (double)(this.fillMode.is("Auto") ? this.smartRange.getValue() : 5) || !(distance < bestDistance)) continue;
                        posToFill = pos;
                    }
                    continue;
                }
                posToFill = pos;
            }
            if (PlayerUtil.findObiInHotbar() == -1) {
                return;
            }
            if (posToFill == null) continue;
            BlockUtil.placeBlock(posToFill, PlayerUtil.findObiInHotbar(), (boolean)this.rotate.getValue(), (boolean)this.rotate.getValue(), this.swing);
            holes.remove((Object)posToFill);
        }
    }

    public List<BlockPos> findHoles() {
        int range = this.range.getValue();
        HashSet possibleHoles = Sets.newHashSet();
        List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), range, range, false, true, 0);
        ArrayList<BlockPos> holes = new ArrayList<BlockPos>();
        for (BlockPos pos : blockPosList) {
            if (!HoleFill.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) || HoleFill.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleFill.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleFill.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR)) continue;
            possibleHoles.add(pos);
        }
        for (BlockPos pos : possibleHoles) {
            AxisAlignedBB centreBlocks;
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType == HoleUtil.HoleType.NONE || (centreBlocks = holeInfo.getCentre()) == null || holeType == HoleUtil.HoleType.DOUBLE && !this.doubleHoles.getValue().booleanValue()) continue;
            holes.add(pos);
        }
        return holes;
    }
}

