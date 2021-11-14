/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemExpBottle
 */
package me.travis.wurstplusthree.hack.hacks.player;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

@Hack.Registration(name="Fast Util", description="util but fast", category=Hack.Category.PLAYER)
public class FastUtil
extends Hack {
    BooleanSetting xp = new BooleanSetting("XP", (Boolean)true, (Hack)this);
    BooleanSetting crystal = new BooleanSetting("Crystal", (Boolean)true, (Hack)this);

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        Item main = FastUtil.mc.player.getHeldItemMainhand().getItem();
        Item off = FastUtil.mc.player.getHeldItemOffhand().getItem();
        boolean mainXP = main instanceof ItemExpBottle;
        boolean offXP = off instanceof ItemExpBottle;
        boolean mainC = main instanceof ItemEndCrystal;
        boolean offC = off instanceof ItemEndCrystal;
        if (mainXP | offXP && this.xp.getValue().booleanValue()) {
            FastUtil.mc.rightClickDelayTimer = 0;
        }
        if (mainC | offC && this.crystal.getValue().booleanValue()) {
            FastUtil.mc.rightClickDelayTimer = 0;
        }
    }
}

