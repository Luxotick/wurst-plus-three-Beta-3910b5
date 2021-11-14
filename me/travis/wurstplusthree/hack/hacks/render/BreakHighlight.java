/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import me.travis.wurstplusthree.event.events.BlockBreakingEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Hack.Registration(name="Break Highlight", description="highlights where people are breaking", category=Hack.Category.RENDER, priority=HackPriority.Lowest)
public class BreakHighlight
extends Hack {
    ColourSetting self = new ColourSetting("SelfColour", new Colour(255, 255, 255, 200), (Hack)this);
    ColourSetting other = new ColourSetting("OtherColour", new Colour(255, 0, 0), (Hack)this);
    EnumSetting renderMode = new EnumSetting("Mode", "Both", Arrays.asList("Outline", "Both", "Fill"), this);
    HashMap<Integer, Pair<Integer, BlockPos>> breakingBlockList = new HashMap();

    @Override
    public void onEnable() {
        this.breakingBlockList.clear();
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void damageBlockEvent(BlockBreakingEvent event) {
        if (BreakHighlight.mc.world.getBlockState(event.pos).getBlock() == Blocks.BEDROCK) {
            return;
        }
        if (this.breakingBlockList.isEmpty()) {
            this.breakingBlockList.putIfAbsent(event.breakingID, new Pair<Integer, BlockPos>(event.breakStage, event.pos));
        } else {
            HashMap<Integer, Pair<Integer, BlockPos>> shitToAdd = new HashMap<Integer, Pair<Integer, BlockPos>>();
            ArrayList<Integer> idsToRemove = new ArrayList<Integer>();
            for (int id : this.breakingBlockList.keySet()) {
                Pair<Integer, BlockPos> pair = this.breakingBlockList.get(id);
                if (event.breakingID != id) {
                    shitToAdd.put(event.breakingID, new Pair<Integer, BlockPos>(event.breakStage, event.pos));
                    continue;
                }
                if (event.breakStage > pair.getKey()) {
                    idsToRemove.add(event.breakingID);
                    shitToAdd.put(event.breakingID, new Pair<Integer, BlockPos>(event.breakStage, event.pos));
                }
                if (event.breakingID == id && event.pos != pair.getValue()) {
                    idsToRemove.add(id);
                }
                if (event.breakingID != id || event.breakStage >= pair.getKey()) continue;
                idsToRemove.add(id);
            }
            for (int id : idsToRemove) {
                this.breakingBlockList.remove(id);
            }
            this.breakingBlockList.putAll(shitToAdd);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (int i : this.breakingBlockList.keySet()) {
            if (this.breakingBlockList.get(i).getValue() == null) continue;
            BlockPos pos = this.breakingBlockList.get(i).getValue();
            int state = this.breakingBlockList.get(i).getKey();
            EntityPlayer player = (EntityPlayer)BreakHighlight.mc.world.getEntityByID(i);
            if (pos == null || state == -1 || BreakHighlight.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) continue;
            AxisAlignedBB bb = BreakHighlight.mc.world.getBlockState(pos).getSelectedBoundingBox((World)BreakHighlight.mc.world, pos);
            bb = this.calcBB(bb, state);
            ColourSetting colourSetting = player == BreakHighlight.mc.player ? this.self : this.other;
            switch (this.renderMode.getValue()) {
                case "Both": {
                    RenderUtil.drawBBBox(bb, colourSetting.getValue(), colourSetting.getValue().getAlpha());
                    RenderUtil.drawBlockOutlineBB(bb, new Color(colourSetting.getValue().getRed(), colourSetting.getValue().getGreen(), colourSetting.getValue().getBlue(), 255), 1.0f);
                    break;
                }
                case "Outline": {
                    RenderUtil.drawBlockOutlineBB(bb, colourSetting.getValue(), 1.0f);
                    break;
                }
                case "Fill": {
                    RenderUtil.drawBBBox(bb, colourSetting.getValue(), colourSetting.getValue().getAlpha());
                }
            }
        }
    }

    private AxisAlignedBB calcBB(AxisAlignedBB bb, int state) {
        AxisAlignedBB rbb = bb;
        switch (state) {
            case 0: {
                rbb = bb.shrink(0.6);
                break;
            }
            case 1: {
                rbb = bb.shrink(0.65);
                break;
            }
            case 2: {
                rbb = bb.shrink(0.7);
                break;
            }
            case 3: {
                rbb = bb.shrink(0.75);
                break;
            }
            case 4: {
                rbb = bb.shrink(0.8);
                break;
            }
            case 5: {
                rbb = bb.shrink(0.85);
                break;
            }
            case 6: {
                rbb = bb.shrink(0.9);
                break;
            }
            case 7: {
                rbb = bb.shrink(0.95);
                break;
            }
            case 8: {
                rbb = bb;
            }
        }
        return rbb;
    }

    @Override
    public void onLogout() {
        this.breakingBlockList.clear();
    }
}

