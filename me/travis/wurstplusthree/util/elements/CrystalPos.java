/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.util.elements;

import net.minecraft.util.math.BlockPos;

public class CrystalPos {
    private final BlockPos pos;
    private final Double damage;

    public CrystalPos(BlockPos pos, Double damage) {
        this.pos = pos;
        this.damage = damage;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Double getDamage() {
        return this.damage;
    }
}

