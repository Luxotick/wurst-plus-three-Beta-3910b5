/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class RPC {
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = (var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2);
        String discordID = "838078740344471623";
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        RPC.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        RPC.discordRichPresence.details = "0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611";
        RPC.discordRichPresence.largeImageKey = "logo";
        RPC.discordRichPresence.largeImageText = "with the_fellas";
        RPC.discordRichPresence.smallImageKey = "small";
        RPC.discordRichPresence.smallImageText = "discord.gg/wurst";
        RPC.discordRichPresence.state = null;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}

