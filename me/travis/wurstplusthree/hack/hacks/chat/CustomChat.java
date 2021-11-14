/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ChatType
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.fml.common.ObfuscationReflectionHelper
 */
package me.travis.wurstplusthree.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.gui.chat.GuiChat;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@Hack.Registration(name="Custom Chat", description="lets you customise chat", category=Hack.Category.CHAT, priority=HackPriority.Low)
public class CustomChat
extends Hack {
    public static CustomChat INSTANCE;
    public BooleanSetting customFont = new BooleanSetting("Custom Font", (Boolean)true, (Hack)this);
    public BooleanSetting rainbow = new BooleanSetting("Rainbow", (Boolean)false, (Hack)this);
    public BooleanSetting nameHighlight = new BooleanSetting("Name Highlight", (Boolean)true, (Hack)this);
    public BooleanSetting timeStamps = new BooleanSetting("Time Stamps", (Boolean)true, (Hack)this);
    public BooleanSetting colourTimeStamps = new BooleanSetting("Rainbow Time", (Boolean)true, (Hack)this, s -> this.timeStamps.getValue());
    public BooleanSetting suffix = new BooleanSetting("Suffix", (Boolean)false, (Hack)this);
    public BooleanSetting infinite = new BooleanSetting("Infinite", (Boolean)true, (Hack)this);
    public BooleanSetting smoothChat = new BooleanSetting("SmoothChat", (Boolean)false, (Hack)this);
    public DoubleSetting xOffset = new DoubleSetting("XOffset", (Double)0.0, (Double)0.0, (Double)600.0, this);
    public DoubleSetting yOffset = new DoubleSetting("YOffset", (Double)0.0, (Double)0.0, (Double)30.0, this);
    public DoubleSetting vSpeed = new DoubleSetting("VSpeed", (Double)30.0, (Double)1.0, (Double)100.0, this);
    public DoubleSetting vLength = new DoubleSetting("VLength", (Double)10.0, (Double)5.0, (Double)100.0, this);
    public DoubleSetting vIncrements = new DoubleSetting("VIncrements", (Double)1.0, (Double)1.0, (Double)5.0, this);
    public EnumSetting type = new EnumSetting("Type", "Horizontal", Arrays.asList("Horizontal", "Vertical"), this);
    public BooleanSetting help = new BooleanSetting("HelpMessages", (Boolean)true, (Hack)this);
    public static GuiChat guiChatSmooth;
    public static GuiNewChat guiChat;

    public CustomChat() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        guiChatSmooth = new GuiChat(mc);
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, (Object)CustomChat.mc.ingameGUI, (Object)guiChatSmooth, (String[])new String[]{"field_73840_e"});
    }

    @Override
    public void onDisable() {
        guiChat = new GuiNewChat(mc);
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, (Object)CustomChat.mc.ingameGUI, (Object)guiChat, (String[])new String[]{"field_73840_e"});
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/")) {
                return;
            }
            if (this.suffix.getValue().booleanValue()) {
                s = s + " \u25e6 \u0461\u028b\u0433\u0455t\u0440\u0406\u028b\u0455 \u01b7";
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            if (((SPacketChat)event.getPacket()).getType() == ChatType.GAME_INFO) {
                return;
            }
            String originalMessage = ((SPacketChat)event.getPacket()).chatComponent.getFormattedText();
            String message = this.getTimeString(originalMessage);
            if (this.nameHighlight.getValue().booleanValue()) {
                try {
                    message = message.replace(CustomChat.mc.player.getName(), (Object)ChatFormatting.GOLD + CustomChat.mc.player.getName() + (Object)ChatFormatting.RESET);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ((SPacketChat)event.getPacket()).chatComponent = new TextComponentString(message);
        }
    }

    private String getTimeString(String message) {
        if (this.timeStamps.getValue().booleanValue()) {
            String date = new SimpleDateFormat("k:mm").format(new Date());
            return "[" + date + "] " + (this.colourTimeStamps.getValue() != false ? (Object)ChatFormatting.RESET + message : message);
        }
        return message;
    }

    @Override
    public void onLogin() {
        if (this.isEnabled()) {
            this.disable();
            this.enable();
        }
    }
}

