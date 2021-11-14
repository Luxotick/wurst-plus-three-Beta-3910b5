/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.event.events;

import me.travis.wurstplusthree.event.EventStage;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BlockCollisionBoundingBoxEvent
extends EventStage {
    private final BlockPos pos;
    private AxisAlignedBB _boundingBox;

    public BlockCollisionBoundingBoxEvent(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public AxisAlignedBB getBoundingBox() {
        return this._boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this._boundingBox = boundingBox;
    }
}

