/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import java.util.List;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;
import me.travis.wurstplusthree.util.RenderUtil2D;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Rainbow;
import net.minecraft.client.gui.ScaledResolution;

@HudElement.Element(name="Array List", posX=50, posY=50)
public class HudArrayList
extends HudElement {
    int width = 0;
    int height = 0;

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int padding = 5;
        Colour fill = new Colour(77, 77, 77, 255);
        Colour outline = new Colour(127, 127, 127, 255);
        if (HudEditor.INSTANCE.arrayOutline.getValue().booleanValue()) {
            RenderUtil2D.drawBorderedRect(this.getX() - padding, this.getY() - padding, this.getX() + padding + this.getWidth(), this.getY() + this.getHeight() - 1, 1, fill.hashCode(), outline.hashCode(), false);
            RenderUtil2D.drawHLineG(this.getX() - padding, this.getY() - padding, this.getX() + padding + this.getWidth() - (this.getX() - padding), Rainbow.getColour().hashCode(), Rainbow.getFurtherColour(HudEditor.INSTANCE.welcomerOffset.getValue()).hashCode());
        }
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        boolean isTop = false;
        boolean isLeft = false;
        if ((float)this.getY() < (float)scaledResolution.getScaledHeight() / 2.0f) {
            isTop = true;
        }
        if ((float)this.getX() < (float)scaledResolution.getScaledWidth() / 2.0f) {
            isLeft = true;
        }
        List<Hack> hacks = WurstplusThree.HACKS.getSortedHacks(isTop, HudEditor.INSTANCE.customFont.getValue());
        int bestWidth = 0;
        int y = 0;
        for (Hack hack : hacks) {
            if (WurstplusThree.HACKS.isDrawHack(hack)) continue;
            String name = hack.getFullArrayString();
            HudUtil.drawHudString(name, isLeft ? this.getX() : HudUtil.getRightX(name, this.getX() + this.width), this.getY() + y, HudEditor.INSTANCE.fontColor.getValue().hashCode());
            int w = HudUtil.getHudStringWidth(name);
            if (w > bestWidth) {
                bestWidth = w;
            }
            y += 11;
        }
        this.height = y;
        this.width = bestWidth;
    }
}

