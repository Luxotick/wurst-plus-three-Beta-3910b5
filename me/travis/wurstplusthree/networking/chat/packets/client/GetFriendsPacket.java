/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.networking.chat.packets.client;

import java.io.IOException;
import java.net.Socket;
import me.travis.wurstplusthree.networking.chat.Packet;
import me.travis.wurstplusthree.networking.chat.Sockets;

public class GetFriendsPacket
extends Packet {
    @Override
    public String[] run(String key) throws IOException {
        String client = this.mc.player.getName() + ":" + this.mc.player.getUniqueID();
        Socket s = Sockets.createConnection();
        Sockets.sendData(s, "client:getclientuuid:" + client + ":" + key);
        String[] data = Sockets.getData(s);
        s.close();
        return data;
    }
}

