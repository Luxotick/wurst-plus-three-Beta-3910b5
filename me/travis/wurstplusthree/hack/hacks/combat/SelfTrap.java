/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.Arrays;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Self Trap", description="when all else fails u can self trap", category=Hack.Category.COMBAT, isListening=false)
public class SelfTrap
extends Hack {
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)false, (Hack)this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);
    private BlockPos trapPos;

    @Override
    public void onEnable() {
        if (this.findInHotbar() == -1) {
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        BlockUtil.ValidResult result;
        Vec3d pos = EntityUtil.interpolateEntity((Entity)SelfTrap.mc.player, mc.getRenderPartialTicks());
        this.trapPos = new BlockPos(pos.x, pos.y + 2.0, pos.z);
        if (this.isTrapped()) {
            if (this.isEnabled()) {
                this.disable();
            } else {
                this.enable();
                return;
            }
        }
        if ((result = BlockUtil.valid(this.trapPos)) == BlockUtil.ValidResult.AlreadyBlockThere && !SelfTrap.mc.world.getBlockState(this.trapPos).getMaterial().isReplaceable()) {
            return;
        }
        if (result == BlockUtil.ValidResult.NoNeighbors) {
            BlockPos[] tests;
            for (BlockPos pos_ : tests = new BlockPos[]{this.trapPos.north(), this.trapPos.south(), this.trapPos.east(), this.trapPos.west(), this.trapPos.up(), this.trapPos.down().west()}) {
                BlockUtil.ValidResult result_ = BlockUtil.valid(pos_);
                if (result_ == BlockUtil.ValidResult.NoNeighbors || result_ == BlockUtil.ValidResult.NoEntityCollision || !BlockUtil.placeBlock(pos_, this.findInHotbar(), (boolean)this.rotate.getValue(), (boolean)this.rotate.getValue(), this.swing)) continue;
                return;
            }
            return;
        }
        BlockUtil.placeBlock(this.trapPos, this.findInHotbar(), (boolean)this.rotate.getValue(), (boolean)this.rotate.getValue(), this.swing);
    }

    public boolean isTrapped() {
        if (this.trapPos == null) {
            return false;
        }
        IBlockState state = SelfTrap.mc.world.getBlockState(this.trapPos);
        return state.getBlock() != Blocks.AIR && state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.LAVA;
    }

    private int findInHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = SelfTrap.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockEnderChest) {
                return i;
            }
            if (!(block instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }
}

