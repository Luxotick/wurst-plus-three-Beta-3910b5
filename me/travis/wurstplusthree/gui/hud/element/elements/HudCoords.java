/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;

@HudElement.Element(name="Coords", posX=50, posY=50)
public class HudCoords
extends HudElement {
    public String text;

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
        String x = "[" + (int)HudCoords.mc.player.posX + "]";
        String y = "[" + (int)HudCoords.mc.player.posY + "]";
        String z = "[" + (int)HudCoords.mc.player.posZ + "]";
        String x_nether = "[" + Math.round(HudCoords.mc.player.dimension != -1 ? HudCoords.mc.player.posX / 8.0 : HudCoords.mc.player.posX * 8.0) + "]";
        String z_nether = "[" + Math.round(HudCoords.mc.player.dimension != -1 ? HudCoords.mc.player.posZ / 8.0 : HudCoords.mc.player.posZ * 8.0) + "]";
        this.text = "XYZ " + x + y + z + " XZ " + x_nether + z_nether;
        HudUtil.drawHudString(this.text, this.getX(), this.getY(), HudEditor.INSTANCE.fontColor.getValue().hashCode());
    }
}

