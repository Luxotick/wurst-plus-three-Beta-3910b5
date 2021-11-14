/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.util.RenderUtil2D;
import net.minecraft.entity.EntityLivingBase;

@HudElement.Element(name="Player View", posY=100, posX=100)
public class HudPlayerView
extends HudElement {
    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public int getWidth() {
        return 40;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        RenderUtil2D.drawEntityOnScreen((float)this.getX() + (float)this.getWidth() / 2.0f, this.getY() + this.getHeight(), 40.0f, (EntityLivingBase)HudPlayerView.mc.player);
    }
}

