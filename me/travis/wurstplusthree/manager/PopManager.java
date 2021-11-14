/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.travis.wurstplusthree.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.Globals;
import net.minecraft.entity.player.EntityPlayer;

public class PopManager
implements Globals {
    private Map<EntityPlayer, Integer> popList = new ConcurrentHashMap<EntityPlayer, Integer>();
    public final List<String> toAnnouce = new ArrayList<String>();

    public void onTotemPop(EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals((Object)PopManager.mc.player) && player.isEntityAlive()) {
            this.toAnnouce.add(this.getPopString(player, this.getTotemPops(player)));
        }
    }

    public void onDeath(EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals((Object)PopManager.mc.player)) {
            this.toAnnouce.add(this.getDeathString(player, this.getTotemPops(player)));
        }
        this.resetPops(player);
    }

    public void onLogout() {
        this.clearList();
    }

    public void clearList() {
        this.popList = new ConcurrentHashMap<EntityPlayer, Integer>();
    }

    public void resetPops(EntityPlayer player) {
        this.setTotemPops(player, 0);
    }

    public void popTotem(EntityPlayer player) {
        this.popList.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(EntityPlayer player, int amount) {
        this.popList.put(player, amount);
    }

    public int getTotemPops(EntityPlayer player) {
        return this.popList.get((Object)player) == null ? 0 : this.popList.get((Object)player);
    }

    private String getDeathString(EntityPlayer player, int pops) {
        if (WurstplusThree.FRIEND_MANAGER.isFriend(player.getName())) {
            return "DUDE! you just let " + (Object)ChatFormatting.AQUA + player.getName() + (Object)ChatFormatting.RESET + " DIE after popping " + (Object)ChatFormatting.GREEN + (Object)ChatFormatting.BOLD + pops + (Object)ChatFormatting.RESET + (pops == 1 ? " totem" : " totems");
        }
        return "LMAO " + (Object)ChatFormatting.RED + player.getName() + (Object)ChatFormatting.RESET + " just fucking DIED after popping " + (Object)ChatFormatting.GREEN + (Object)ChatFormatting.BOLD + pops + (Object)ChatFormatting.RESET + (pops == 1 ? " totem" : " totems");
    }

    private String getPopString(EntityPlayer player, int pops) {
        if (WurstplusThree.FRIEND_MANAGER.isFriend(player.getName())) {
            return "ur pal " + (Object)ChatFormatting.AQUA + player.getName() + (Object)ChatFormatting.RESET + " has now popped " + (Object)ChatFormatting.RED + (Object)ChatFormatting.BOLD + pops + (Object)ChatFormatting.RESET + (pops == 1 ? " totem" : " totems") + " go help them";
        }
        return "shitter known as " + (Object)ChatFormatting.RED + player.getName() + (Object)ChatFormatting.RESET + " has now popped " + (Object)ChatFormatting.RED + (Object)ChatFormatting.BOLD + pops + (Object)ChatFormatting.RESET + (pops == 1 ? " totem" : " totems");
    }
}

