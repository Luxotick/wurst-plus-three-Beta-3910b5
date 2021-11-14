/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;

@HudElement.Element(name="Watermark", posX=100, posY=100)
public class HudWatermark
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
        this.text = (Object)ChatFormatting.GOLD + "Wurst+3" + (Object)ChatFormatting.RESET + " v" + "0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611";
        HudUtil.drawHudString(this.text, this.getX(), this.getY(), HudEditor.INSTANCE.fontColor.getValue().hashCode());
    }
}

