/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.DamageUtil;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CrystalUtil
implements Globals {
    public static Boolean getArmourFucker(EntityPlayer player, float percent) {
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) {
                return true;
            }
            float armourPercent = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
            if (!(percent >= armourPercent) || stack.stackSize >= 2) continue;
            return true;
        }
        return false;
    }

    public static boolean rayTraceSolidCheck(Vec3d start, Vec3d end, boolean shouldIgnore) {
        if (!(Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z) || Double.isNaN(end.x) || Double.isNaN(end.y) || Double.isNaN(end.z))) {
            int currX = MathHelper.floor((double)start.x);
            int currY = MathHelper.floor((double)start.y);
            int currZ = MathHelper.floor((double)start.z);
            int endX = MathHelper.floor((double)end.x);
            int endY = MathHelper.floor((double)end.y);
            int endZ = MathHelper.floor((double)end.z);
            BlockPos blockPos = new BlockPos(currX, currY, currZ);
            IBlockState blockState = CrystalUtil.mc.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (blockState.getCollisionBoundingBox((IBlockAccess)CrystalUtil.mc.world, blockPos) != Block.NULL_AABB && block.canCollideCheck(blockState, false) && (CrystalUtil.getBlocks().contains((Object)block) || !shouldIgnore)) {
                return true;
            }
            double seDeltaX = end.x - start.x;
            double seDeltaY = end.y - start.y;
            double seDeltaZ = end.z - start.z;
            int steps = 200;
            while (steps-- >= 0) {
                EnumFacing facing;
                if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                    return false;
                }
                if (currX == endX && currY == endY && currZ == endZ) {
                    return false;
                }
                boolean unboundedX = true;
                boolean unboundedY = true;
                boolean unboundedZ = true;
                double stepX = 999.0;
                double stepY = 999.0;
                double stepZ = 999.0;
                double deltaX = 999.0;
                double deltaY = 999.0;
                double deltaZ = 999.0;
                if (endX > currX) {
                    stepX = (double)currX + 1.0;
                } else if (endX < currX) {
                    stepX = currX;
                } else {
                    unboundedX = false;
                }
                if (endY > currY) {
                    stepY = (double)currY + 1.0;
                } else if (endY < currY) {
                    stepY = currY;
                } else {
                    unboundedY = false;
                }
                if (endZ > currZ) {
                    stepZ = (double)currZ + 1.0;
                } else if (endZ < currZ) {
                    stepZ = currZ;
                } else {
                    unboundedZ = false;
                }
                if (unboundedX) {
                    deltaX = (stepX - start.x) / seDeltaX;
                }
                if (unboundedY) {
                    deltaY = (stepY - start.y) / seDeltaY;
                }
                if (unboundedZ) {
                    deltaZ = (stepZ - start.z) / seDeltaZ;
                }
                if (deltaX == 0.0) {
                    deltaX = -1.0E-4;
                }
                if (deltaY == 0.0) {
                    deltaY = -1.0E-4;
                }
                if (deltaZ == 0.0) {
                    deltaZ = -1.0E-4;
                }
                if (deltaX < deltaY && deltaX < deltaZ) {
                    facing = endX > currX ? EnumFacing.WEST : EnumFacing.EAST;
                    start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
                } else if (deltaY < deltaZ) {
                    facing = endY > currY ? EnumFacing.DOWN : EnumFacing.UP;
                    start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
                } else {
                    facing = endZ > currZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
                }
                if (!(block = (blockState = CrystalUtil.mc.world.getBlockState(blockPos = new BlockPos(currX = MathHelper.floor((double)start.x) - (facing == EnumFacing.EAST ? 1 : 0), currY = MathHelper.floor((double)start.y) - (facing == EnumFacing.UP ? 1 : 0), currZ = MathHelper.floor((double)start.z) - (facing == EnumFacing.SOUTH ? 1 : 0)))).getBlock()).canCollideCheck(blockState, false) || !CrystalUtil.getBlocks().contains((Object)block) && shouldIgnore) continue;
                return true;
            }
        }
        return false;
    }

    public static float getDamageFromDifficulty(float damage) {
        switch (CrystalUtil.mc.world.getDifficulty()) {
            case PEACEFUL: {
                return 0.0f;
            }
            case EASY: {
                return Math.min(damage / 2.0f + 1.0f, damage);
            }
            case HARD: {
                return damage * 3.0f / 2.0f;
            }
        }
        return damage;
    }

    public static float calculateDamage(BlockPos pos, Entity target, boolean shouldIgnore) {
        return CrystalUtil.getExplosionDamage(target, new Vec3d((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5), 6.0f, shouldIgnore);
    }

    public static float calculateDamage(Entity crystal, Entity target, boolean shouldIgnore) {
        return CrystalUtil.getExplosionDamage(target, new Vec3d(crystal.posX, crystal.posY, crystal.posZ), 6.0f, shouldIgnore);
    }

    public static float getExplosionDamage(Entity targetEntity, Vec3d explosionPosition, float explosionPower, boolean shouldIgnore) {
        Vec3d entityPosition = new Vec3d(targetEntity.posX, targetEntity.posY, targetEntity.posZ);
        if (targetEntity.isImmuneToExplosions()) {
            return 0.0f;
        }
        double distanceToSize = entityPosition.distanceTo(explosionPosition) / (double)(explosionPower *= 2.0f);
        double blockDensity = 0.0;
        AxisAlignedBB bbox = targetEntity.getEntityBoundingBox().offset(targetEntity.getPositionVector().subtract(entityPosition));
        Vec3d bboxDelta = new Vec3d(1.0 / ((bbox.maxX - bbox.minX) * 2.0 + 1.0), 1.0 / ((bbox.maxY - bbox.minY) * 2.0 + 1.0), 1.0 / ((bbox.maxZ - bbox.minZ) * 2.0 + 1.0));
        double xOff = (1.0 - Math.floor(1.0 / bboxDelta.x) * bboxDelta.x) / 2.0;
        double zOff = (1.0 - Math.floor(1.0 / bboxDelta.z) * bboxDelta.z) / 2.0;
        if (bboxDelta.x >= 0.0 && bboxDelta.y >= 0.0 && bboxDelta.z >= 0.0) {
            int nonSolid = 0;
            int total = 0;
            for (double x = 0.0; x <= 1.0; x += bboxDelta.x) {
                for (double y = 0.0; y <= 1.0; y += bboxDelta.y) {
                    for (double z = 0.0; z <= 1.0; z += bboxDelta.z) {
                        Vec3d startPos = new Vec3d(xOff + bbox.minX + (bbox.maxX - bbox.minX) * x, bbox.minY + (bbox.maxY - bbox.minY) * y, zOff + bbox.minZ + (bbox.maxZ - bbox.minZ) * z);
                        if (!CrystalUtil.rayTraceSolidCheck(startPos, explosionPosition, shouldIgnore)) {
                            ++nonSolid;
                        }
                        ++total;
                    }
                }
            }
            blockDensity = (double)nonSolid / (double)total;
        }
        double densityAdjust = (1.0 - distanceToSize) * blockDensity;
        float damage = (int)((densityAdjust * densityAdjust + densityAdjust) / 2.0 * 7.0 * (double)explosionPower + 1.0);
        if (targetEntity instanceof EntityLivingBase) {
            damage = CrystalUtil.getBlastReduction((EntityLivingBase)targetEntity, CrystalUtil.getDamageFromDifficulty(damage), new Explosion((World)CrystalUtil.mc.world, null, explosionPosition.x, explosionPosition.y, explosionPosition.z, explosionPower / 2.0f, false, true));
        }
        return damage;
    }

    public static List<Block> getBlocks() {
        return Arrays.asList(new Block[]{Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.COMMAND_BLOCK, Blocks.BARRIER, Blocks.ENCHANTING_TABLE, Blocks.ENDER_CHEST, Blocks.END_PORTAL_FRAME, Blocks.BEACON, Blocks.ANVIL});
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        float enchantmentModifierDamage = 0.0f;
        try {
            enchantmentModifierDamage = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)entity.getArmorInventoryList(), (DamageSource)DamageSource.causeExplosionDamage((Explosion)explosion));
        }
        catch (Exception exception) {
            // empty catch block
        }
        enchantmentModifierDamage = MathHelper.clamp((float)enchantmentModifierDamage, (float)0.0f, (float)20.0f);
        damage *= 1.0f - enchantmentModifierDamage / 25.0f;
        PotionEffect resistanceEffect = entity.getActivePotionEffect(MobEffects.RESISTANCE);
        if (entity.isPotionActive(MobEffects.RESISTANCE) && resistanceEffect != null) {
            damage = damage * (25.0f - (float)(resistanceEffect.getAmplifier() + 1) * 5.0f) / 25.0f;
        }
        damage = Math.max(damage, 0.0f);
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = DamageUtil.mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)CrystalUtil.getSphere(PlayerUtil.getPlayerPos(), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> CrystalUtil.mc.world.getBlockState(pos).getBlock() != Blocks.AIR).filter(pos -> CrystalUtil.canPlaceCrystal(pos, specialEntityCheck, oneDot15)).collect(Collectors.toList()));
        return positions;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static boolean canSeePos(BlockPos pos) {
        return CrystalUtil.mc.world.rayTraceBlocks(new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ), new Vec3d((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5), false, true, false) == null;
    }

    public static boolean isClose(int input1, int input2, double deviation) {
        return (double)input2 + deviation < (double)input1 - deviation && (double)input1 - deviation < (double)input2;
    }

    public static boolean isClose(BlockPos pos1, BlockPos pos2, double d) {
        return CrystalUtil.isClose(pos1.getX(), pos2.getX(), d) && CrystalUtil.isClose(pos1.getY(), pos2.getY(), d) && CrystalUtil.isClose(pos1.getZ(), pos2.getZ(), d);
    }

    public static EntityEnderCrystal isCrystalStuck(BlockPos crystalPos) {
        for (Entity e : CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(crystalPos))) {
            if (CrystalUtil.isClose(crystalPos, e.getPosition(), 0.5)) {
                ClientMessage.sendMessage("shit too close");
                continue;
            }
            if (!(e instanceof EntityEnderCrystal)) continue;
            return (EntityEnderCrystal)e;
        }
        return null;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean onepointThirteen) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (!onepointThirteen) {
                if (CrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && CrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (CrystalUtil.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || CrystalUtil.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (Entity entity : CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
                for (Entity entity : CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (CrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && CrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (CrystalUtil.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Entity getPredictedPosition(Entity entity, double x) {
        if (x == 0.0) {
            return entity;
        }
        EntityPlayer e = null;
        double motionX = entity.posX - entity.lastTickPosX;
        double motionY = entity.posY - entity.lastTickPosY;
        double motionZ = entity.posZ - entity.lastTickPosZ;
        boolean shouldPredict = false;
        boolean shouldStrafe = false;
        double motion = Math.sqrt(Math.pow(motionX, 2.0) + Math.pow(motionZ, 2.0) + Math.pow(motionY, 2.0));
        if (motion > 0.1) {
            shouldPredict = true;
        }
        if (!shouldPredict) {
            return entity;
        }
        if (motion > 0.31) {
            shouldStrafe = true;
        }
        int i = 0;
        while ((double)i < x) {
            if (e == null) {
                if (CrystalUtil.isOnGround(0.0, 0.0, 0.0, entity)) {
                    motionY = shouldStrafe ? 0.4 : -0.07840015258789;
                } else {
                    motionY -= 0.08;
                    motionY *= (double)0.98f;
                }
                e = CrystalUtil.placeValue(motionX, motionY, motionZ, (EntityPlayer)entity);
            } else {
                if (CrystalUtil.isOnGround(0.0, 0.0, 0.0, e)) {
                    motionY = shouldStrafe ? 0.4 : -0.07840015258789;
                } else {
                    motionY -= 0.08;
                    motionY *= (double)0.98f;
                }
                e = CrystalUtil.placeValue(motionX, motionY, motionZ, e);
            }
            ++i;
        }
        return e;
    }

    public static boolean isOnGround(double x, double y, double z, Entity entity) {
        try {
            double d3 = y;
            List list1 = CrystalUtil.mc.world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(x, y, z));
            if (y != 0.0) {
                int l = list1.size();
                for (int k = 0; k < l; ++k) {
                    y = ((AxisAlignedBB)list1.get(k)).calculateYOffset(entity.getEntityBoundingBox(), y);
                }
            }
            return d3 != y && d3 < 0.0;
        }
        catch (Exception ignored) {
            return false;
        }
    }

    public static EntityPlayer placeValue(double x, double y, double z, EntityPlayer entity) {
        List list1 = CrystalUtil.mc.world.getCollisionBoxes((Entity)entity, entity.getEntityBoundingBox().expand(x, y, z));
        if (y != 0.0) {
            int l = list1.size();
            for (int k = 0; k < l; ++k) {
                y = ((AxisAlignedBB)list1.get(k)).calculateYOffset(entity.getEntityBoundingBox(), y);
            }
            if (y != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0, y, 0.0));
            }
        }
        if (x != 0.0) {
            int l5 = list1.size();
            for (int j5 = 0; j5 < l5; ++j5) {
                x = CrystalUtil.calculateXOffset(entity.getEntityBoundingBox(), x, (AxisAlignedBB)list1.get(j5));
            }
            if (x != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(x, 0.0, 0.0));
            }
        }
        if (z != 0.0) {
            int i6 = list1.size();
            for (int k5 = 0; k5 < i6; ++k5) {
                z = CrystalUtil.calculateZOffset(entity.getEntityBoundingBox(), z, (AxisAlignedBB)list1.get(k5));
            }
            if (z != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0, 0.0, z));
            }
        }
        return entity;
    }

    public static double calculateXOffset(AxisAlignedBB other, double offsetX, AxisAlignedBB this1) {
        if (other.maxY > this1.minY && other.minY < this1.maxY && other.maxZ > this1.minZ && other.minZ < this1.maxZ) {
            double d0;
            if (offsetX > 0.0 && other.maxX <= this1.minX) {
                double d1 = this1.minX - 0.3 - other.maxX;
                if (d1 < offsetX) {
                    offsetX = d1;
                }
            } else if (offsetX < 0.0 && other.minX >= this1.maxX && (d0 = this1.maxX + 0.3 - other.minX) > offsetX) {
                offsetX = d0;
            }
        }
        return offsetX;
    }

    public static double calculateZOffset(AxisAlignedBB other, double offsetZ, AxisAlignedBB this1) {
        if (other.maxX > this1.minX && other.minX < this1.maxX && other.maxY > this1.minY && other.minY < this1.maxY) {
            double d0;
            if (offsetZ > 0.0 && other.maxZ <= this1.minZ) {
                double d1 = this1.minZ - 0.3 - other.maxZ;
                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if (offsetZ < 0.0 && other.minZ >= this1.maxZ && (d0 = this1.maxZ + 0.3 - other.minZ) > offsetZ) {
                offsetZ = d0;
            }
        }
        return offsetZ;
    }
}

