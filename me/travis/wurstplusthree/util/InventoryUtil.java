/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAir
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.travis.wurstplusthree.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.travis.wurstplusthree.util.Globals;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil
implements Globals {
    public static boolean isNull(ItemStack stack) {
        return stack == null || stack.getItem() instanceof ItemAir;
    }

    public static void switchToHotbarSlot(int slot, boolean silent) {
        if (InventoryUtil.mc.player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            InventoryUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            InventoryUtil.mc.playerController.updateController();
        } else {
            InventoryUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            InventoryUtil.mc.player.inventory.currentItem = slot;
            InventoryUtil.mc.playerController.updateController();
        }
    }

    public static boolean isBlock(Item item, Class c) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).getBlock();
            return c.isInstance((Object)block);
        }
        return false;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return InventoryUtil.getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)InventoryUtil.mc.player.inventoryContainer.getInventory().get(current));
        }
        return fullInventorySlots;
    }

    public static List<Integer> findEmptySlots(boolean withXCarry) {
        ArrayList<Integer> outPut = new ArrayList<Integer>();
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().isEmpty && entry.getValue().getItem() != Items.AIR) continue;
            outPut.add(entry.getKey());
        }
        if (withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack craftingStack = craftingSlot.getStack();
                if (!craftingStack.isEmpty() && craftingStack.getItem() != Items.AIR) continue;
                outPut.add(i);
            }
        }
        return outPut;
    }

    public static int findArmorSlot(EntityEquipmentSlot type2, boolean binding) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            boolean cursed;
            ItemArmor armor;
            ItemStack s = InventoryUtil.mc.player.inventoryContainer.getSlot(i).getStack();
            if (s.getItem() == Items.AIR || !(s.getItem() instanceof ItemArmor) || (armor = (ItemArmor)s.getItem()).getEquipmentSlot() != type2) continue;
            float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)s);
            boolean bl = cursed = binding && EnchantmentHelper.hasBindingCurse((ItemStack)s);
            if (!(currentDamage > damage) || cursed) continue;
            damage = currentDamage;
            slot = i;
        }
        return slot;
    }

    public static boolean holdingItem(Class c) {
        return InventoryUtil.isInstanceOf(InventoryUtil.mc.player.getHeldItemMainhand(), c) || InventoryUtil.isInstanceOf(InventoryUtil.mc.player.getHeldItemOffhand(), c);
    }

    public static boolean isInstanceOf(ItemStack stack, Class c) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        if (c.isInstance((Object)item)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem((Item)item);
            return c.isInstance((Object)block);
        }
        return false;
    }

    public static int findArmorSlot(EntityEquipmentSlot type2, boolean binding, boolean withXCarry) {
        int slot = InventoryUtil.findArmorSlot(type2, binding);
        if (slot == -1 && withXCarry) {
            float damage = 0.0f;
            for (int i = 1; i < 5; ++i) {
                boolean cursed;
                ItemArmor armor;
                Slot craftingSlot = (Slot)InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack craftingStack = craftingSlot.getStack();
                if (craftingStack.getItem() == Items.AIR || !(craftingStack.getItem() instanceof ItemArmor) || (armor = (ItemArmor)craftingStack.getItem()).getEquipmentSlot() != type2) continue;
                float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)craftingStack);
                boolean bl = cursed = binding && EnchantmentHelper.hasBindingCurse((ItemStack)craftingStack);
                if (!(currentDamage > damage) || cursed) continue;
                damage = currentDamage;
                slot = i;
            }
        }
        return slot;
    }

    public static void switchToHotbarSlot(Class c, boolean silent) {
        int slot = InventoryUtil.findHotbarBlock(c);
        if (slot > -1) {
            InventoryUtil.switchToHotbarSlot(slot, silent);
        }
    }

    public static int findHotbarBlock(Class c) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (c.isInstance((Object)stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof ItemBlock) || !c.isInstance((Object)((ItemBlock)stack.getItem()).getBlock())) continue;
            return i;
        }
        return -1;
    }

    public static class Task {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;

        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }

        public Task(int slot) {
            this.slot = slot;
            this.quickClick = false;
            this.update = false;
        }

        public Task(int slot, boolean quickClick) {
            this.slot = slot;
            this.quickClick = quickClick;
            this.update = false;
        }

        public void run() {
            if (this.update) {
                Globals.mc.playerController.updateController();
            }
            if (this.slot != -1) {
                Globals.mc.playerController.windowClick(0, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)Globals.mc.player);
            }
        }

        public boolean isSwitching() {
            return !this.update;
        }
    }
}

