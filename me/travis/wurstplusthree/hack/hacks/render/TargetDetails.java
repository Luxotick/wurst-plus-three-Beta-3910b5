/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.CrystalUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

@Hack.Registration(name="Target Details", description="shows status of dude", category=Hack.Category.RENDER, isListening=false)
public class TargetDetails
extends Hack {
    BooleanSetting showFucked = new BooleanSetting("Fucked", (Boolean)true, (Hack)this);
    BooleanSetting onePointT = new BooleanSetting("1.13+", (Boolean)false, (Hack)this);
    BooleanSetting showBurrow = new BooleanSetting("Burrowed", (Boolean)true, (Hack)this);
    ColourSetting fuckedColour = new ColourSetting("FuckedColour", new Colour(255, 20, 20, 150), (Hack)this, s -> this.showFucked.getValue());
    ColourSetting burrowedColour = new ColourSetting("BurrowedColour", new Colour(20, 255, 255, 150), (Hack)this, s -> this.showBurrow.getValue());
    EnumSetting mode = new EnumSetting("Render", "Pretty", Arrays.asList("Pretty", "Solid", "Outline"), this);
    private final ArrayList<BlockPos> fuckedBlocks = new ArrayList();
    private final ArrayList<BlockPos> burrowedBlocks = new ArrayList();

    @Override
    public void onEnable() {
        this.fuckedBlocks.clear();
        this.burrowedBlocks.clear();
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.fuckedBlocks.clear();
        this.burrowedBlocks.clear();
        this.getFuckedPlayers();
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.showBurrow.getValue().booleanValue()) {
            if (!this.burrowedBlocks.isEmpty()) {
                this.burrowedBlocks.forEach(this::renderBurrowedBlock);
            }
        } else if (this.showFucked.getValue().booleanValue() && !this.fuckedBlocks.isEmpty()) {
            this.fuckedBlocks.forEach(this::renderFuckedBlock);
        }
    }

    private void renderBurrowedBlock(BlockPos pos) {
        Colour color = this.burrowedColour.getValue();
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
        RenderUtil.drawBoxESP(pos, (Color)color, (Color)color, 2.0f, outline, solid, true);
    }

    private void renderFuckedBlock(BlockPos pos) {
        Colour color = this.fuckedColour.getValue();
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
        RenderUtil.drawBoxESP(pos, (Color)color, (Color)color, 2.0f, outline, solid, true);
    }

    private void getFuckedPlayers() {
        this.fuckedBlocks.clear();
        for (EntityPlayer player : TargetDetails.mc.world.playerEntities) {
            if (player == TargetDetails.mc.player || WurstplusThree.FRIEND_MANAGER.isFriend(player.getName()) || !EntityUtil.isLiving((Entity)player)) continue;
            if (this.isBurrowed(player)) {
                this.burrowedBlocks.add(new BlockPos(player.posX, player.posY, player.posZ));
                continue;
            }
            if (!this.isFucked(player)) continue;
            this.fuckedBlocks.add(new BlockPos(player.posX, player.posY, player.posZ));
        }
    }

    private boolean isFucked(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.posX, player.posY - 1.0, player.posZ);
        if (CrystalUtil.canPlaceCrystal(pos.south(), true, this.onePointT.getValue()) || CrystalUtil.canPlaceCrystal(pos.south().south(), true, this.onePointT.getValue()) && TargetDetails.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (CrystalUtil.canPlaceCrystal(pos.east(), true, this.onePointT.getValue()) || CrystalUtil.canPlaceCrystal(pos.east().east(), true, this.onePointT.getValue()) && TargetDetails.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (CrystalUtil.canPlaceCrystal(pos.west(), true, this.onePointT.getValue()) || CrystalUtil.canPlaceCrystal(pos.west().west(), true, this.onePointT.getValue()) && TargetDetails.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        return CrystalUtil.canPlaceCrystal(pos.north(), true, this.onePointT.getValue()) || CrystalUtil.canPlaceCrystal(pos.north().north(), true, this.onePointT.getValue()) && TargetDetails.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR;
    }

    private boolean isBurrowed(EntityPlayer player) {
        BlockPos pos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
        return TargetDetails.mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST || TargetDetails.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || TargetDetails.mc.world.getBlockState(pos).getBlock() == Blocks.CHEST;
    }
}

