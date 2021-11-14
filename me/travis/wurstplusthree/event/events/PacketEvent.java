/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package me.travis.wurstplusthree.event.events;

import me.travis.wurstplusthree.event.EventStage;
import me.travis.wurstplusthree.util.Globals;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent
extends EventStage
implements Globals {
    private Packet<?> packet;
    public boolean hasSetPacket;

    public PacketEvent(int stage, Packet<?> packet) {
        super(stage);
        this.packet = packet;
        this.hasSetPacket = false;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }

    public void setPacket(Packet<?> packet) {
        if (!this.nullCheck()) {
            this.hasSetPacket = true;
            if (!this.isCancelled()) {
                this.setCancelled(true);
            }
            PacketEvent.mc.player.connection.sendPacket(packet);
        }
    }

    @Cancelable
    public static class Receive
    extends PacketEvent {
        public Receive(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }

    @Cancelable
    public static class Send
    extends PacketEvent {
        public Send(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }
}

