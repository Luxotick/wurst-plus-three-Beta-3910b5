/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.networking.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.networking.proxy.CPacketHandler;
import org.jetbrains.annotations.NotNull;

public final class WurstPlusProxy {
    public final String host;
    public final int port;
    public final Mode mode;

    public WurstPlusProxy(int port, @NotNull String host, Mode mode) {
        this.port = port;
        this.host = host;
        this.mode = mode;
        WurstplusThree.EVENT_PROCESSOR.addEventListener(this);
    }

    public final Socket bind() throws IOException {
        return new Socket(this.host, this.port);
    }

    public static String[] getData(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        return r.readLine().split(":");
    }

    public static void sendData(Socket socket, String data) throws IOException {
        OutputStream stream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(stream, true);
        writer.println(data);
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void cPacketEvent(PacketEvent.Send event) {
        if (this.mode.equals((Object)Mode.CLIENT)) {
            CPacketHandler cPacketHandler = new CPacketHandler(event);
        }
    }

    public static enum Mode {
        CLIENT,
        SERVER;

    }
}

