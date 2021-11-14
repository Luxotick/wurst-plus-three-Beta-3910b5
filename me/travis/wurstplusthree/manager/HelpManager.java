/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import me.travis.wurstplusthree.hack.hacks.chat.CustomChat;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.Globals;

public class HelpManager
implements Globals {
    private int counter = 5950;
    private final String[] tips0 = new String[]{"You cant disable these messages in the custom chat settings", "You can edit the hud using the hud editor module", "You can name a config to a server so it gets enabled when you join that server example: '@wurst.plus'", "The quiver module shoots arrow with a positive effect at you", "By adding the baritone jar to your mods folder extra baritone modules will show up", "If you experience frequent crashes you can send .minecraft/logs/latest.txt to one of the devs", "Configs for wurst + 3 can be found in the official discord discord.gg.wurst", "Donators receive a animated cape", "Right clicking a bind setting will make it a hold bind"};

    public void onUpdate() {
        if (!CustomChat.INSTANCE.help.getValue().booleanValue()) {
            return;
        }
        if (this.counter > 6000) {
            this.sendHelp();
        }
        ++this.counter;
    }

    public void sendHelp() {
        this.counter = 0;
        ArrayList tips = new ArrayList();
        Collections.addAll(tips, this.tips0);
        Random random = new Random();
        ClientMessage.sendMessage((String)tips.get(random.nextInt(tips.size()) + 0));
    }
}

