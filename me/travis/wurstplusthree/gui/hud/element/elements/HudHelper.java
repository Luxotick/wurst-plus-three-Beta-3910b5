/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;
import me.travis.wurstplusthree.util.RenderUtil2D;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Rainbow;

@HudElement.Element(name="Helper", posX=100, posY=100)
public class HudHelper
extends HudElement {
    List<String> displayStrings = new ArrayList<String>();
    int width = 0;
    int currentlyDrawn = 0;

    @Override
    public int getHeight() {
        return this.currentlyDrawn;
    }

    @Override
    public int getWidth() {
        return HudUtil.getHudStringWidth("AA");
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.displayStrings.clear();
        int padding = 5;
        Colour fill = new Colour(77, 77, 77, 200);
        Colour outline = new Colour(127, 127, 127, 255);
        RenderUtil2D.drawBorderedRect(this.getX() - padding, this.getY() - padding, this.getX() + padding + this.getWidth(), this.getY() + padding + this.getHeight() - 1, 1, fill.hashCode(), outline.hashCode(), false);
        RenderUtil2D.drawHLineG(this.getX() - padding, this.getY() - padding, this.getX() + padding + this.getWidth() - (this.getX() - padding), Rainbow.getColour().hashCode(), Rainbow.getFurtherColour(HudEditor.INSTANCE.welcomerOffset.getValue()).hashCode());
        String ca = (WurstplusThree.HACKS.ishackEnabled("Crystal Aura") ? (Object)ChatFormatting.GREEN + "CA" : (HudEditor.INSTANCE.showOff.getValue() != false ? (Object)ChatFormatting.RED + "CA" : "")) + (Object)ChatFormatting.RESET;
        this.displayStrings.add(ca);
        String hf = (WurstplusThree.HACKS.ishackEnabled("Hole Fill") ? (Object)ChatFormatting.GREEN + "HF" : (HudEditor.INSTANCE.showOff.getValue() != false ? (Object)ChatFormatting.RED + "HF" : "")) + (Object)ChatFormatting.RESET;
        this.displayStrings.add(hf);
        String tr = (WurstplusThree.HACKS.ishackEnabled("Trap") ? (Object)ChatFormatting.GREEN + "TR" : (HudEditor.INSTANCE.showOff.getValue() != false ? (Object)ChatFormatting.RED + "TR" : "")) + (Object)ChatFormatting.RESET;
        this.displayStrings.add(tr);
        String sr = (WurstplusThree.HACKS.ishackEnabled("Surround") ? (Object)ChatFormatting.GREEN + "SR" : (HudEditor.INSTANCE.showOff.getValue() != false ? (Object)ChatFormatting.RED + "SR" : "")) + (Object)ChatFormatting.RESET;
        this.displayStrings.add(sr);
        String ka = (WurstplusThree.HACKS.ishackEnabled("Kill Aura") ? (Object)ChatFormatting.GREEN + "KA" : (HudEditor.INSTANCE.showOff.getValue() != false ? (Object)ChatFormatting.RED + "KA" : "")) + (Object)ChatFormatting.RESET;
        this.displayStrings.add(ka);
        this.displayStrings.removeIf(string -> string.equalsIgnoreCase("" + (Object)ChatFormatting.RESET));
        int i = 0;
        int vOffset = HudUtil.getHudStringHeight() + 4;
        int biggestWidth = 0;
        for (String string2 : this.displayStrings) {
            HudUtil.drawHudString(string2, this.getX(), this.getY() + i * vOffset + 2, HudEditor.INSTANCE.fontColor.getValue().hashCode());
            int w = HudUtil.getHudStringWidth(string2);
            if (w > biggestWidth) {
                biggestWidth = w;
                this.width = w;
            }
            ++i;
        }
        this.currentlyDrawn = i * vOffset;
    }
}

