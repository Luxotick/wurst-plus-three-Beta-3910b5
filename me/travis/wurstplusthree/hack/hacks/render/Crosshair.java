/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.RenderUtil2D;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Hack.Registration(name="Crosshair", description="Renders a Crosshair like csgo", category=Hack.Category.RENDER, priority=HackPriority.Lowest)
public class Crosshair
extends Hack {
    ParentSetting dot = new ParentSetting("Dot", this);
    BooleanSetting centerDot = new BooleanSetting("Center Dot", (Boolean)true, this.dot);
    BooleanSetting dotOutline = new BooleanSetting("Dot Outline", (Boolean)true, this.dot, s -> this.centerDot.getValue());
    IntSetting dotSize = new IntSetting("Dot Size", 2, 1, 5, this.dot, s -> this.centerDot.getValue());
    IntSetting dotOutlineSize = new IntSetting("Dot Outline Width", 1, 1, 5, this.dot, s -> this.centerDot.getValue() != false && this.dotOutline.getValue() != false);
    ColourSetting dotColor = new ColourSetting("Dot Color", new Colour(255, 255, 255), this.dot, s -> this.centerDot.getValue());
    ColourSetting dotOutlineColor = new ColourSetting("Dot Outline Color", new Colour(0, 0, 0), this.dot, s -> this.centerDot.getValue() != false && this.dotOutline.getValue() != false);
    ParentSetting linesInner = new ParentSetting("Inner Lines", this);
    BooleanSetting innerLines = new BooleanSetting("Inner Lines", (Boolean)false, this.linesInner);
    BooleanSetting moveError = new BooleanSetting("Move Error", (Boolean)true, this.linesInner, s -> this.innerLines.getValue());
    BooleanSetting innerLinesOutline = new BooleanSetting("Inner Lines Outline", (Boolean)true, this.linesInner, s -> this.innerLines.getValue());
    ColourSetting innerLineColor = new ColourSetting("Inner Line Color", new Colour(255, 255, 255), this.linesInner, s -> this.innerLines.getValue());
    ColourSetting innerOutlineColor = new ColourSetting("Inner Outline Color", new Colour(0, 0, 0), this.linesInner, s -> this.innerLines.getValue());
    IntSetting innerLineLength = new IntSetting("Inner Length", 5, 1, 8, this.linesInner, s -> this.innerLines.getValue());
    IntSetting innerLineOffset = new IntSetting("Inner Offset", 1, 0, 8, this.linesInner, s -> this.innerLines.getValue());
    IntSetting innerLineWidth = new IntSetting("Inner Width", 2, 1, 5, this.linesInner, s -> this.innerLines.getValue());
    IntSetting innerLinesOutlineWidth = new IntSetting("Inner Outline Width", 1, 1, 5, this.linesInner, s -> this.innerLines.getValue() != false && this.innerLinesOutline.getValue() != false);
    boolean shouldMove = false;

    @Override
    public void onTick() {
        this.shouldMove = this.moveError.getValue() != false && (Crosshair.mc.player.motionX > 0.1 || Crosshair.mc.player.motionY > 0.1 || Crosshair.mc.player.motionZ > 0.1 || Crosshair.mc.player.motionX < -0.1 || Crosshair.mc.player.motionY < -0.1 || Crosshair.mc.player.motionZ < -0.1);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (Crosshair.mc.gameSettings.thirdPersonView != 0) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();
        if (this.centerDot.getValue().booleanValue() && !this.dotOutline.getValue().booleanValue()) {
            RenderUtil2D.drawRectMC(w / 2 + this.dotSize.getValue(), h / 2 + this.dotSize.getValue(), w / 2 - this.dotSize.getValue(), h / 2 - this.dotSize.getValue(), this.dotColor.getValue().hashCode());
        } else if (this.dotOutline.getValue().booleanValue() && this.centerDot.getValue().booleanValue()) {
            RenderUtil2D.drawBorderedRect(w / 2 + this.dotSize.getValue(), h / 2 + this.dotSize.getValue(), w / 2 - this.dotSize.getValue(), h / 2 - this.dotSize.getValue(), this.dotOutlineSize.getValue(), this.dotColor.getValue().hashCode(), this.dotOutlineColor.getValue().hashCode(), false);
        }
        if (this.innerLinesOutline.getValue().booleanValue() && this.innerLines.getValue().booleanValue()) {
            RenderUtil2D.drawRectMC(w / 2 + this.innerLineWidth.getValue() + this.innerLinesOutlineWidth.getValue(), h / 2 + this.innerLineOffset.getValue() - this.innerLinesOutlineWidth.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), w / 2 - this.innerLineWidth.getValue() - this.innerLinesOutlineWidth.getValue(), h / 2 + this.innerLineLength.getValue() + this.innerLineOffset.getValue() + this.innerLinesOutlineWidth.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), this.innerOutlineColor.getValue().hashCode());
            RenderUtil2D.drawRectMC(w / 2 + this.innerLineWidth.getValue() + this.innerLinesOutlineWidth.getValue(), h / 2 - this.innerLineOffset.getValue() + this.innerLinesOutlineWidth.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), w / 2 - this.innerLineWidth.getValue() - this.innerLinesOutlineWidth.getValue(), h / 2 - this.innerLineLength.getValue() - this.innerLineOffset.getValue() - this.innerLinesOutlineWidth.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), this.innerOutlineColor.getValue().hashCode());
            RenderUtil2D.drawRectMC(w / 2 - this.innerLineOffset.getValue() + this.innerLinesOutlineWidth.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 + this.innerLineWidth.getValue() + this.innerLinesOutlineWidth.getValue(), w / 2 - this.innerLineLength.getValue() - this.innerLineOffset.getValue() - this.innerLinesOutlineWidth.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 - this.innerLineWidth.getValue() - this.innerLinesOutlineWidth.getValue(), this.innerOutlineColor.getValue().hashCode());
            RenderUtil2D.drawRectMC(w / 2 + this.innerLineOffset.getValue() - this.innerLinesOutlineWidth.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 - this.innerLineWidth.getValue() - this.innerLinesOutlineWidth.getValue(), w / 2 + this.innerLineLength.getValue() + this.innerLineOffset.getValue() + this.innerLinesOutlineWidth.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 + this.innerLineWidth.getValue() + this.innerLinesOutlineWidth.getValue(), this.innerOutlineColor.getValue().hashCode());
        }
        if (this.innerLines.getValue().booleanValue()) {
            RenderUtil2D.drawRectMC(w / 2 + this.innerLineWidth.getValue(), this.shouldMove ? h / 2 + this.innerLineOffset.getValue() + this.innerLineOffset.getValue() : h / 2 + this.innerLineOffset.getValue(), w / 2 - this.innerLineWidth.getValue(), h / 2 + this.innerLineLength.getValue() + this.innerLineOffset.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), this.innerLineColor.getValue().hashCode());
            RenderUtil2D.drawRectMC(w / 2 + this.innerLineWidth.getValue(), h / 2 - this.innerLineOffset.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), w / 2 - this.innerLineWidth.getValue(), h / 2 - this.innerLineLength.getValue() - this.innerLineOffset.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), this.innerLineColor.getValue().hashCode());
            RenderUtil2D.drawRectMC(w / 2 - this.innerLineOffset.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 + this.innerLineWidth.getValue(), w / 2 - this.innerLineLength.getValue() - this.innerLineOffset.getValue() - (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 - this.innerLineWidth.getValue(), this.innerLineColor.getValue().hashCode());
            RenderUtil2D.drawRectMC(w / 2 + this.innerLineOffset.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 - this.innerLineWidth.getValue(), w / 2 + this.innerLineLength.getValue() + this.innerLineOffset.getValue() + (this.shouldMove ? this.innerLineOffset.getValue() : 0), h / 2 + this.innerLineWidth.getValue(), this.innerLineColor.getValue().hashCode());
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (Crosshair.mc.gameSettings.thirdPersonView != 0) {
            return;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            event.setCanceled(true);
        }
    }
}

