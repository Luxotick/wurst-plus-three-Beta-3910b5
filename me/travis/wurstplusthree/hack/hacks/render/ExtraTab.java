/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 */
package me.travis.wurstplusthree.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.IntSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

@Hack.Registration(name="Extra Tab", description="this one doesnt crash ur game promise", category=Hack.Category.RENDER, isListening=false)
public class ExtraTab
extends Hack {
    public static ExtraTab INSTANCE;
    public IntSetting count = new IntSetting("Count", 250, 0, 1000, this);

    public ExtraTab() {
        INSTANCE = this;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String name;
        String string = name = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), (String)networkPlayerInfoIn.getGameProfile().getName());
        if (WurstplusThree.FRIEND_MANAGER.isFriend(name)) {
            return (Object)ChatFormatting.AQUA + name;
        }
        if (WurstplusThree.ENEMY_MANAGER.isEnemy(name)) {
            return (Object)ChatFormatting.RED + name;
        }
        if (name.equalsIgnoreCase("TrvsF")) {
            return (Object)ChatFormatting.GOLD + name;
        }
        return name;
    }
}

