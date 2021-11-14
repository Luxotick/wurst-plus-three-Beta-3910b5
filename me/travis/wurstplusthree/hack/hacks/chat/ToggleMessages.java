/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.chat;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;

@Hack.Registration(name="Toggle Msgs", description="Says in chat when you toggle something", category=Hack.Category.CHAT, priority=HackPriority.Lowest)
public class ToggleMessages
extends Hack {
    public static ToggleMessages INSTANCE;
    public BooleanSetting compact = new BooleanSetting("Compact", (Boolean)true, (Hack)this);

    public ToggleMessages() {
        INSTANCE = this;
    }
}

