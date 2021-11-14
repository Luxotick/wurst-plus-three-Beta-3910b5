/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;

@HudElement.Element(name="Clock", posX=100, posY=100)
public class HudClock
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
        this.text = HudUtil.getAnaTimeLine() + " | " + HudUtil.getDate();
        HudUtil.drawHudString(this.text, this.getX(), this.getY(), HudEditor.INSTANCE.fontColor.getValue().hashCode());
    }
}

