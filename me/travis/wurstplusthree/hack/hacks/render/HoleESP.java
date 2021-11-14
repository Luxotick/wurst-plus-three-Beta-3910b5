/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.hack.hacks.render;

import com.google.common.collect.Sets;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.ColorUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.HoleUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Pair;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Hack.Registration(name="Hole ESP", description="shows holes", category=Hack.Category.RENDER, priority=HackPriority.Low)
public class HoleESP
extends Hack {
    IntSetting range = new IntSetting("Range", 5, 1, 20, this);
    EnumSetting customHoles = new EnumSetting("Show", "Single", Arrays.asList("Single", "Double"), this);
    EnumSetting mode = new EnumSetting("Render", "Pretty", Arrays.asList("Pretty", "Solid", "Outline", "Gradient", "Fade"), this);
    DoubleSetting Height = new DoubleSetting("Height", (Double)1.0, (Double)-1.0, (Double)2.0, this, s -> !this.mode.is("Fade"));
    DoubleSetting lineWidth = new DoubleSetting("LineWidth", (Double)1.0, (Double)-1.0, (Double)2.0, this, s -> !this.mode.is("Fade"));
    BooleanSetting hideOwn = new BooleanSetting("HideOwn", (Boolean)false, (Hack)this);
    ColourSetting bedrockColor = new ColourSetting("BedrockColor", new Colour(0, 255, 0, 100), (Hack)this);
    ColourSetting bedrockColor2 = new ColourSetting("BedrockOutline", new Colour(0, 255, 0, 100), (Hack)this);
    ColourSetting obsidianColor = new ColourSetting("ObsidianColor", new Colour(255, 0, 0, 100), (Hack)this);
    ColourSetting obsidianColor2 = new ColourSetting("ObsidianOutline", new Colour(255, 0, 0, 100), (Hack)this);
    EnumSetting RMode = new EnumSetting("ColorMode", "Rainbow", Arrays.asList("Rainbow", "Sin", "Two Tone"), this);
    EnumSetting SinMode = new EnumSetting("SineMode", "Special", Arrays.asList("Special", "Hue", "Saturation", "Brightness"), this, s -> this.RMode.is("Sin"));
    ColourSetting obsidianTwoToneColor = new ColourSetting("ObsidianTwoTone", new Colour(0, 0, 100, 255), (Hack)this, s -> this.RMode.is("Two Tone"));
    ColourSetting bedrockTwoToneColor = new ColourSetting("BedrockTwoTone", new Colour(0, 0, 100, 255), (Hack)this, s -> this.RMode.is("Two Tone"));
    IntSetting RDelay = new IntSetting("RainbowDelay", 500, 0, 2500, this);
    IntSetting FillUp = new IntSetting("FillUp", 80, 0, 255, this, s -> this.mode.is("Fade"));
    IntSetting FillDown = new IntSetting("FillDown", 0, 0, 255, this, s -> this.mode.is("Fade"));
    IntSetting LineFillUp = new IntSetting("LineFillUp", 80, 0, 255, this, s -> this.mode.is("Fade"));
    IntSetting LineFillDown = new IntSetting("LineFillDown", 0, 0, 255, this, s -> this.mode.is("Fade"));
    BooleanSetting invertLine = new BooleanSetting("InvertLine", (Boolean)false, (Hack)this, s -> this.mode.is("Fade"));
    BooleanSetting invertFill = new BooleanSetting("InvertFill", (Boolean)false, (Hack)this, s -> this.mode.is("Fade"));
    private final ConcurrentHashMap<BlockPos, Pair<Colour, Boolean>> holes = new ConcurrentHashMap();

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.holes.isEmpty()) {
            return;
        }
        this.holes.forEach((arg_0, arg_1) -> this.renderHoles(arg_0, arg_1));
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.holes.clear();
        int range = this.range.getValue();
        HashSet possibleHoles = Sets.newHashSet();
        List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), range, range, false, true, 0);
        for (BlockPos pos : blockPosList) {
            if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) || HoleESP.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR)) continue;
            possibleHoles.add(pos);
        }
        for (BlockPos pos : possibleHoles) {
            Pair<Colour, Boolean> p;
            boolean safe;
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType == HoleUtil.HoleType.NONE) continue;
            HoleUtil.BlockSafety holeSafety = holeInfo.getSafety();
            AxisAlignedBB centreBlocks = holeInfo.getCentre();
            if (centreBlocks == null) continue;
            Colour colour = holeSafety == HoleUtil.BlockSafety.UNBREAKABLE ? this.bedrockColor.getValue() : this.obsidianColor.getValue();
            boolean bl = safe = holeSafety == HoleUtil.BlockSafety.UNBREAKABLE;
            if (this.customHoles.is("Custom") && (holeType == HoleUtil.HoleType.CUSTOM || holeType == HoleUtil.HoleType.DOUBLE)) {
                p = new Pair<Colour, Boolean>(colour, safe);
                this.holes.put(pos, p);
                continue;
            }
            if (this.customHoles.is("Double") && holeType == HoleUtil.HoleType.DOUBLE) {
                p = new Pair<Colour, Boolean>(colour, safe);
                this.holes.put(pos, p);
                continue;
            }
            if (holeType != HoleUtil.HoleType.SINGLE) continue;
            p = new Pair<Colour, Boolean>(colour, safe);
            this.holes.put(pos, p);
        }
    }

    private void renderHoles(BlockPos hole, Pair<Colour, Boolean> pair) {
        boolean safe = pair.getValue();
        if (this.hideOwn.getValue().booleanValue() && hole.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ))) {
            return;
        }
        if (!this.mode.is("Gradient") && !this.mode.is("Fade")) {
            boolean outline = false;
            boolean solid = false;
            switch (this.mode.getValue()) {
                case "Pretty": {
                    outline = true;
                    solid = true;
                    break;
                }
                case "Solid": {
                    outline = false;
                    solid = true;
                    break;
                }
                case "Outline": {
                    outline = true;
                    solid = false;
                }
            }
            RenderUtil.drawBoxESP(hole, (Color)(safe ? this.bedrockColor.getValue() : this.obsidianColor.getValue()), (Color)(safe ? this.bedrockColor2.getValue() : this.obsidianColor2.getValue()), this.lineWidth.getValue(), outline, solid, Float.valueOf((float)(this.Height.getValue() - 1.0)));
        } else if (this.mode.is("Gradient")) {
            RenderUtil.drawGlowBox(hole, this.Height.getValue() - 1.0, Float.valueOf(this.lineWidth.getValue().floatValue()), safe ? this.bedrockColor.getValue() : this.obsidianColor.getValue(), safe ? this.bedrockColor2.getValue() : this.obsidianColor2.getValue());
        } else {
            RenderUtil.drawOpenGradientBox(hole, this.invertFill.getValue() == false ? this.getGColor(safe, false, false) : this.getGColor(safe, true, false), this.invertFill.getValue() == false ? this.getGColor(safe, true, false) : this.getGColor(safe, false, false), 0.0);
            RenderUtil.drawGradientBlockOutline(hole, this.invertLine.getValue() != false ? this.getGColor(safe, false, true) : this.getGColor(safe, true, true), this.invertLine.getValue() != false ? this.getGColor(safe, true, true) : this.getGColor(safe, false, true), 2.0f, 0.0);
        }
    }

    private Color getGColor(boolean safe, boolean top, boolean line) {
        ColorUtil.type type2 = null;
        switch (this.SinMode.getValue()) {
            case "Special": {
                type2 = ColorUtil.type.SPECIAL;
                break;
            }
            case "Saturation": {
                type2 = ColorUtil.type.SATURATION;
                break;
            }
            case "Brightness": {
                type2 = ColorUtil.type.BRIGHTNESS;
            }
        }
        Color rVal = !safe ? (this.obsidianColor.getRainbow() ? (this.RMode.is("Rainbow") ? (top ? ColorUtil.releasedDynamicRainbow(0, line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : ColorUtil.releasedDynamicRainbow(this.RDelay.getValue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())) : (this.SinMode.is("Hue") ? (top ? ColorUtil.getSinState(this.obsidianColor.getColor(), this.obsidianColor2.getColor(), 1000.0, line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : ColorUtil.getSinState(this.obsidianColor.getColor(), this.obsidianColor2.getColor(), (double)this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())) : (top ? ColorUtil.getSinState(this.obsidianColor.getColor(), 1000.0, line ? this.LineFillUp.getValue() : this.FillUp.getValue(), type2) : ColorUtil.getSinState(this.obsidianColor.getColor(), this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue(), type2)))) : (!this.RMode.is("Two Tone") ? (top ? new Colour(this.obsidianColor.getColor().getRed(), this.obsidianColor.getColor().getGreen(), this.obsidianColor.getColor().getBlue(), line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : new Colour(this.obsidianColor.getColor().getRed(), this.obsidianColor.getColor().getGreen(), this.obsidianColor.getColor().getBlue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())) : (top ? new Color(this.obsidianTwoToneColor.getColor().getRed(), this.obsidianTwoToneColor.getColor().getGreen(), this.obsidianTwoToneColor.getColor().getBlue(), line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : new Color(this.obsidianColor.getColor().getRed(), this.obsidianColor.getColor().getGreen(), this.obsidianColor.getColor().getBlue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())))) : (this.bedrockColor.getRainbow() ? (this.RMode.is("Rainbow") ? (top ? ColorUtil.releasedDynamicRainbow(0, line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : ColorUtil.releasedDynamicRainbow(this.RDelay.getValue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())) : (this.SinMode.is("Hue") ? (top ? ColorUtil.getSinState(this.bedrockColor.getColor(), this.bedrockColor2.getColor(), 1000.0, line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : ColorUtil.getSinState(this.bedrockColor.getColor(), this.bedrockColor2.getColor(), (double)this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())) : (top ? ColorUtil.getSinState(this.bedrockColor.getColor(), 1000.0, line ? this.LineFillUp.getValue() : this.FillUp.getValue(), type2) : ColorUtil.getSinState(this.bedrockColor.getColor(), this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue(), type2)))) : (!this.RMode.is("Two Tone") ? (top ? new Colour(this.bedrockColor.getColor().getRed(), this.bedrockColor.getColor().getGreen(), this.bedrockColor.getColor().getBlue(), line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : new Colour(this.bedrockColor.getColor().getRed(), this.bedrockColor.getColor().getGreen(), this.bedrockColor.getColor().getBlue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue())) : (top ? new Color(this.bedrockTwoToneColor.getColor().getRed(), this.bedrockTwoToneColor.getColor().getGreen(), this.bedrockTwoToneColor.getColor().getBlue(), line ? this.LineFillUp.getValue() : this.FillUp.getValue()) : new Color(this.bedrockColor.getColor().getRed(), this.bedrockColor.getColor().getGreen(), this.bedrockColor.getColor().getBlue(), line ? this.LineFillDown.getValue() : this.FillDown.getValue()))));
        return rVal;
    }

    @Override
    public String getDisplayInfo() {
        return "" + this.holes.size();
    }
}

