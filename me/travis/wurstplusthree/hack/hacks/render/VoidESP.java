/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.travis.wurstplusthree.hack.hacks.render;

import io.netty.util.internal.ConcurrentSet;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Hack.Registration(name="Void Esp", description="see where the void is", category=Hack.Category.RENDER, isListening=false)
public class VoidESP
extends Hack {
    IntSetting range = new IntSetting("Range", 10, 0, 50, this);
    IntSetting yLevel = new IntSetting("SlefY", 20, 0, 255, this);
    EnumSetting mode = new EnumSetting("Render", "Pretty", Arrays.asList("Pretty", "Solid", "Outline"), this);
    ColourSetting colour = new ColourSetting("Colour", new Colour(200, 255, 200, 100), (Hack)this);
    private final ConcurrentSet<BlockPos> voidHoles = new ConcurrentSet();

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.voidHoles.clear();
        if (VoidESP.mc.player.dimension == 1) {
            return;
        }
        if (VoidESP.mc.player.getPosition().getY() > this.yLevel.getValue()) {
            return;
        }
        List<BlockPos> blockPosList = BlockUtil.getCircle(PlayerUtil.getPlayerPos(), 0, this.range.getValue().intValue(), false);
        for (BlockPos blockPos : blockPosList) {
            if (VoidESP.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.BEDROCK) || this.isAnyBedrock(blockPos, Offsets.center)) continue;
            this.voidHoles.add((Object)blockPos);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        boolean outline = false;
        boolean solid = false;
        if (this.mode.is("Pretty")) {
            outline = true;
            solid = true;
        }
        if (this.mode.is("Solid")) {
            outline = false;
            solid = true;
        }
        if (this.mode.is("Outline")) {
            outline = true;
            solid = false;
        }
        for (BlockPos hole : this.voidHoles) {
            RenderUtil.drawBoxESP(hole, (Color)this.colour.getValue(), (Color)this.colour.getValue(), 2.0f, outline, solid, true);
        }
    }

    private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
        for (BlockPos pos : offset) {
            if (!VoidESP.mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals((Object)Blocks.BEDROCK)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        this.voidHoles.clear();
    }

    private static class Offsets {
        static final BlockPos[] center = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};

        private Offsets() {
        }
    }
}

