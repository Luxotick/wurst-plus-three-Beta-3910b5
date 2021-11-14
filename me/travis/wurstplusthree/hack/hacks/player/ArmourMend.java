/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemStack
 */
package me.travis.wurstplusthree.hack.hacks.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;

@Hack.Registration(name="Auto Armour", description="automates ur armour", category=Hack.Category.PLAYER)
public class ArmourMend
extends Hack {
    IntSetting delay = new IntSetting("Delay", 50, 0, 500, this);
    BooleanSetting mendingTakeOff = new BooleanSetting("AutoMend", (Boolean)true, (Hack)this);
    IntSetting enemyRange = new IntSetting("EnemyRange", 8, 0, 25, this, s -> this.mendingTakeOff.getValue());
    IntSetting helmetThreshold = new IntSetting("Helmet", 80, 0, 100, this, s -> this.mendingTakeOff.getValue());
    IntSetting chestThreshold = new IntSetting("Chest", 80, 0, 100, this, s -> this.mendingTakeOff.getValue());
    IntSetting legThreshold = new IntSetting("Leggings", 80, 0, 100, this, s -> this.mendingTakeOff.getValue());
    IntSetting bootsThreshold = new IntSetting("Boots", 80, 0, 100, this, s -> this.mendingTakeOff.getValue());
    IntSetting actions = new IntSetting("Actions", 3, 1, 10, this, s -> this.mendingTakeOff.getValue());
    private final Timer timer = new Timer();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final List<Integer> doneSlots = new ArrayList<Integer>();
    boolean flag;

    @Override
    public void onLogin() {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.flag = false;
    }

    @Override
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }

    @Override
    public void onTick() {
        if (this.nullCheck() || ArmourMend.mc.currentScreen instanceof GuiContainer && !(ArmourMend.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.taskList.isEmpty()) {
            int slot;
            int slot2;
            int slot3;
            int slot4;
            if (this.mendingTakeOff.getValue().booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && ArmourMend.mc.gameSettings.keyBindUseItem.isKeyDown() && ArmourMend.mc.world.playerEntities.stream().noneMatch(e -> e != ArmourMend.mc.player && !WurstplusThree.FRIEND_MANAGER.isFriend(e.getName()) && ArmourMend.mc.player.getDistance((Entity)e) <= (float)this.enemyRange.getValue().intValue()) && !this.flag) {
                int dam;
                int takeOff = 0;
                for (Map.Entry<Integer, ItemStack> armorSlot : this.getArmor().entrySet()) {
                    ItemStack stack = armorSlot.getValue();
                    float percent = (float)this.helmetThreshold.getValue().intValue() / 100.0f;
                    dam = Math.round((float)stack.getMaxDamage() * percent);
                    if (dam >= stack.getMaxDamage() - stack.getItemDamage()) continue;
                    ++takeOff;
                }
                if (takeOff == 4) {
                    this.flag = true;
                }
                if (!this.flag) {
                    ItemStack itemStack1 = ArmourMend.mc.player.inventoryContainer.getSlot(5).getStack();
                    if (!itemStack1.isEmpty) {
                        float percent = (float)this.helmetThreshold.getValue().intValue() / 100.0f;
                        int dam2 = Math.round((float)itemStack1.getMaxDamage() * percent);
                        if (dam2 < itemStack1.getMaxDamage() - itemStack1.getItemDamage()) {
                            this.takeOffSlot(5);
                        }
                    }
                    ItemStack itemStack2 = ArmourMend.mc.player.inventoryContainer.getSlot(6).getStack();
                    if (!itemStack2.isEmpty) {
                        float percent = (float)this.chestThreshold.getValue().intValue() / 100.0f;
                        int dam3 = Math.round((float)itemStack2.getMaxDamage() * percent);
                        if (dam3 < itemStack2.getMaxDamage() - itemStack2.getItemDamage()) {
                            this.takeOffSlot(6);
                        }
                    }
                    ItemStack itemStack3 = ArmourMend.mc.player.inventoryContainer.getSlot(7).getStack();
                    if (!itemStack3.isEmpty) {
                        float percent = (float)this.legThreshold.getValue().intValue() / 100.0f;
                        dam = Math.round((float)itemStack3.getMaxDamage() * percent);
                        if (dam < itemStack3.getMaxDamage() - itemStack3.getItemDamage()) {
                            this.takeOffSlot(7);
                        }
                    }
                    ItemStack itemStack4 = ArmourMend.mc.player.inventoryContainer.getSlot(8).getStack();
                    if (!itemStack4.isEmpty) {
                        float percent = (float)this.bootsThreshold.getValue().intValue() / 100.0f;
                        int dam4 = Math.round((float)itemStack4.getMaxDamage() * percent);
                        if (dam4 < itemStack4.getMaxDamage() - itemStack4.getItemDamage()) {
                            this.takeOffSlot(8);
                        }
                    }
                }
                return;
            }
            this.flag = false;
            ItemStack helm = ArmourMend.mc.player.inventoryContainer.getSlot(5).getStack();
            if (helm.getItem() == Items.AIR && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, true, true)) != -1) {
                this.getSlotOn(5, slot4);
            }
            if (ArmourMend.mc.player.inventoryContainer.getSlot(6).getStack().getItem() == Items.AIR && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, true, true)) != -1) {
                this.getSlotOn(6, slot3);
            }
            if (ArmourMend.mc.player.inventoryContainer.getSlot(7).getStack().getItem() == Items.AIR && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, true, true)) != -1) {
                this.getSlotOn(7, slot2);
            }
            if (ArmourMend.mc.player.inventoryContainer.getSlot(8).getStack().getItem() == Items.AIR && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, true, true)) != -1) {
                this.getSlotOn(8, slot);
            }
        }
        if (this.timer.passedMs((int)((float)this.delay.getValue().intValue() * WurstplusThree.SERVER_MANAGER.getTpsFactor()))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.getValue(); ++i) {
                    InventoryUtil.Task task = this.taskList.poll();
                    if (task == null) continue;
                    task.run();
                }
            }
            this.timer.reset();
        }
    }

    private void takeOffSlot(int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (int i : InventoryUtil.findEmptySlots(true)) {
                if (this.doneSlots.contains(target)) continue;
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                this.taskList.add(new InventoryUtil.Task(slot));
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    private void getSlotOn(int slot, int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object)target);
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    private Map<Integer, ItemStack> getArmor() {
        return this.getInventorySlots(5, 8);
    }

    private Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)ArmourMend.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
}

