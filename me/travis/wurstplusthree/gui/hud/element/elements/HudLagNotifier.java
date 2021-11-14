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

@HudElement.Element(name="Lag Notifier", posY=100, posX=100)
public class HudLagNotifier
extends HudElement {
    String length;

    @Override
    public int getWidth() {
        return HudUtil.getHudStringWidth(this.length);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.length = (Object)ChatFormatting.RED + "Server is not responding " + Math.round((float)WurstplusThree.SERVER_MANAGER.serverRespondingTime() / 1000.0f);
        if (WurstplusThree.SERVER_MANAGER.isServerNotResponding()) {
            HudUtil.drawHudString(this.length, this.getX(), this.getY(), HudEditor.INSTANCE.fontColor.getValue().hashCode());
        }
    }
}

