/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;
import me.travis.wurstplusthree.util.RenderUtil2D;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Rainbow;

@HudElement.Element(name="CSGO Watermark", posX=100, posY=100)
public class HudCsgoWatermark
extends HudElement {
    String text = "";

    @Override
    public int getHeight() {
        return HudUtil.getHudStringHeight();
    }

    @Override
    public int getWidth() {
        return HudUtil.getHudStringWidth(this.text);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int padding = 5;
        Colour fill = new Colour(77, 77, 77, 255);
        Colour outline = new Colour(127, 127, 127, 255);
        RenderUtil2D.drawBorderedRect(this.getX() - padding, this.getY() - padding, this.getX() + padding + this.getWidth(), this.getY() + padding + this.getHeight() - 1, 1, fill.hashCode(), outline.hashCode(), false);
        RenderUtil2D.drawHLineG(this.getX() - padding, this.getY() - padding, this.getX() + padding + this.getWidth() - (this.getX() - padding), Rainbow.getColour().hashCode(), Rainbow.getFurtherColour(HudEditor.INSTANCE.welcomerOffset.getValue()).hashCode());
        this.text = (Object)ChatFormatting.GOLD + "Wurst" + (Object)ChatFormatting.RESET + "plus";
        if (HudEditor.INSTANCE.welcomerName.getValue().booleanValue()) {
            this.text = this.text + " | " + HudCsgoWatermark.mc.player.getName();
        }
        if (HudEditor.INSTANCE.welcomerFps.getValue().booleanValue()) {
            this.text = this.text + " |" + HudUtil.getFpsLine() + (Object)ChatFormatting.RESET;
        }
        if (HudEditor.INSTANCE.welcomerTps.getValue().booleanValue()) {
            this.text = this.text + " |" + HudUtil.getTpsLine() + (Object)ChatFormatting.RESET;
        }
        if (HudEditor.INSTANCE.welcomerPing.getValue().booleanValue()) {
            this.text = this.text + " |" + HudUtil.getPingLine() + (Object)ChatFormatting.RESET;
        }
        if (HudEditor.INSTANCE.welcomerTime.getValue().booleanValue()) {
            this.text = this.text + " | " + HudUtil.getAnaTimeLine() + (Object)ChatFormatting.RESET;
        }
        if (HudEditor.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringRainbow(this.text, this.getX(), this.getY(), true);
        }
        HudUtil.drawHudString(this.text, this.getX(), this.getY(), HudEditor.INSTANCE.fontColor.getValue().hashCode());
    }
}

