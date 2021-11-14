/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.login.client.CPacketEncryptionResponse
 *  net.minecraft.network.login.client.CPacketLoginStart
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.networking.proxy;

import java.io.IOException;
import java.net.Socket;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.networking.proxy.WurstPlusProxy;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.NotNull;

public final class CPacketHandler {
    private final PacketEvent.Send event;
    public boolean didSend;

    public CPacketHandler(@NotNull PacketEvent.Send event) {
        this.event = event;
        this.didSend = false;
        this.sendCPacketToProxy();
    }

    private void sendCPacketToProxy() {
        try {
            Socket s = WurstplusThree.PROXY.bind();
            String toSend = this.createProxyPacket();
            if (toSend.equals("CLIENT")) {
                return;
            }
            this.event.setCancelled(true);
            WurstPlusProxy.sendData(s, toSend);
            String[] data = WurstPlusProxy.getData(s);
            if (data[0].equals("SERVER") && data[1].equals("DONE")) {
                this.didSend = true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createProxyPacket() {
        String packet = "CLIENT";
        if (this.event.getPacket() instanceof CPacketAnimation) {
            packet = packet + ":Animation:" + (Object)((CPacketAnimation)this.event.getPacket()).getHand();
        } else if (this.event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer p = (CPacketPlayer)this.event.getPacket();
            packet = packet + ":Player:" + p.moving + ":" + p.onGround + ":" + p.rotating + ":" + p.pitch + ":" + p.yaw + ":" + p.x + ":" + p.y + ":" + p.z;
        } else if (this.event.getPacket() instanceof CPacketChatMessage) {
            packet = packet + ":ChatMessage:" + ((CPacketChatMessage)this.event.getPacket()).message;
        } else if (this.event.getPacket() instanceof CPacketConfirmTeleport) {
            packet = packet + ":ConfirmTeleport:" + ((CPacketConfirmTeleport)this.event.getPacket()).getTeleportId();
        } else if (this.event.getPacket() instanceof CPacketClientStatus) {
            packet = packet + ":ClientStatus:" + (Object)((CPacketClientStatus)this.event.getPacket()).getStatus();
        } else if (this.event.getPacket() instanceof CPacketClickWindow) {
            CPacketClickWindow p = (CPacketClickWindow)this.event.getPacket();
            packet = packet + ":ClickWindow:" + p.getWindowId() + ":" + (Object)p.getClickType() + ":" + p.getClickedItem().getDisplayName() + ":" + p.getActionNumber() + ":" + p.getSlotId() + ":" + p.getUsedButton();
        } else if (this.event.getPacket() instanceof CPacketCloseWindow) {
            packet = packet + ":CloseWindow:" + ((CPacketCloseWindow)this.event.getPacket()).windowId;
        } else if (this.event.getPacket() instanceof CPacketConfirmTransaction) {
            packet = packet + ":ConfirmTransaction:" + ((CPacketConfirmTransaction)this.event.getPacket()).getWindowId() + ":" + ((CPacketConfirmTransaction)this.event.getPacket()).getUid();
        } else if (this.event.getPacket() instanceof CPacketLoginStart) {
            packet = packet + ":LoginStart:" + ((CPacketLoginStart)this.event.getPacket()).getProfile().getName() + ":" + ((CPacketLoginStart)this.event.getPacket()).getProfile().getId();
        } else if (this.event.getPacket() instanceof C00Handshake) {
            packet = packet + ":Handshake:" + ((C00Handshake)this.event.getPacket()).hasFMLMarker() + ":" + (Object)((C00Handshake)this.event.getPacket()).getRequestedState();
        } else if (this.event.getPacket() instanceof CPacketEncryptionResponse) {
            packet = packet + ":EncryptionResponse";
        } else if (this.event.getPacket() instanceof CPacketKeepAlive) {
            packet = packet + ":KeepAlive";
        }
        return packet;
    }
}

