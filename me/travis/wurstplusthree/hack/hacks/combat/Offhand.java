/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemSword
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.MoveEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.CrystalUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;

@Hack.Registration(name="Offhand", description="puts things in ur offhand", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public class Offhand
extends Hack {
    EnumSetting mode = new EnumSetting("Mode", "Totem", Arrays.asList("Totem", "Crystal", "Gapple"), this);
    BooleanSetting cancelMovement = new BooleanSetting("CancelMovement", (Boolean)false, (Hack)this);
    IntSetting TotemHp = new IntSetting("TotemHP", 16, 0, 36, this);
    IntSetting HoleHP = new IntSetting("HoleHP", 16, 0, 36, this);
    BooleanSetting GapSwitch = new BooleanSetting("GapSwap", (Boolean)false, (Hack)this);
    BooleanSetting GapOnSword = new BooleanSetting("SwordGap", (Boolean)false, (Hack)this, s -> this.GapSwitch.getValue());
    BooleanSetting GapOnPick = new BooleanSetting("PickGap", (Boolean)false, (Hack)this, s -> this.GapSwitch.getValue());
    BooleanSetting Always = new BooleanSetting("Always", (Boolean)false, (Hack)this, s -> this.GapSwitch.getValue());
    BooleanSetting CrystalCheck = new BooleanSetting("CrystalCheck", (Boolean)false, (Hack)this);
    BooleanSetting check32K = new BooleanSetting("32KCheck", (Boolean)false, (Hack)this);
    IntSetting cooldown = new IntSetting("Cooldown", 0, 0, 40, this);
    private int timer = 0;

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (Offhand.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        float hp = EntityUtil.getHealth((Entity)Offhand.mc.player);
        if (hp < (float)this.TotemHp.getValue().intValue()) {
            if (this.cancelMovement.getValue().booleanValue()) {
                StopPlayerMovement.toggle(true);
            }
            this.swapItems(this.getItemSlot(Items.TOTEM_OF_UNDYING));
            if (this.cancelMovement.getValue().booleanValue()) {
                StopPlayerMovement.toggle(false);
            }
        }
    }

    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        ++this.timer;
        if (Offhand.mc.currentScreen == null || Offhand.mc.currentScreen instanceof GuiInventory) {
            float hp = Offhand.mc.player.getHealth() + Offhand.mc.player.getAbsorptionAmount();
            if ((hp > (float)this.TotemHp.getValue().intValue() || EntityUtil.isInHole((Entity)Offhand.mc.player) && hp > (float)this.HoleHP.getValue().intValue()) && this.lethalToLocalCheck() && this.Check32K()) {
                if (!(!this.mode.getValue().equalsIgnoreCase("crystal") || (this.GapOnSword.getValue().booleanValue() && Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || this.Always.getValue().booleanValue() || this.GapOnPick.getValue().booleanValue() && Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown() && this.GapSwitch.getValue().booleanValue())) {
                    this.swapItems(this.getItemSlot(Items.END_CRYSTAL));
                    return;
                }
                if ((this.GapOnSword.getValue().booleanValue() && Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || this.Always.getValue().booleanValue() || this.GapOnPick.getValue().booleanValue() && Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown() && this.GapSwitch.getValue().booleanValue()) {
                    this.swapItems(this.getItemSlot(Items.GOLDEN_APPLE));
                    if (Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                        Offhand.mc.playerController.isHittingBlock = true;
                    }
                    return;
                }
                if (this.mode.getValue().equalsIgnoreCase("totem")) {
                    this.swapItems(this.getItemSlot(Items.TOTEM_OF_UNDYING));
                    return;
                }
                if (this.mode.getValue().equalsIgnoreCase("gapple")) {
                    this.swapItems(this.getItemSlot(Items.GOLDEN_APPLE));
                    return;
                }
            } else {
                this.swapItems(this.getItemSlot(Items.TOTEM_OF_UNDYING));
                return;
            }
            if (Offhand.mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
                this.swapItems(this.getItemSlot(Items.TOTEM_OF_UNDYING));
            }
        }
    }

    private boolean Check32K() {
        if (!this.check32K.getValue().booleanValue() || Offhand.mc.world == null || Offhand.mc.player == null) {
            return true;
        }
        for (Entity entity : Offhand.mc.world.loadedEntityList) {
            if (entity == Offhand.mc.player || !WurstplusThree.FRIEND_MANAGER.isFriend(entity.getName()) || !(entity instanceof EntityPlayer) || !(entity.getDistance((Entity)Offhand.mc.player) < 7.0f) || !EntityUtil.holding32k((EntityPlayer)entity)) continue;
            return true;
        }
        return true;
    }

    private boolean lethalToLocalCheck() {
        if (!this.CrystalCheck.getValue().booleanValue()) {
            return true;
        }
        for (Entity entity : Offhand.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(Offhand.mc.player.getDistance(entity) <= 12.0f)) continue;
            BlockPos blockPos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (!(CrystalUtil.calculateDamage(blockPos, (Entity)Offhand.mc.player, false) >= Offhand.mc.player.getHealth())) continue;
            return false;
        }
        return true;
    }

    public void swapItems(int slot) {
        if (slot == -1 || this.timer <= this.cooldown.getValue() && Offhand.mc.player.inventory.getStackInSlot(slot).getItem() != Items.TOTEM_OF_UNDYING) {
            return;
        }
        this.timer = 0;
        Offhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
        Offhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
        Offhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
        Offhand.mc.playerController.updateController();
    }

    private int getItemSlot(Item input) {
        if (input == Offhand.mc.player.getHeldItemOffhand().getItem()) {
            return -1;
        }
        for (int i = 36; i >= 0; --i) {
            Item item = Offhand.mc.player.inventory.getStackInSlot(i).getItem();
            if (item != input) continue;
            if (i < 9) {
                if (input == Items.GOLDEN_APPLE) {
                    return -1;
                }
                i += 36;
            }
            return i;
        }
        return -1;
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.getValue();
    }

    public static class StopPlayerMovement {
        private static final StopPlayerMovement stopPlayerMovement = new StopPlayerMovement();

        public static void toggle(boolean on) {
            if (on) {
                WurstplusThree.EVENT_PROCESSOR.addEventListener(stopPlayerMovement);
            } else {
                WurstplusThree.EVENT_PROCESSOR.removeEventListener(stopPlayerMovement);
            }
        }

        @CommitEvent(priority=EventPriority.HIGH)
        public void onMove(MoveEvent event) {
            event.setCancelled(true);
        }
    }
}

