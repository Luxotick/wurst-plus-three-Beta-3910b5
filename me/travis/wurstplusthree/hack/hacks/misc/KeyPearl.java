/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.MouseUtil;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Hack.Registration(name="Key Pearl", description="uses packets to through shit", category=Hack.Category.MISC)
public class KeyPearl
extends Hack {
    KeySetting key = new KeySetting("Key", 0, (Hack)this);
    private boolean isButtonDown = false;
    int slot;
    int oldSlot;

    @Override
    public void onUpdate() {
        if (this.key.getKey() < -1) {
            if (Mouse.isButtonDown((int)MouseUtil.convertToMouse(this.key.getKey()))) {
                if (!this.isButtonDown && KeyPearl.mc.currentScreen == null) {
                    this.pearl();
                }
                this.isButtonDown = true;
            } else {
                this.isButtonDown = false;
            }
        } else if (this.key.getKey() > -1) {
            if (Keyboard.isKeyDown((int)this.key.getKey())) {
                if (!this.isButtonDown && KeyPearl.mc.currentScreen == null) {
                    this.pearl();
                }
                this.isButtonDown = true;
            } else {
                this.isButtonDown = false;
            }
        }
    }

    private void pearl() {
        this.oldSlot = KeyPearl.mc.player.inventory.currentItem;
        this.slot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        if (this.slot == -1) {
            return;
        }
        KeyPearl.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.slot));
        KeyPearl.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        KeyPearl.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlot));
    }
}

