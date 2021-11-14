/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.network.play.server.SPacketUpdateBossInfo
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Hack.Registration(name="No Render", description="stops rendering things", category=Hack.Category.RENDER, isListening=false)
public class NoRender
extends Hack {
    public static NoRender INSTANCE;
    public BooleanSetting armour = new BooleanSetting("Armour", (Boolean)true, (Hack)this);
    public BooleanSetting fire = new BooleanSetting("Fire", (Boolean)true, (Hack)this);
    public BooleanSetting blind = new BooleanSetting("Blind", (Boolean)true, (Hack)this);
    public BooleanSetting nausea = new BooleanSetting("Nausea", (Boolean)true, (Hack)this);
    public BooleanSetting hurtcam = new BooleanSetting("Hurt Cam", (Boolean)true, (Hack)this);
    public BooleanSetting skylight = new BooleanSetting("Sky Light", (Boolean)false, (Hack)this);
    public BooleanSetting bossbar = new BooleanSetting("Bossbar", (Boolean)false, (Hack)this);
    public BooleanSetting weather = new BooleanSetting("Weather", (Boolean)false, (Hack)this);
    public BooleanSetting time = new BooleanSetting("Change Time", (Boolean)false, (Hack)this);
    public BooleanSetting block = new BooleanSetting("Block", (Boolean)true, (Hack)this);
    public BooleanSetting water = new BooleanSetting("Water", (Boolean)true, (Hack)this);
    public IntSetting newTime = new IntSetting("Time", 0, 0, 23000, this, s -> this.time.getValue());

    public NoRender() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.blind.getValue().booleanValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue().booleanValue() && NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
        if (this.time.getValue().booleanValue()) {
            NoRender.mc.world.setWorldTime((long)this.newTime.getValue().intValue());
        }
        if (this.weather.getValue().booleanValue()) {
            NoRender.mc.world.setRainStrength(0.0f);
        }
    }

    @SubscribeEvent
    public void renderBlockEvent(RenderBlockOverlayEvent event) {
        if (this.block.getValue().booleanValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK) {
            event.setCanceled(true);
        }
        if (this.water.getValue().booleanValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER) {
            event.setCanceled(true);
        }
    }

    @CommitEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate && this.time.getValue().booleanValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateBossInfo && this.bossbar.getValue().booleanValue()) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onLogin() {
        if (this.isEnabled()) {
            this.disable();
            this.enable();
        }
    }
}

