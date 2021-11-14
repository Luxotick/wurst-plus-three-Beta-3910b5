/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.networking.chat.packets.client;

import java.io.IOException;
import java.net.Socket;
import me.travis.wurstplusthree.networking.chat.Packet;
import me.travis.wurstplusthree.networking.chat.Sockets;

public class GetClientFromNamePacket
extends Packet {
    @Override
    public String[] run(String ... arguments) throws IOException {
        Socket s = Sockets.createConnection();
        Sockets.sendData(s, "client:getclientname:" + arguments[0]);
        String[] data = Sockets.getData(s);
        s.close();
        return data;
    }
}

