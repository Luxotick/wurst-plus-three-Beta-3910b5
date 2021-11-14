/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import io.netty.util.internal.ConcurrentSet;
import java.util.List;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Hack.Registration(name="Anti Void", description="stops dumb ppl (you) falling into the void", category=Hack.Category.MISC, priority=HackPriority.High)
public class AntiVoid
extends Hack {
    private ConcurrentSet<BlockPos> voidHoles;

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (AntiVoid.mc.player.getPosition().getY() > 1) {
            return;
        }
        if (this.voidHoles == null) {
            this.voidHoles = new ConcurrentSet();
        } else {
            this.voidHoles.clear();
        }
        List<BlockPos> blockPosList = PlayerUtil.getSphere(PlayerUtil.getPlayerPos(), 10.0f, 6, false, true, 0);
        for (BlockPos blockPos : blockPosList) {
            if (AntiVoid.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.BEDROCK) || AntiVoid.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.OBSIDIAN) || this.isAnyBedrock(blockPos, Offsets.center)) continue;
            this.voidHoles.add((Object)blockPos);
        }
        if (this.voidHoles.contains((Object)new BlockPos(PlayerUtil.getPlayerPos().getX(), PlayerUtil.getPlayerPos().getY() - 2, PlayerUtil.getPlayerPos().getZ()))) {
            AntiVoid.mc.player.motionY = 0.1;
        }
    }

    private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
        for (BlockPos pos : offset) {
            if (!AntiVoid.mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals((Object)Blocks.BEDROCK) && !AntiVoid.mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals((Object)Blocks.OBSIDIAN)) continue;
            return true;
        }
        return false;
    }

    private static class Offsets {
        static final BlockPos[] center = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};

        private Offsets() {
        }
    }
}

