/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.item.ItemShield
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.hack.hacks.player;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemShield;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Hack.Registration(name="NoSlow", description="Lets you move freely", category=Hack.Category.PLAYER, priority=HackPriority.Low)
public class NoSlow
extends Hack {
    BooleanSetting inventoryMove = new BooleanSetting("Inventory Move", (Boolean)true, (Hack)this);
    BooleanSetting noInput = new BooleanSetting("No Input GUIs", (Boolean)true, (Hack)this);
    BooleanSetting items = new BooleanSetting("Items", (Boolean)true, (Hack)this);

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.inventoryMove.getValue().booleanValue() && NoSlow.mc.currentScreen != null) {
            if (this.noInput.getValue().booleanValue() && NoSlow.mc.currentScreen instanceof GuiChat) {
                return;
            }
            NoSlow.mc.player.movementInput.moveStrafe = 0.0f;
            NoSlow.mc.player.movementInput.moveForward = 0.0f;
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindForward.getKeyCode(), (boolean)Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindForward.getKeyCode()));
            if (Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindForward.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveForward += 1.0f;
                NoSlow.mc.player.movementInput.forwardKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.forwardKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindBack.getKeyCode(), (boolean)Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindBack.getKeyCode()));
            if (Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindBack.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveForward -= 1.0f;
                NoSlow.mc.player.movementInput.backKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.backKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindLeft.getKeyCode(), (boolean)Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindLeft.getKeyCode()));
            if (Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindLeft.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveStrafe += 1.0f;
                NoSlow.mc.player.movementInput.leftKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.leftKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindRight.getKeyCode(), (boolean)Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindRight.getKeyCode()));
            if (Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindRight.getKeyCode())) {
                NoSlow.mc.player.movementInput.moveStrafe -= 1.0f;
                NoSlow.mc.player.movementInput.rightKeyDown = true;
            } else {
                NoSlow.mc.player.movementInput.rightKeyDown = false;
            }
            KeyBinding.setKeyBindState((int)NoSlow.mc.gameSettings.keyBindJump.getKeyCode(), (boolean)Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindJump.getKeyCode()));
            NoSlow.mc.player.movementInput.jump = Keyboard.isKeyDown((int)NoSlow.mc.gameSettings.keyBindJump.getKeyCode());
        }
    }

    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        if (NoSlow.mc.player.isHandActive() && NoSlow.mc.player.getHeldItem(NoSlow.mc.player.getActiveHand()).getItem() instanceof ItemShield && (NoSlow.mc.player.movementInput.moveStrafe != 0.0f || NoSlow.mc.player.movementInput.moveForward != 0.0f && NoSlow.mc.player.getItemInUseMaxCount() >= 8)) {
            NoSlow.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, NoSlow.mc.player.getHorizontalFacing()));
        }
    }

    @SubscribeEvent
    public void onMove(InputUpdateEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.items.getValue().booleanValue() && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            NoSlow.mc.player.movementInput.moveForward /= 0.2f;
            NoSlow.mc.player.movementInput.moveStrafe /= 0.2f;
        }
    }
}

