/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.BlockTNT
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemFlintAndSteel
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import java.util.Arrays;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Auto Dupe", category=Hack.Category.MISC, description="Dupes for u", priority=HackPriority.Low)
public final class AutoDupe
extends Hack {
    EnumSetting server = new EnumSetting("Server", "Wurst.Plus", Arrays.asList("Wurst.Plus"), this);
    EnumSetting modes = new EnumSetting("Mode", "Main", Arrays.asList("Main", "Slave"), this, s -> this.server.is("Wurst.Plus"));
    BooleanSetting sendChantMessage = new BooleanSetting("Chat Message", (Boolean)true, (Hack)this, s -> this.server.is("Wurst.Plus"));
    BooleanSetting waitItems = new BooleanSetting("Wait For Items", (Boolean)true, (Hack)this, s -> this.server.is("Wurst.Plus"));
    IntSetting delay = new IntSetting("Dupe Delay", 1, 0, 25, this);
    BooleanSetting rotations = new BooleanSetting("Rotations", (Boolean)false, (Hack)this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);
    private int stage;
    private BlockPos tntPos = null;
    private BlockPos chestPos = null;
    private boolean shouldWait;
    private int waitTicks;

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onEnable() {
        this.stage = 0;
        this.tntPos = null;
        this.chestPos = null;
        this.shouldWait = false;
        this.waitTicks = 0;
    }

    @Override
    public void onUpdate() {
        Object dir;
        BlockPos pPos;
        if (this.nullCheck()) {
            return;
        }
        if (this.server.is("Wurst.Plus") && this.modes.is("Slave")) {
            pPos = PlayerUtil.getPlayerPos();
            dir = PlayerUtil.getFacing();
            switch (1.$SwitchMap$me$travis$wurstplusthree$util$PlayerUtil$FacingDirection[((Enum)dir).ordinal()]) {
                case 1: {
                    this.chestPos = new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() - 1);
                    break;
                }
                case 2: {
                    this.chestPos = new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() + 1);
                    break;
                }
                case 3: {
                    this.chestPos = new BlockPos(pPos.getX() + 1, pPos.getY(), pPos.getZ());
                    break;
                }
                case 4: {
                    this.chestPos = new BlockPos(pPos.getX() - 1, pPos.getY(), pPos.getZ());
                }
            }
            if (this.chestPos == null) {
                ClientMessage.sendMessage("Face a direction!");
                this.toggle();
                return;
            }
            if (AutoDupe.mc.world.getBlockState(this.chestPos).getBlock() == Blocks.CHEST && !(AutoDupe.mc.currentScreen instanceof GuiChest)) {
                BlockUtil.openBlock(this.chestPos);
            }
        }
        if (this.server.is("Wurst.Plus") && this.modes.is("Main")) {
            pPos = PlayerUtil.getPlayerPos();
            if (this.shouldWait) {
                if (this.waitTicks < this.delay.getValue()) {
                    ++this.waitTicks;
                    ClientMessage.sendMessage("Waiting for " + this.waitTicks);
                    return;
                }
                this.shouldWait = false;
                this.stage = 0;
                this.waitTicks = 0;
                if (this.sendChantMessage.getValue().booleanValue()) {
                    AutoDupe.mc.player.sendChatMessage("Auto duping thanks to Wurst + 3!");
                }
                return;
            }
            if (this.stage == 5 && AutoDupe.mc.world.getBlockState(this.chestPos).getBlock() != Blocks.CHEST) {
                if (this.waitItems.getValue().booleanValue()) {
                    for (Entity entity : AutoDupe.mc.world.loadedEntityList) {
                        if (!(entity instanceof EntityItem) || !(entity.getDistance((double)this.chestPos.getX(), (double)this.chestPos.getY(), (double)this.chestPos.getZ()) <= 3.0)) continue;
                        this.shouldWait = false;
                        ClientMessage.sendMessage("Waiting for items to be picked up!");
                        return;
                    }
                }
                this.shouldWait = true;
                return;
            }
            switch (this.stage) {
                case 0: {
                    dir = PlayerUtil.getFacing();
                    switch (1.$SwitchMap$me$travis$wurstplusthree$util$PlayerUtil$FacingDirection[((Enum)dir).ordinal()]) {
                        case 1: {
                            this.chestPos = new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() - 1);
                            break;
                        }
                        case 2: {
                            this.chestPos = new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() + 1);
                            break;
                        }
                        case 3: {
                            this.chestPos = new BlockPos(pPos.getX() + 1, pPos.getY(), pPos.getZ());
                            break;
                        }
                        case 4: {
                            this.chestPos = new BlockPos(pPos.getX() - 1, pPos.getY(), pPos.getZ());
                        }
                    }
                    if (this.chestPos == null) {
                        ClientMessage.sendMessage("Face a direction!");
                        this.toggle();
                        return;
                    }
                    int cSlot = InventoryUtil.findHotbarBlock(BlockChest.class);
                    if (cSlot == -1) {
                        ClientMessage.sendMessage("You do not have a chest in you hotbar!");
                        this.toggle();
                        return;
                    }
                    BlockUtil.placeBlock(this.chestPos, cSlot, (boolean)this.rotations.getValue(), (boolean)this.rotations.getValue(), this.swing);
                    ++this.stage;
                    return;
                }
                case 1: {
                    PlayerUtil.FacingDirection dir1 = PlayerUtil.getFacing();
                    switch (dir1) {
                        case NORTH: {
                            this.tntPos = new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() - 2);
                            break;
                        }
                        case SOUTH: {
                            this.tntPos = new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() + 2);
                            break;
                        }
                        case EAST: {
                            this.tntPos = new BlockPos(pPos.getX() + 2, pPos.getY(), pPos.getZ());
                            break;
                        }
                        case WEST: {
                            this.tntPos = new BlockPos(pPos.getX() - 2, pPos.getY(), pPos.getZ());
                        }
                    }
                    if (this.tntPos == null) {
                        ClientMessage.sendMessage("Face a direction!");
                        this.toggle();
                        return;
                    }
                    int tntSlot = InventoryUtil.findHotbarBlock(BlockTNT.class);
                    if (tntSlot == -1) {
                        ClientMessage.sendMessage("You do not have tnt in you hotbar!");
                        this.toggle();
                        return;
                    }
                    BlockUtil.placeBlock(this.tntPos, tntSlot, (boolean)this.rotations.getValue(), (boolean)this.rotations.getValue(), this.swing);
                    ++this.stage;
                    return;
                }
                case 2: {
                    if (this.tntPos == null) {
                        ClientMessage.sendMessage("Face a direction!");
                        this.toggle();
                        return;
                    }
                    if (AutoDupe.mc.world.getBlockState(this.tntPos).getBlock() != Blocks.TNT) {
                        ClientMessage.sendMessage("There is no tnt placed!");
                        this.toggle();
                        return;
                    }
                    int flitSlot = InventoryUtil.findHotbarBlock(ItemFlintAndSteel.class);
                    if (flitSlot == -1) {
                        ClientMessage.sendMessage("There is no flit and steel in your hotbar");
                        this.toggle();
                        return;
                    }
                    int old = AutoDupe.mc.playerController.currentPlayerItem;
                    AutoDupe.mc.player.inventory.currentItem = flitSlot;
                    AutoDupe.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(flitSlot));
                    AutoDupe.mc.playerController.syncCurrentPlayItem();
                    this.useFlint(this.tntPos, EnumHand.MAIN_HAND);
                    AutoDupe.mc.player.inventory.currentItem = old;
                    AutoDupe.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(old));
                    AutoDupe.mc.playerController.syncCurrentPlayItem();
                    ++this.stage;
                    return;
                }
                case 3: {
                    AutoDupe.mc.player.closeScreen();
                    BlockUtil.openBlock(this.chestPos);
                    ++this.stage;
                    return;
                }
                case 4: {
                    if (AutoDupe.mc.currentScreen instanceof GuiChest) {
                        for (int i = 9; i < AutoDupe.mc.player.inventory.getSizeInventory() + 13; ++i) {
                            AutoDupe.mc.playerController.windowClick(AutoDupe.mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoDupe.mc.player);
                        }
                    }
                    ++this.stage;
                    return;
                }
            }
        }
    }

    public void useFlint(BlockPos pos, EnumHand swingHand) {
        RayTraceResult result = AutoDupe.mc.world.rayTraceBlocks(new Vec3d(AutoDupe.mc.player.posX, AutoDupe.mc.player.posY + (double)AutoDupe.mc.player.getEyeHeight(), AutoDupe.mc.player.posZ), new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() - 0.5, (double)pos.getZ() + 0.5));
        EnumFacing f = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        AutoDupe.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, f, swingHand, 0.0f, 0.0f, 0.0f));
    }
}

