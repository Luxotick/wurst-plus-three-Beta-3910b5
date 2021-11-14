/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.HudUtil;
import net.minecraft.entity.player.EntityPlayer;

@HudElement.Element(name="Friends", posX=100, posY=100)
public class HudFriends
extends HudElement {
    int biggest = 0;
    int yVal = 0;

    @Override
    public int getHeight() {
        return this.yVal;
    }

    @Override
    public int getWidth() {
        return this.biggest;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.yVal = WurstplusThree.GUI_FONT_MANAGER.getTextHeight();
        ArrayList<String> friends = new ArrayList<String>();
        for (EntityPlayer player : HudFriends.mc.world.playerEntities) {
            if (!WurstplusThree.FRIEND_MANAGER.isFriend(player.getName())) continue;
            friends.add(player.getName());
        }
        int y = this.getY();
        if (friends.isEmpty()) {
            HudUtil.drawHudString((Object)ChatFormatting.BOLD + "U got no friends", this.getX(), y, HudEditor.INSTANCE.fontColor.getValue().hashCode());
            this.biggest = WurstplusThree.GUI_FONT_MANAGER.getTextWidth("U got no friends");
        } else {
            HudUtil.drawHudString((Object)ChatFormatting.BOLD + "the_fellas", this.getX(), y, HudEditor.INSTANCE.fontColor.getValue().hashCode());
            y += 12;
            int temp = 0;
            for (String friend : friends) {
                HudUtil.drawHudString(friend, this.getX(), y, HudEditor.INSTANCE.fontColor.getValue().hashCode());
                if (temp < WurstplusThree.GUI_FONT_MANAGER.getTextWidth(friend) && (temp = WurstplusThree.GUI_FONT_MANAGER.getTextWidth(friend)) < WurstplusThree.GUI_FONT_MANAGER.getTextWidth("the_fellas")) {
                    temp = WurstplusThree.GUI_FONT_MANAGER.getTextWidth("the_fellas");
                }
                y += 12;
                this.yVal += 12;
            }
            this.biggest = temp;
        }
    }
}

