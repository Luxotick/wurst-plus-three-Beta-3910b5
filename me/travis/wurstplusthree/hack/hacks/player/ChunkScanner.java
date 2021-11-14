/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.Chunk
 */
package me.travis.wurstplusthree.hack.hacks.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.IOException;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.mixin.mixins.accessors.IChunkProviderClient;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

@Hack.Registration(name="Chunk Scanner", description="Finds coords thx to azrn", category=Hack.Category.PLAYER)
public class ChunkScanner
extends Hack {
    IntSetting delay = new IntSetting("Delay", 200, 0, 1000, this);
    IntSetting loop = new IntSetting("Loop Per Tick", 1, 1, 100, this);
    IntSetting startX = new IntSetting("Start X", 0, 0, 1000000, this);
    IntSetting startZ = new IntSetting("Start Z", 0, 0, 1000000, this);
    BooleanSetting saveCoords = new BooleanSetting("Save Coords", (Boolean)true, (Hack)this);
    private BlockPos playerPos = null;
    private int renderDistanceDiameter = 0;
    private long time = 0L;
    private int count = 0;
    private int x;
    private int z;

    @Override
    public void onUpdate() {
        this.playerPos = new BlockPos(ChunkScanner.mc.player.posX, ChunkScanner.mc.player.posY - 1.0, ChunkScanner.mc.player.posZ);
        if (this.renderDistanceDiameter == 0) {
            IChunkProviderClient chunkProviderClient = (IChunkProviderClient)ChunkScanner.mc.world.getChunkProvider();
            this.renderDistanceDiameter = (int)Math.sqrt(chunkProviderClient.getLoadedChunks().size());
        }
        if (this.time == 0L) {
            this.time = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.time > (long)this.delay.getValue().intValue()) {
            for (int i = 0; i < this.loop.getValue(); ++i) {
                int x = this.getSpiralCoords(this.count)[0] * this.renderDistanceDiameter * 16 + this.startX.getValue();
                int z = this.getSpiralCoords(this.count)[1] * this.renderDistanceDiameter * 16 + this.startZ.getValue();
                BlockPos position = new BlockPos(x, 0, z);
                this.x = x;
                this.z = z;
                ChunkScanner.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.playerPos, EnumFacing.EAST));
                ChunkScanner.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, position, EnumFacing.EAST));
                this.playerPos = new BlockPos(ChunkScanner.mc.player.posX, ChunkScanner.mc.player.posY - 1.0, ChunkScanner.mc.player.posZ);
                this.time = System.currentTimeMillis();
                ++this.count;
            }
        }
    }

    @CommitEvent
    public final void CPacketEvent(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockChange) {
            int x = ((SPacketBlockChange)event.getPacket()).getBlockPosition().getX();
            int z = ((SPacketBlockChange)event.getPacket()).getBlockPosition().getZ();
            IChunkProviderClient chunkProviderClient = (IChunkProviderClient)ChunkScanner.mc.world.getChunkProvider();
            for (Chunk chunk : chunkProviderClient.getLoadedChunks().values()) {
                if (chunk.x != x / 16 && chunk.z != z / 16) continue;
                return;
            }
            String text = (Object)ChatFormatting.DARK_RED + "[" + (Object)ChatFormatting.RED + "CE" + (Object)ChatFormatting.DARK_RED + "] " + (Object)ChatFormatting.RESET;
            ClientMessage.sendMessage(text + "Player found at X: " + (Object)ChatFormatting.GREEN + x + (Object)ChatFormatting.RESET + " Z: " + (Object)ChatFormatting.GREEN + z);
            try {
                if (this.saveCoords.getValue().booleanValue()) {
                    WurstplusThree.CONFIG_MANAGER.saveCoords("X: " + x + " Z: " + z);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int[] getSpiralCoords(int n) {
        int x = 0;
        int z = 0;
        int d = 1;
        int lineNumber = 1;
        int[] coords = new int[]{0, 0};
        for (int i = 0; i < n; ++i) {
            if (2 * x * d < lineNumber) {
                coords = new int[]{x += d, z};
                continue;
            }
            if (2 * z * d < lineNumber) {
                coords = new int[]{x, z += d};
                continue;
            }
            d *= -1;
            ++lineNumber;
            ++n;
        }
        return coords;
    }

    @Override
    public void onEnable() {
        this.playerPos = null;
        this.count = 0;
        this.time = 0L;
    }

    @Override
    public void onDisable() {
        this.playerPos = null;
        this.count = 0;
        this.time = 0L;
    }

    @Override
    public String getDisplayInfo() {
        return this.x + " , " + this.z;
    }
}

