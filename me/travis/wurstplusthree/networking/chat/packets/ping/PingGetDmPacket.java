/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.networking.chat.packets.ping;

import java.io.IOException;
import java.net.Socket;
import me.travis.wurstplusthree.networking.chat.Packet;
import me.travis.wurstplusthree.networking.chat.Sockets;

public class PingGetDmPacket
extends Packet {
    @Override
    public String[] run(String key) throws IOException {
        String client = this.mc.player.getName() + ":" + this.mc.player.getUniqueID();
        Socket s = Sockets.createConnection();
        Sockets.sendData(s, "client:pinggetdm:" + client + ":" + key);
        String[] data = Sockets.getData(s);
        s.close();
        return data;
    }
}

