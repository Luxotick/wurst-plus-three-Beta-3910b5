/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.command.commands;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.command.Command;
import me.travis.wurstplusthree.util.ClientMessage;

public class ReloadCapesCommand
extends Command {
    public ReloadCapesCommand() {
        super("ReloadCapes", "ReloadCape");
    }

    @Override
    public void execute(String[] message) {
        WurstplusThree.CAPE_MANAGER.reload();
        ClientMessage.sendMessage("Reloaded Capes!");
    }
}

