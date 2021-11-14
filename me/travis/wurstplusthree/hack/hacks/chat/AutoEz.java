/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.travis.wurstplusthree.hack.hacks.chat;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

@Hack.Registration(name="Auto Ez", description="lets people know ur clouted", category=Hack.Category.CHAT, priority=HackPriority.Lowest)
public class AutoEz
extends Hack {
    public static AutoEz INSTANCE;
    BooleanSetting discord = new BooleanSetting("Discord", (Boolean)false, (Hack)this);
    private int delayCount;

    public AutoEz() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.delayCount = 0;
    }

    @Override
    public void onUpdate() {
        ++this.delayCount;
    }

    public void announceDeath() {
        if (this.delayCount < 150 || !this.isEnabled()) {
            return;
        }
        this.delayCount = 0;
        AutoEz.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("you just got nae nae'd by wurst+3" + (this.discord.getValue() != false ? " | discord.gg/wurst" : "")));
    }
}

