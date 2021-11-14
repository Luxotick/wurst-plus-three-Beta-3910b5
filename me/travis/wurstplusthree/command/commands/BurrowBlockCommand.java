/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package me.travis.wurstplusthree.command.commands;

import java.io.IOException;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.command.Command;
import me.travis.wurstplusthree.hack.hacks.combat.Burrow;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.WhitelistUtil;
import net.minecraft.block.Block;

public class BurrowBlockCommand
extends Command {
    String bBlock = "";

    public BurrowBlockCommand() {
        super("BurrowBlock", "bb");
    }

    @Override
    public void execute(String[] message) {
        Burrow bClass = (Burrow)WurstplusThree.HACKS.getHackByName("Burrow");
        Block b = WhitelistUtil.findBlock(message[0]);
        if (b.equals(null)) {
            ClientMessage.sendMessage("Cannot set Block to " + message[0]);
            return;
        }
        bClass.setBlock(b);
        ClientMessage.sendMessage("Set Block to " + message[0]);
        this.bBlock = message[0];
        try {
            WurstplusThree.CONFIG_MANAGER.saveBurrowBlock();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public String getBBlock() {
        return this.bBlock;
    }

    public void setBBlock(String b) {
        this.bBlock = b;
    }
}

