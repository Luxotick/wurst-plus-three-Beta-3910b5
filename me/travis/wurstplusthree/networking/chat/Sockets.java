/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.networking.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Sockets {
    static final String IP = "0.0.0.0";
    static final int PORT = 4200;

    public static Socket createConnection() throws IOException {
        return new Socket(IP, 4200);
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
}

