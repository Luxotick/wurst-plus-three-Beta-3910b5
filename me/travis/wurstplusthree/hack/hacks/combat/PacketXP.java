/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.util.MouseUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Hack.Registration(name="Silent Xp", description="uses exp with packets", category=Hack.Category.COMBAT, priority=HackPriority.Lowest)
public class PacketXP
extends Hack {
    KeySetting bind = new KeySetting("PacketBind", 0, (Hack)this);
    IntSetting lookPitch = new IntSetting("LookPitch", 90, 0, 100, this);
    BooleanSetting allowTakeOff = new BooleanSetting("AllowTakeOff", (Boolean)true, (Hack)this);
    IntSetting takeOffVal = new IntSetting("TakeOffVal", 100, 1, 100, this, s -> this.allowTakeOff.getValue());
    IntSetting delay = new IntSetting("Delay", 0, 0, 5, this, s -> this.allowTakeOff.getValue());
    private int delay_count;
    int prvSlot;

    @Override
    public void onEnable() {
        this.delay_count = 0;
    }

    @Override
    public void onUpdate() {
        if (this.bind.getKey() > -1) {
            if (Keyboard.isKeyDown((int)this.bind.getKey()) && PacketXP.mc.currentScreen == null) {
                this.usedXp();
            }
        } else if (this.bind.getKey() < -1 && Mouse.isButtonDown((int)MouseUtil.convertToMouse(this.bind.getKey())) && PacketXP.mc.currentScreen == null) {
            this.usedXp();
        }
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (PacketXP.mc.player.inventory.getStackInSlot(i).getItem() != Items.EXPERIENCE_BOTTLE) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private void usedXp() {
        int oldPitch = (int)PacketXP.mc.player.rotationPitch;
        this.prvSlot = PacketXP.mc.player.inventory.currentItem;
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.findExpInHotbar()));
        PacketXP.mc.player.rotationPitch = this.lookPitch.getValue().intValue();
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(PacketXP.mc.player.rotationYaw, (float)this.lookPitch.getValue().intValue(), true));
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        PacketXP.mc.player.rotationPitch = oldPitch;
        PacketXP.mc.player.inventory.currentItem = this.prvSlot;
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.prvSlot));
        if (this.allowTakeOff.getValue().booleanValue()) {
            this.takeArmorOff();
        }
    }

    private ItemStack getArmor(int first) {
        return (ItemStack)PacketXP.mc.player.inventoryContainer.getInventory().get(first);
    }

    private void takeArmorOff() {
        for (int slot = 5; slot <= 8; ++slot) {
            ItemStack item = this.getArmor(slot);
            double max_dam = item.getMaxDamage();
            double dam_left = item.getMaxDamage() - item.getItemDamage();
            double percent = dam_left / max_dam * 100.0;
            if (!(percent >= (double)this.takeOffVal.getValue().intValue()) || item.equals((Object)Items.AIR)) continue;
            if (!this.notInInv(Items.AIR).booleanValue()) {
                return;
            }
            if (this.delay_count < this.delay.getValue()) {
                ++this.delay_count;
                return;
            }
            this.delay_count = 0;
            PacketXP.mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, (EntityPlayer)PacketXP.mc.player);
        }
    }

    public Boolean notInInv(Item itemOfChoice) {
        int n = 0;
        if (itemOfChoice == PacketXP.mc.player.getHeldItemOffhand().getItem()) {
            return true;
        }
        for (int i = 35; i >= 0; --i) {
            Item item = PacketXP.mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;
            }
            if (item == itemOfChoice) continue;
            ++n;
        }
        if (n >= 35) {
            return false;
        }
        return true;
    }
}

