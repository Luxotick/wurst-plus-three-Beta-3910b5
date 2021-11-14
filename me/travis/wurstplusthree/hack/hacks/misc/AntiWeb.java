/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockWeb
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.BlockCollisionBoundingBoxEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import org.lwjgl.input.Keyboard;

@Hack.Registration(name="Anti Web", description="stops faggots from trapping you in webs", category=Hack.Category.MISC, priority=HackPriority.High)
public class AntiWeb
extends Hack {
    BooleanSetting disableBB = new BooleanSetting("AddBB", (Boolean)true, (Hack)this);
    DoubleSetting bbOffset = new DoubleSetting("BBOffset", (Double)0.0, (Double)-2.0, (Double)2.0, this, s -> this.disableBB.getValue());
    BooleanSetting onGround = new BooleanSetting("OnGround", (Boolean)true, (Hack)this);
    DoubleSetting motionY = new DoubleSetting("SetMotionY", (Double)1.0, (Double)0.0, (Double)20.0, this);
    DoubleSetting motionX = new DoubleSetting("SetMotionX", (Double)0.84, (Double)-1.0, (Double)5.0, this);

    @CommitEvent(priority=EventPriority.LOW)
    public void bbEvent(BlockCollisionBoundingBoxEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (AntiWeb.mc.world.getBlockState(event.getPos()).getBlock() instanceof BlockWeb && this.disableBB.getValue().booleanValue()) {
            event.setCancelled(true);
            event.setBoundingBox(Block.FULL_BLOCK_AABB.contract(0.0, this.bbOffset.getValue().doubleValue(), 0.0));
        }
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (AntiWeb.mc.player.isInWeb && !WurstplusThree.HACKS.ishackEnabled("Step")) {
            if (Keyboard.isKeyDown((int)AntiWeb.mc.gameSettings.keyBindSneak.keyCode)) {
                AntiWeb.mc.player.isInWeb = true;
                AntiWeb.mc.player.motionY *= this.motionY.getValue().doubleValue();
            } else if (this.onGround.getValue().booleanValue()) {
                AntiWeb.mc.player.onGround = false;
            }
            if (Keyboard.isKeyDown((int)AntiWeb.mc.gameSettings.keyBindForward.keyCode) || Keyboard.isKeyDown((int)AntiWeb.mc.gameSettings.keyBindBack.keyCode) || Keyboard.isKeyDown((int)AntiWeb.mc.gameSettings.keyBindLeft.keyCode) || Keyboard.isKeyDown((int)AntiWeb.mc.gameSettings.keyBindRight.keyCode)) {
                AntiWeb.mc.player.isInWeb = false;
                AntiWeb.mc.player.motionX *= this.motionX.getValue().doubleValue();
                AntiWeb.mc.player.motionZ *= this.motionX.getValue().doubleValue();
            }
        }
    }
}

