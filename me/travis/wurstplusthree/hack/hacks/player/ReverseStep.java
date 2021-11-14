/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.travis.wurstplusthree.hack.hacks.player;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import net.minecraft.entity.Entity;

@Hack.Registration(name="Reverse Step", description="pulls u down down down", category=Hack.Category.PLAYER)
public class ReverseStep
extends Hack {
    DoubleSetting height = new DoubleSetting("Height", (Double)2.0, (Double)0.0, (Double)10.0, this);

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        try {
            if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isOnLadder() || WurstplusThree.HACKS.ishackEnabled("Speed")) {
                return;
            }
        }
        catch (Exception ignored) {
            return;
        }
        if (ReverseStep.mc.player.onGround) {
            for (double y = 0.0; y < this.height.getValue() + 0.5; y += 0.01) {
                if (ReverseStep.mc.world.getCollisionBoxes((Entity)ReverseStep.mc.player, ReverseStep.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) continue;
                ReverseStep.mc.player.motionY = -10.0;
                break;
            }
        }
    }
}

