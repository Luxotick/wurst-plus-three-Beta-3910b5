/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import me.travis.wurstplusthree.hack.Hack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

@Hack.Registration(name="Entity Mine", description="mines through entities", category=Hack.Category.MISC)
public class EntityMine
extends Hack {
    private boolean focus = false;

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        EntityMine.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> EntityMine.mc.player == entity).map(entity -> (EntityLivingBase)entity).filter(entity -> !entity.isDead).forEach(this::process);
        RayTraceResult normalResult = EntityMine.mc.objectMouseOver;
        if (normalResult != null) {
            this.focus = normalResult.typeOfHit == RayTraceResult.Type.ENTITY;
        }
    }

    private void process(EntityLivingBase event) {
        RayTraceResult bypassEntityResult = event.rayTrace(6.0, mc.getRenderPartialTicks());
        if (bypassEntityResult != null && this.focus && bypassEntityResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = bypassEntityResult.getBlockPos();
            if (EntityMine.mc.gameSettings.keyBindAttack.isKeyDown()) {
                EntityMine.mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
            }
        }
    }
}

