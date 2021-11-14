/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.travis.wurstplusthree.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.DeathEvent;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.hacks.chat.AutoEz;
import me.travis.wurstplusthree.hack.hacks.chat.TotemPopCounter;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.MathsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KDManager
implements Globals {
    public final ConcurrentHashMap<String, Integer> targets = new ConcurrentHashMap();
    private double totalDeaths;
    private double totalKills;
    private int currentKills;
    private String kdString = (Object)ChatFormatting.RED + "[" + (Object)ChatFormatting.GOLD + "KD" + (Object)ChatFormatting.RED + "] " + (Object)ChatFormatting.RESET;

    public int getCurrentKills() {
        return this.currentKills;
    }

    public String getKD() {
        if (this.totalDeaths == 0.0) {
            return "" + this.totalKills;
        }
        return "" + MathsUtil.round(this.totalKills / this.totalDeaths, 2);
    }

    public String getTotalKills() {
        return "" + this.totalKills;
    }

    public String getTotalDeaths() {
        return "" + this.totalDeaths;
    }

    public KDManager() {
        WurstplusThree.EVENT_PROCESSOR.addEventListener(this);
    }

    public void addDeath() {
        this.totalDeaths += 1.0;
        if (TotemPopCounter.INSTANCE.kdMessages.getValue().booleanValue()) {
            ClientMessage.sendMessage(this.kdString + "Your KD is " + this.getKD() + "!");
        }
        this.currentKills = 0;
    }

    public void onUpdate() {
        this.targets.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targets.remove(name);
            } else {
                this.targets.put((String)name, timeout - 1);
            }
        });
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !WurstplusThree.FRIEND_MANAGER.isFriend(event.getEntityPlayer().getName())) {
            this.targets.put(event.getTarget().getName(), 20);
        }
    }

    @CommitEvent
    public void onSendAttackPacket(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)KDManager.mc.world) instanceof EntityPlayer && !WurstplusThree.FRIEND_MANAGER.isFriend(Objects.requireNonNull(packet.getEntityFromWorld((World)KDManager.mc.world)).getName())) {
            this.targets.put(Objects.requireNonNull(packet.getEntityFromWorld((World)KDManager.mc.world)).getName(), 20);
        }
    }

    @CommitEvent
    public void onEntityDeath(DeathEvent event) {
        if (this.targets.containsKey(event.player.getName())) {
            this.totalKills += 1.0;
            ++this.currentKills;
            if (TotemPopCounter.INSTANCE.kdMessages.getValue().booleanValue()) {
                ClientMessage.sendMessage(this.kdString + "Your KD is " + this.getKD() + "!");
            }
            AutoEz.INSTANCE.announceDeath();
            this.targets.remove(event.player.getName());
        }
    }
}

