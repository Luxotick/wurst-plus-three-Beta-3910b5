/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.travis.wurstplusthree.networking.chat.handler;

import java.io.IOException;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.networking.chat.Packet;
import me.travis.wurstplusthree.networking.chat.packets.client.NewClientPacket;
import me.travis.wurstplusthree.networking.chat.packets.ping.PingUpPacket;
import net.minecraft.client.Minecraft;

public class ClientHandling {
    public String token = "";
    private final Minecraft mc = Minecraft.getMinecraft();

    public void newClient() {
        try {
            NewClientPacket packetClient = new NewClientPacket();
            String[] data = ((Packet)packetClient).run();
            if (data[0].equals("server") && data[1].equals("newclient")) {
                if (data[2].equals("false")) {
                    this.token = WurstplusThree.CONFIG_MANAGER.loadIRCtoken();
                } else if (data[2].equals("true")) {
                    this.token = data[3];
                    WurstplusThree.CONFIG_MANAGER.saveIRCtoken(this.token);
                }
            }
            if (!this.token.isEmpty()) {
                PingUpPacket packetUp = new PingUpPacket();
                data = ((Packet)packetUp).run(this.token);
                if (data[0].equals("server") && data[1].equals("pingup")) {
                    WurstplusThree.LOGGER.info("IRC chat init complete");
                } else {
                    WurstplusThree.LOGGER.error("IRC chat not didnt init");
                }
            }
        }
        catch (IOException e) {
            WurstplusThree.LOGGER.error("Exception in loading new client for IRC " + e);
        }
    }
}

