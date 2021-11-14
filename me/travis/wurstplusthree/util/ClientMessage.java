/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.TextComponentBase
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.event.HoverEvent
 *  net.minecraft.util.text.event.HoverEvent$Action
 */
package me.travis.wurstplusthree.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.chat.ToggleMessages;
import me.travis.wurstplusthree.util.Globals;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;

public class ClientMessage
implements Globals {
    private static final String opener() {
        if (ClientMessage.mc.player.getName().equalsIgnoreCase("wallhacks_")) {
            return (Object)ChatFormatting.DARK_BLUE + "[" + (Object)ChatFormatting.BLUE + "wurst+" + (Object)ChatFormatting.DARK_BLUE + "] " + (Object)ChatFormatting.RESET;
        }
        return (Object)ChatFormatting.GOLD + "Wurst+3" + (Object)ChatFormatting.WHITE + " : " + (Object)ChatFormatting.RESET;
    }

    public static void sendToggleMessage(Hack hack, boolean enabled) {
        if (ClientMessage.mc.world != null && ClientMessage.mc.player != null && WurstplusThree.HACKS.ishackEnabled("Toggle msgs")) {
            if (hack.getName().equalsIgnoreCase("gui")) {
                return;
            }
            ChatFormatting open = enabled ? ChatFormatting.GREEN : ChatFormatting.RED;
            boolean compact = ToggleMessages.INSTANCE.compact.getValue();
            if (hack.getName().equalsIgnoreCase("crystal aura")) {
                if (open == ChatFormatting.GREEN) {
                    ClientMessage.sendMessage("we " + (Object)open + "gaming", !compact);
                } else {
                    ClientMessage.sendMessage("we aint " + (Object)open + "gaming " + (Object)ChatFormatting.RESET + "no more", !compact);
                }
            } else {
                ClientMessage.sendMessage((Object)open + hack.getName(), !compact);
            }
        }
    }

    public static void sendMessage(String message) {
        ClientMessage.sendClientMessage(ClientMessage.opener() + message);
    }

    public static void sendIRCMessage(String message) {
        ClientMessage.sendMessage((Object)ChatFormatting.WHITE + "[" + (Object)ChatFormatting.AQUA + "IRC - " + (Object)((Object)WurstplusThree.CHAT_HANDLING.mode) + (Object)ChatFormatting.WHITE + "] " + (Object)ChatFormatting.RESET + message);
    }

    public static void sendErrorMessage(String message) {
        ClientMessage.sendClientMessage(ClientMessage.opener() + (Object)ChatFormatting.RED + message);
    }

    private static void sendClientMessage(String message) {
        if (ClientMessage.mc.player != null) {
            ClientMessage.mc.player.sendMessage((ITextComponent)new ChatMessage(message));
        }
    }

    public static void sendOverwriteClientMessage(String message) {
        if (ClientMessage.mc.player != null) {
            ITextComponent itc = new TextComponentString(ClientMessage.opener() + message).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (ITextComponent)new TextComponentString("Travis Fitzroy"))));
            ClientMessage.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, 5936);
        }
    }

    public static void sendRainbowMessage(String message) {
        StringBuilder stringBuilder = new StringBuilder(message);
        stringBuilder.insert(0, "\u00a7+");
        ClientMessage.mc.player.sendMessage((ITextComponent)new ChatMessage(stringBuilder.toString()));
    }

    public static void sendMessage(String message, boolean perm) {
        if (ClientMessage.mc.player == null) {
            return;
        }
        try {
            TextComponentString component = new TextComponentString(ClientMessage.opener() + message);
            int i = perm ? 0 : 12076;
            ClientMessage.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)component, i);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static class ChatMessage
    extends TextComponentBase {
        String message_input;

        public ChatMessage(String message) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(message);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String replacement = "\u00a7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            this.message_input = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.message_input;
        }

        public ITextComponent createCopy() {
            return new ChatMessage(this.message_input);
        }
    }
}

