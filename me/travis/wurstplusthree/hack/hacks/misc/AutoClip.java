/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.command.commands.ClipBindCommand;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.KeyUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Hack.Registration(name="Auto Clip", description="Clips ppl when you kill them", category=Hack.Category.MISC, isListening=false)
public class AutoClip
extends Hack {
    public static AutoClip INSTANCE;
    DoubleSetting delay = new DoubleSetting("Delay", (Double)2.0, (Double)0.0, (Double)20.0, this);
    BooleanSetting test = new BooleanSetting("TestBind", (Boolean)false, (Hack)this);
    private int delayCount;
    private boolean shouldClip;
    private String target;
    public final ConcurrentHashMap<String, Integer> targets = new ConcurrentHashMap();

    public AutoClip() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.delayCount = 0;
        this.shouldClip = false;
    }

    @Override
    public void onUpdate() {
        if (this.test.getValue().booleanValue()) {
            KeyUtil.clip(ClipBindCommand.getKeys());
            this.test.setValue(false);
        }
        if (this.shouldClip) {
            this.Clip();
            ++this.delayCount;
        }
        for (Entity entity : AutoClip.mc.world.getLoadedEntityList()) {
            EntityPlayer player;
            if (!(entity instanceof EntityPlayer) || !((player = (EntityPlayer)entity).getHealth() <= 0.0f) || !this.targets.containsKey(player.getName())) continue;
            this.shouldClip = true;
            this.target = player.getName();
            this.targets.remove(player.getName());
        }
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
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)AutoClip.mc.world) instanceof EntityPlayer && !WurstplusThree.FRIEND_MANAGER.isFriend(Objects.requireNonNull(packet.getEntityFromWorld((World)AutoClip.mc.world)).getName())) {
            this.targets.put(Objects.requireNonNull(packet.getEntityFromWorld((World)AutoClip.mc.world)).getName(), 20);
        }
    }

    private void Clip() {
        if ((double)this.delayCount > this.delay.getValue() * 20.0) {
            this.delayCount = 0;
            ClientMessage.sendMessage("Clipping " + this.target);
            this.target = "";
            KeyUtil.clip(ClipBindCommand.getKeys());
            this.shouldClip = false;
        }
    }
}

