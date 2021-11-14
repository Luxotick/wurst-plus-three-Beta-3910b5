/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  baritone.api.BaritoneAPI
 *  baritone.api.pathing.goals.Goal
 *  baritone.api.pathing.goals.GoalBlock
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.HoleUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;

@Hack.Registration(name="Hole Blink", description="walks to hole", category=Hack.Category.COMBAT, priority=HackPriority.High)
public class HoleBlink
extends Hack {
    private BooleanSetting blink = new BooleanSetting("Blink", (Boolean)true, (Hack)this);
    private DoubleSetting range = new DoubleSetting("MaxRange", (Double)2.0, (Double)0.0, (Double)20.0, this);
    private EnumSetting priority = new EnumSetting("Priority", "Middle", Arrays.asList("Close", "Far", "Middle"), this);
    BlockPos target;

    @Override
    public void onEnable() {
        if (BaritoneAPI.getProvider().getPrimaryBaritone() == null) {
            ClientMessage.sendMessage("No baritone found, get baritone on");
            ClientMessage.sendMessage("https://github.com/cabaletta/baritone");
            this.disable();
        }
        this.target = null;
        for (BlockPos pos : EntityUtil.getSphere(HoleBlink.mc.player.getPosition(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0)) {
            if (!(this.getBlock(pos) instanceof BlockAir) || !(this.getBlock(pos.up()) instanceof BlockAir) || !(this.getBlock(pos.up().up()) instanceof BlockAir) || HoleUtil.isHole(pos, true, false).getType() == HoleUtil.HoleType.NONE || PlayerUtil.getPlayerPos().getX() == pos.getX() || PlayerUtil.getPlayerPos().getY() == pos.getY() || PlayerUtil.getPlayerPos().getZ() == pos.getZ()) continue;
            if (this.priority.is("Far")) {
                if (this.target != null && (!(HoleBlink.mc.player.getDistanceSq(this.target) < HoleBlink.mc.player.getDistanceSq(pos)) || !(Math.sqrt(HoleBlink.mc.player.getDistanceSq(pos)) < this.range.getValue()))) continue;
                this.target = pos;
                continue;
            }
            if (this.priority.is("Close")) {
                if (this.target != null && !(HoleBlink.mc.player.getDistanceSq(this.target) > HoleBlink.mc.player.getDistanceSq(pos))) continue;
                this.target = pos;
                continue;
            }
            if (!this.priority.is("Middle") || this.target != null && !(Math.abs(Math.sqrt(HoleBlink.mc.player.getDistanceSq(pos)) - this.range.getValue() / 2.0) < Math.abs(Math.sqrt(HoleBlink.mc.player.getDistanceSq(this.target)) - this.range.getValue() / 2.0))) continue;
            this.target = pos;
        }
        if (this.target != null) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)new GoalBlock(this.target));
            if (this.blink.getValue().booleanValue()) {
                WurstplusThree.HACKS.enablehack("Blink");
            }
        } else {
            ClientMessage.sendMessage("Couldn't find hole");
            this.disable();
        }
    }

    private Block getBlock(BlockPos pos) {
        return HoleBlink.mc.world.getBlockState(pos).getBlock();
    }

    @Override
    public void onDisable() {
        if (this.blink.getValue().booleanValue()) {
            WurstplusThree.HACKS.disablehack("Blink");
        }
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive()) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
        }
    }

    @Override
    public void onUpdate() {
        if (!BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive()) {
            ClientMessage.sendMessage("Reached target... or failed to reach it idk");
            this.disable();
            return;
        }
    }
}

