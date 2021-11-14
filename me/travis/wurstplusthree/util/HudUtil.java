/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 */
package me.travis.wurstplusthree.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.hack.hacks.player.PlayerSpoofer;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.MathsUtil;
import me.travis.wurstplusthree.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HudUtil
implements Globals {
    public static String getWelcomerLine() {
        int time = TimeUtil.get_hour();
        String line = time >= 0 && time < 12 ? "Morning, " + (Object)ChatFormatting.GOLD + (Object)ChatFormatting.BOLD + (!PlayerSpoofer.INSTANCE.isEnabled() || PlayerSpoofer.INSTANCE.name == null ? HudUtil.mc.player.getName() : PlayerSpoofer.INSTANCE.name) + (Object)ChatFormatting.RESET + " you smell good today :)" : (time >= 12 && time < 16 ? "Afternoon, " + (Object)ChatFormatting.GOLD + (Object)ChatFormatting.BOLD + (!PlayerSpoofer.INSTANCE.isEnabled() || PlayerSpoofer.INSTANCE.name == null ? HudUtil.mc.player.getName() : PlayerSpoofer.INSTANCE.name) + (Object)ChatFormatting.RESET + " you're looking good today :)" : (time >= 16 && time < 24 ? "Evening, " + (Object)ChatFormatting.GOLD + (Object)ChatFormatting.BOLD + (!PlayerSpoofer.INSTANCE.isEnabled() || PlayerSpoofer.INSTANCE.name == null ? HudUtil.mc.player.getName() : PlayerSpoofer.INSTANCE.name) + (Object)ChatFormatting.RESET + " you smell good today :)" : "Welcome, " + (Object)ChatFormatting.GOLD + (Object)ChatFormatting.BOLD + (!PlayerSpoofer.INSTANCE.isEnabled() || PlayerSpoofer.INSTANCE.name == null ? HudUtil.mc.player.getName() : PlayerSpoofer.INSTANCE.name) + (Object)ChatFormatting.RESET + " you're looking fine today :)"));
        return line;
    }

    public static String getTotems() {
        String totems = "";
        int totemCount = HudUtil.mc.player.inventory.mainInventory.stream().filter(stack -> stack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::func_190916_E).sum() + (HudUtil.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING ? 1 : 0);
        totems = totemCount > 2 ? totems + (Object)ChatFormatting.GREEN : totems + (Object)ChatFormatting.RED;
        return totems + totemCount;
    }

    public static String getPingLine() {
        String line = "";
        int ping = WurstplusThree.SERVER_MANAGER.getPing();
        line = ping > 150 ? line + (Object)ChatFormatting.RED : (ping > 100 ? line + (Object)ChatFormatting.YELLOW : line + (Object)ChatFormatting.GREEN);
        return line + " " + ping;
    }

    public static String getTpsLine() {
        String line = "";
        double tps = MathsUtil.round(WurstplusThree.SERVER_MANAGER.getTPS(), 1);
        line = tps > 16.0 ? line + (Object)ChatFormatting.GREEN : (tps > 10.0 ? line + (Object)ChatFormatting.YELLOW : line + (Object)ChatFormatting.RED);
        return line + " " + tps;
    }

    public static String getFpsLine() {
        String line = "";
        int fps = Minecraft.getDebugFPS();
        line = fps > 120 ? line + (Object)ChatFormatting.GREEN : (fps > 60 ? line + (Object)ChatFormatting.YELLOW : line + (Object)ChatFormatting.RED);
        return line + " " + fps;
    }

    public static String getAnaTimeLine() {
        String line = "";
        line = line + (TimeUtil.get_hour() < 10 ? "0" + TimeUtil.get_hour() : Integer.valueOf(TimeUtil.get_hour()));
        line = line + ":";
        line = line + (TimeUtil.get_minuite() < 10 ? "0" + TimeUtil.get_minuite() : Integer.valueOf(TimeUtil.get_minuite()));
        line = line + ":";
        line = line + (TimeUtil.get_second() < 10 ? "0" + TimeUtil.get_second() : Integer.valueOf(TimeUtil.get_second()));
        return line;
    }

    public static String getDate() {
        return TimeUtil.get_year() + "/" + (TimeUtil.get_month() + 1) + "/" + TimeUtil.get_day();
    }

    public static void drawHudString(String string, int x, int y, int colour) {
        if (HudEditor.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(string, x, y, colour);
        } else {
            HudUtil.mc.fontRenderer.drawStringWithShadow(string, (float)x, (float)y, colour);
        }
    }

    public static int getHudStringWidth(String string) {
        if (HudEditor.INSTANCE.customFont.getValue().booleanValue()) {
            return WurstplusThree.GUI_FONT_MANAGER.getTextWidth(string);
        }
        return HudUtil.mc.fontRenderer.getStringWidth(string);
    }

    public static int getHudStringHeight() {
        if (HudEditor.INSTANCE.customFont.getValue().booleanValue()) {
            return WurstplusThree.GUI_FONT_MANAGER.getTextHeight();
        }
        return HudUtil.mc.fontRenderer.FONT_HEIGHT;
    }

    public static int getRightX(String string, int x) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        if (HudEditor.INSTANCE.customFont.getValue().booleanValue()) {
            return x - WurstplusThree.GUI_FONT_MANAGER.getTextWidth(string);
        }
        return x - HudUtil.mc.fontRenderer.getStringWidth(string);
    }
}

