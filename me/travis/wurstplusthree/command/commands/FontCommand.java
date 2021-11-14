/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.travis.wurstplusthree.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.command.Command;
import me.travis.wurstplusthree.util.ClientMessage;

public class FontCommand
extends Command {
    public FontCommand() {
        super("Font");
    }

    @Override
    public void execute(String[] message) {
        if (message[0].equalsIgnoreCase("reset")) {
            WurstplusThree.GUI_FONT_MANAGER.reset();
            ClientMessage.sendMessage("Reset font");
            return;
        }
        if (message.length < 2) {
            ClientMessage.sendErrorMessage((Object)ChatFormatting.BOLD + "Set" + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + " to set font or" + (Object)ChatFormatting.BOLD + " Size" + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + " to set font size");
            return;
        }
        String mode = message[0];
        String name = message[1].replaceAll("_", " ");
        if (mode.equalsIgnoreCase("set")) {
            if (name.equalsIgnoreCase("random")) {
                ClientMessage.sendMessage("Set font to " + WurstplusThree.GUI_FONT_MANAGER.setRandomFont());
                return;
            }
            if (WurstplusThree.GUI_FONT_MANAGER.setFont(name)) {
                ClientMessage.sendMessage("Set font to " + (Object)ChatFormatting.BOLD + name);
            } else {
                ClientMessage.sendErrorMessage("Cannot find font " + (Object)ChatFormatting.BOLD + name);
            }
            return;
        }
        if (mode.equalsIgnoreCase("size")) {
            int size;
            try {
                size = Integer.parseInt(name);
            }
            catch (Exception ignored) {
                ClientMessage.sendErrorMessage("Size not valid");
                return;
            }
            WurstplusThree.GUI_FONT_MANAGER.setFontSize(size);
            ClientMessage.sendMessage("Font size set to " + size);
            return;
        }
        ClientMessage.sendErrorMessage((Object)ChatFormatting.BOLD + "Set" + (Object)ChatFormatting.RESET + " to set font or" + (Object)ChatFormatting.BOLD + " Size" + (Object)ChatFormatting.RESET + " to set font size");
    }
}

