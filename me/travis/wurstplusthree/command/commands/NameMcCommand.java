/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.command.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import me.travis.wurstplusthree.command.Command;

public class NameMcCommand
extends Command {
    public NameMcCommand() {
        super("Namemc", "nmc");
    }

    @Override
    public void execute(String[] message) {
        String name = message[0];
        try {
            Desktop.getDesktop().browse(URI.create("https://namemc.com/search?q=" + name));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

