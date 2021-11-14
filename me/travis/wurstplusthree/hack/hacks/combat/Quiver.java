/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.PotionTypes
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTippedArrow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.potion.PotionUtils
 *  net.minecraft.util.math.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

@Hack.Registration(name="Quiver", description="shoots arrows like brr", category=Hack.Category.COMBAT, isListening=false)
public class Quiver
extends Hack {
    private int timer = 0;
    private int stage = 1;
    private int returnSlot = -1;
    private int oldHotbar;
    private Item expectedItem;

    @Override
    public void onEnable() {
        this.oldHotbar = Quiver.mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        this.timer = 0;
        this.stage = 0;
        Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
        Quiver.mc.player.inventory.currentItem = this.oldHotbar;
        if (this.returnSlot != -1) {
            Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, 9, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
            Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, this.returnSlot, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
            Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, 9, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
            Quiver.mc.playerController.updateController();
        }
        this.returnSlot = -1;
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onUpdateWalkingPlayerEvent(@NotNull UpdateWalkingPlayerEvent event) {
        WurstplusThree.ROTATION_MANAGER.setPlayerPitch(-90.0f);
    }

    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        if (Quiver.mc.currentScreen != null) {
            return;
        }
        if (InventoryUtil.findHotbarBlock(ItemBow.class) == -1) {
            this.disable();
            ClientMessage.sendMessage((Object)ChatFormatting.RED + "No bow found!");
            return;
        }
        if (this.stage != 0) {
            InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
        }
        if (this.stage == 0) {
            if (Quiver.mc.player.inventory.getStackInSlot(9).getItem() == this.expectedItem) {
                ++this.stage;
            } else if (!this.mapArrows()) {
                this.disable();
                return;
            }
        } else {
            if (this.stage == 1) {
                ++this.stage;
                ++this.timer;
                return;
            }
            if (this.stage == 2) {
                Quiver.mc.gameSettings.keyBindUseItem.pressed = true;
                this.timer = 0;
                ++this.stage;
            } else if (this.stage == 3) {
                if (this.timer > 5) {
                    ++this.stage;
                }
            } else if (this.stage == 4) {
                Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
                Quiver.mc.player.resetActiveHand();
                Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
                this.timer = 0;
                ++this.stage;
            } else if (this.stage == 5) {
                if (this.timer < 12) {
                    ++this.timer;
                    return;
                }
                this.stage = 0;
                this.timer = 0;
                this.expectedItem = null;
            }
        }
        ++this.timer;
    }

    private boolean mapArrows() {
        for (int a = 9; a < 45; ++a) {
            if (!(((ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a)).getItem() instanceof ItemTippedArrow)) continue;
            ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
            ItemStack currentArrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(9);
            if (!(!PotionUtils.getPotionFromItem((ItemStack)arrow).equals((Object)PotionTypes.STRENGTH) && !PotionUtils.getPotionFromItem((ItemStack)arrow).equals((Object)PotionTypes.STRONG_STRENGTH) && !PotionUtils.getPotionFromItem((ItemStack)arrow).equals((Object)PotionTypes.LONG_STRENGTH) || Quiver.mc.player.isPotionActive(MobEffects.STRENGTH) || PotionUtils.getPotionFromItem((ItemStack)currentArrow).equals((Object)PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem((ItemStack)currentArrow).equals((Object)PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem((ItemStack)currentArrow).equals((Object)PotionTypes.LONG_STRENGTH))) {
                this.switchTo(a);
                this.expectedItem = Quiver.mc.player.inventory.getStackInSlot(a).getItem();
                return true;
            }
            if (!PotionUtils.getPotionFromItem((ItemStack)arrow).equals((Object)PotionTypes.SWIFTNESS) && !PotionUtils.getPotionFromItem((ItemStack)arrow).equals((Object)PotionTypes.LONG_SWIFTNESS) && !PotionUtils.getPotionFromItem((ItemStack)arrow).equals((Object)PotionTypes.STRONG_SWIFTNESS) || Quiver.mc.player.isPotionActive(MobEffects.SPEED) || PotionUtils.getPotionFromItem((ItemStack)currentArrow).equals((Object)PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem((ItemStack)currentArrow).equals((Object)PotionTypes.STRONG_SWIFTNESS) || PotionUtils.getPotionFromItem((ItemStack)currentArrow).equals((Object)PotionTypes.LONG_SWIFTNESS)) continue;
            this.switchTo(a);
            this.expectedItem = Quiver.mc.player.inventory.getStackInSlot(a).getItem();
            return true;
        }
        return false;
    }

    private void switchTo(int from) {
        if (from == 9) {
            return;
        }
        if (this.returnSlot == -1) {
            this.returnSlot = from;
        }
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, 9, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.updateController();
    }
}

