/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.travis.wurstplusthree.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.command.Command;
import me.travis.wurstplusthree.hack.hacks.player.PlayerSpoofer;
import me.travis.wurstplusthree.util.ClientMessage;

public class PlayerSpooferCommand
extends Command {
    public PlayerSpooferCommand() {
        super("PlayerSpoof", "PlayerSpoofer", "PS");
    }

    @Override
    public void execute(String[] message) {
        if (message.length == 0) {
            ClientMessage.sendErrorMessage("Enter a name dumbass!");
            return;
        }
        if (message.length == 1) {
            String name;
            if (message[0].isEmpty()) {
                ClientMessage.sendErrorMessage("Enter a name dumbass!");
                return;
            }
            PlayerSpoofer.INSTANCE.name = name = message[0];
            PlayerSpoofer.INSTANCE.disable();
            PlayerSpoofer.INSTANCE.enable();
            ClientMessage.sendMessage("Set skin to " + (Object)ChatFormatting.BOLD + name);
        }
    }
}

