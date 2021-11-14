/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 */
package me.travis.wurstplusthree.util;

import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.RotationUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BotUtil
implements Globals {
    public static boolean isAheadStepable() {
        return true;
    }

    public static boolean isAheadJumpable() {
        BlockPos currentGround = PlayerUtil.getPlayerPos().down();
        Direction currentDirection = BotUtil.getDirection();
        if (!BotUtil.canJumpOverBlock(currentGround)) {
            return false;
        }
        switch (currentDirection) {
            case NORTH: {
                if (BotUtil.mc.world.getBlockState(currentGround.north()).getBlock() == Blocks.AIR) {
                    BlockPos checkPos = currentGround.north();
                    if (!BotUtil.canJumpOverBlock(checkPos)) {
                        return false;
                    }
                    for (int i = 0; i < 3; ++i) {
                        if (BotUtil.mc.world.getBlockState((checkPos = checkPos.north()).north()).getBlock() != Blocks.AIR) {
                            return true;
                        }
                        if (BotUtil.canJumpOverBlock(checkPos)) continue;
                        return false;
                    }
                }
                return false;
            }
            case EAST: {
                if (BotUtil.mc.world.getBlockState(currentGround.east()).getBlock() == Blocks.AIR) {
                    BlockPos checkPos = currentGround.east();
                    if (!BotUtil.canJumpOverBlock(checkPos)) {
                        return false;
                    }
                    for (int i = 0; i < 3; ++i) {
                        if (BotUtil.mc.world.getBlockState((checkPos = checkPos.east()).east()).getBlock() != Blocks.AIR) {
                            return true;
                        }
                        if (BotUtil.canJumpOverBlock(checkPos)) continue;
                        return false;
                    }
                }
                return false;
            }
            case SOUTH: {
                if (BotUtil.mc.world.getBlockState(currentGround.south()).getBlock() == Blocks.AIR) {
                    BlockPos checkPos = currentGround.south();
                    if (!BotUtil.canJumpOverBlock(checkPos)) {
                        return false;
                    }
                    for (int i = 0; i < 3; ++i) {
                        if (BotUtil.mc.world.getBlockState((checkPos = checkPos.south()).south()).getBlock() != Blocks.AIR) {
                            return true;
                        }
                        if (BotUtil.canJumpOverBlock(checkPos)) continue;
                        return false;
                    }
                }
                return false;
            }
            case WEST: {
                if (BotUtil.mc.world.getBlockState(currentGround.west()).getBlock() == Blocks.AIR) {
                    BlockPos checkPos = currentGround.west();
                    if (!BotUtil.canJumpOverBlock(checkPos)) {
                        return false;
                    }
                    for (int i = 0; i < 3; ++i) {
                        if (BotUtil.mc.world.getBlockState((checkPos = checkPos.west()).west()).getBlock() != Blocks.AIR) {
                            return true;
                        }
                        if (BotUtil.canJumpOverBlock(checkPos)) continue;
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private static boolean canJumpOverBlock(BlockPos pos) {
        return BotUtil.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && BotUtil.mc.world.getBlockState(pos.up().up()).getBlock() == Blocks.AIR && BotUtil.mc.world.getBlockState(pos.up().up().up()).getBlock() == Blocks.AIR;
    }

    private static Direction getDirection() {
        int dirnumber = MathHelper.floor((double)((double)(RotationUtil.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3;
        if (dirnumber == 0) {
            return Direction.SOUTH;
        }
        if (dirnumber == 1) {
            return Direction.WEST;
        }
        if (dirnumber == 2) {
            return Direction.NORTH;
        }
        return Direction.EAST;
    }

    static enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;

    }
}

