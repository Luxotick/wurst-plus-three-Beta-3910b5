/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.networking.chat.handler;

import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.networking.chat.Packet;
import me.travis.wurstplusthree.networking.chat.packets.ping.PingGetGlobal;
import me.travis.wurstplusthree.util.ClientMessage;

public class ChatHandling
extends Thread {
    public ChatMode mode = ChatMode.SERVER;
    String name = "";
    boolean running = false;
    long lastPing = System.currentTimeMillis();

    @Override
    public void run() {
        this.lastPing = System.currentTimeMillis();
        this.chatLoop();
    }

    public void chatLoop() {
        while (this.running) {
            try {
                if (this.mode.equals((Object)ChatMode.GLOBAL)) {
                    if (System.currentTimeMillis() - this.lastPing != 1000L) continue;
                    try {
                        PingGetGlobal getChatPacket = new PingGetGlobal();
                        String[] data = ((Packet)getChatPacket).run(WurstplusThree.CLIENT_HANDLING.token);
                        if (data[0].equals("server") && data[1].equals("pinggetglobal")) {
                            String[] messages = data[3].split(";");
                            int ID = Integer.getInteger(messages[messages.length - 1].split("\\|")[0]);
                            Arrays.stream(messages).spliterator().forEachRemaining(msg -> {
                                String[] msgList = msg.split("\\|");
                                int msgId = Integer.getInteger(msgList[0]);
                                if (msgId == ID) {
                                    ClientMessage.sendMessage("[" + msgList[1] + "] " + msgList[2]);
                                }
                            });
                        }
                        this.lastPing = System.currentTimeMillis();
                    }
                    catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                if (this.mode.equals((Object)ChatMode.DIRECT) && System.currentTimeMillis() - this.lastPing != 1000L) continue;
            }
            catch (Exception e) {
                WurstplusThree.LOGGER.error("Exception in ChatLoop for IRC " + e);
            }
        }
    }

    public void setToDirect(String name) {
        this.mode = ChatMode.DIRECT;
        this.name = name;
    }

    public void setToGlobal() {
        this.mode = ChatMode.GLOBAL;
    }

    public void setToServer() {
        this.mode = ChatMode.SERVER;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    static enum ChatMode {
        GLOBAL,
        DIRECT,
        SERVER;

    }
}

