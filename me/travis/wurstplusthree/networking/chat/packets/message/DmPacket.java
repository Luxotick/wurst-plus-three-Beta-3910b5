/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.networking.chat.packets.message;

import java.io.IOException;
import java.net.Socket;
import me.travis.wurstplusthree.networking.chat.Packet;
import me.travis.wurstplusthree.networking.chat.Sockets;

public class DmPacket
extends Packet {
    @Override
    public String[] run(String key, String ... arguments) throws IOException {
        String client = this.mc.player.getName() + ":" + this.mc.player.getUniqueID();
        Socket s = Sockets.createConnection();
        Sockets.sendData(s, "client:dmuser:" + client + ":" + key + ":" + arguments[0] + ":" + arguments[1]);
        String[] data = Sockets.getData(s);
        s.close();
        return data;
    }
}

