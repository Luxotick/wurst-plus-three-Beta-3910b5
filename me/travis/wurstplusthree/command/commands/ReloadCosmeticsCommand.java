/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.command.commands;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.command.Command;
import me.travis.wurstplusthree.util.ClientMessage;

public class ReloadCosmeticsCommand
extends Command {
    public ReloadCosmeticsCommand() {
        super("ReloadCosmetics", "ReloadCosmetic");
    }

    @Override
    public void execute(String[] message) {
        WurstplusThree.COSMETIC_MANAGER.reload();
        ClientMessage.sendMessage("Reloaded Cosmetics!");
    }
}

